package soda_roja.backend.service;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import soda_roja.backend.dtoRequest.ProductoDomicilioDTORequest;
import soda_roja.backend.dtoRequest.VentaDTORequest;
import soda_roja.backend.dtoRequestPut.VentaDTORequestPut;
import soda_roja.backend.dtoResponse.VentaDTOResponse;
import soda_roja.backend.model.*;
import soda_roja.backend.repository.*;
import soda_roja.backend.specification.VentaSpecification;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Optional;

@Service
public class VentaService {

    @Autowired
    private VentaRepository repository;

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private DomicilioRepository domicilioRepository;

    @Autowired
    private ProductoZonaRepository productoZonaRepository;
    @Autowired
    private ProductoDomicilioRepository productoDomicilioRepository;

    @Autowired
    private LineaPedidoRepository lineaPedidoRepository;
    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private MapToDTO mapToDTOMapper;
    @Autowired
    private ProductoRepository productoRepository;

    public List<VentaDTOResponse> getAll(String[] populate) {
        return repository.findAll().stream().map(v -> mapToDTO(v, populate)).toList();
    }

    public VentaDTOResponse getById(Long id, String[] populate) {
        return repository.findById(id)
                .map(venta -> mapToDTOMapper.mapToDTO(venta, populate))
                .orElseThrow(() -> new EntityNotFoundException("Venta no encontrada con id: " + id));
    }

    public Page<VentaDTOResponse> getByUserId(Long userId, String[] populate, String sortOption, String state, int page, int size) {

        // Build sort - handle null sortOption
        Sort sort = Sort.by("id").descending();
        if (sortOption != null) {
            switch (sortOption) {
                case "Mas Recientes" -> sort = Sort.by("fecha").descending();
                case "Mas Antiguos" -> sort = Sort.by("fecha").ascending();
                case "Menor Precio" -> sort = Sort.by("total").ascending();
                case "Mayor Precio" -> sort = Sort.by("total").descending();
                default -> sort = Sort.by("id").descending();
            }
        }

        Pageable pageable = PageRequest.of(page, size, sort);

        // Build specification filters
        VentaSpecification.VentaFiltrosDTO filtros = new VentaSpecification.VentaFiltrosDTO(state, userId);

        // Use specification with pageable
        return repository.findAll(VentaSpecification.filtrar(filtros), pageable)
                .map(v -> mapToDTO(v, populate));
    }
    @Transactional
    public VentaDTOResponse save(VentaDTORequest entidad, String[] populate) {
        Domicilio domicilio = findDomicilioOrThrow(entidad.getIdDomicilio());
        List<LineaPedido> lineasPedido = new ArrayList<>();
        final Double[] total = {0.0};
       entidad.getLineasPedido().forEach(lp -> {
           // busco el producto zona
            ProductoZona productoZona = productoZonaRepository.findByZonaIdAndProductoId(domicilio.getZona().getId(), lp.getProductoId())
                    .orElseThrow(() -> new EntityNotFoundException("ProductoZona no encontrado"));
            // calculo el precio
            Double precio = productoZona.getProducto().getPrecio();
            float subTotal = (float) (precio * lp.getCantidad());
            // creo al linea pedido
            LineaPedido lineaPedidoEntity = LineaPedido.builder()
                    .cantidad(lp.getCantidad())
                    .subtotal(subTotal)
                    .productoZona(productoZona)
                    .build();

            // guardo la linea pedido
            LineaPedido savedLineaPedido = lineaPedidoRepository.save(lineaPedidoEntity);
            // agrego la linea pedido al array
            lineasPedido.add(savedLineaPedido);
            //sumo al total
            total[0] += subTotal;
        });


        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 30);
        Date fechaActual = calendar.getTime();
        Venta venta = Venta.builder()
                .fecha(fechaActual)
                .total(total[0])
                .estado("Pendiente")
                .domicilio(domicilio)
                .lineasPedido(lineasPedido)
                .build();

        lineasPedido.forEach(lp -> lp.setVenta(venta));
        return mapToDTO(repository.save(venta), populate);
    }


    @Transactional
    public VentaDTOResponse saveByDriver(VentaDTORequest entidad, List<ProductoDomicilioDTORequest> productoDomicilio, String monto, String[] populate) {
        // Se restan todos los retornables que se devolvieron.
        productoDomicilio.forEach(pd -> {
            Domicilio domicilioEntity = domicilioRepository.findById(pd.getDomicilioId())
                    .orElseThrow(() -> new EntityNotFoundException("Domicilio no encontrado con id: " + pd.getDomicilioId()));

            ProductoDomicilio productoDomicilioEntity = domicilioEntity.getProductoDomicilio().stream()
                    .filter(p -> p.getProducto().getId().equals(pd.getProductoId()))
                    .findFirst()
                    .orElseThrow(() -> new EntityNotFoundException("ProductoDomicilio no encontrado para el productoId: " + pd.getProductoId() + " y domicilioId: " + pd.getDomicilioId()));

            productoDomicilioEntity.setCantVaciosActuales(productoDomicilioEntity.getCantVaciosActuales() - pd.getCantVaciosActuales());
            productoDomicilioRepository.save(productoDomicilioEntity);
        });


        Domicilio domicilio = findDomicilioOrThrow(entidad.getIdDomicilio());
        List<LineaPedido> lineasPedido = new ArrayList<>();
        final Double[] total = {0.0};
        entidad.getLineasPedido().forEach(lp -> {
            if (lp.getCantidad() != 0) {
                // busco el producto zona
                ProductoZona productoZona = productoZonaRepository.findById(lp.getProductoZonaId())
                        .orElseThrow(() -> new EntityNotFoundException("ProductoZona no encontrado"));
                // calculo el precio
                Double precio = productoZona.getProducto().getPrecio();
                float subTotal = (float) (precio * lp.getCantidad());
                // creo al linea pedido
                LineaPedido lineaPedidoEntity = LineaPedido.builder()
                        .cantidad(lp.getCantidad())
                        .subtotal(subTotal)
                        .productoZona(productoZona)
                        .build();

                // guardo la linea pedido
                LineaPedido savedLineaPedido = lineaPedidoRepository.save(lineaPedidoEntity);

                // agrego la linea pedido al array
                lineasPedido.add(savedLineaPedido);
                //sumo al total
                total[0] += subTotal;
            }
        });

        // Se suman los retornables dejados en cada línea de pedido
        entidad.getLineasPedido().forEach(lp -> {
            if (lp.getCantidad() != 0) {
                Domicilio domicilioEntity = domicilioRepository.findById(entidad.getIdDomicilio())
                        .orElseThrow(() -> new EntityNotFoundException("Domicilio no encontrado con id: " + entidad.getIdDomicilio()));

                Optional<ProductoDomicilio> productoDomicilioEntity = domicilioEntity.getProductoDomicilio().stream()
                        .filter(p -> p.getProducto().getId().equals(lp.getProductoZonaId()))
                        .findFirst();

                if (productoDomicilioEntity.isPresent()) {
                    ProductoDomicilio pde = productoDomicilioEntity.get();
                    pde.setCantVaciosActuales(pde.getCantVaciosActuales() + lp.getCantidad());
                    productoDomicilioRepository.save(pde);
                } else {
                    ProductoZona productoZona = productoZonaRepository.findById(lp.getProductoZonaId())
                            .orElseThrow(() -> new EntityNotFoundException("ProductoZona no encontrado con id: " + lp.getProductoZonaId()));
                    Producto productoEntity = productoRepository.findById(productoZona.getProducto().getId())
                            .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con id: " + productoZona.getProducto().getId()));

                    ProductoDomicilio nuevoProductoDomicilio = ProductoDomicilio.builder()
                            .domicilio(domicilioEntity)
                            .producto(productoEntity)
                            .cantVaciosActuales(lp.getCantidad())
                            .build();
                    productoDomicilioRepository.save(nuevoProductoDomicilio);
                }


            }
        });


        Calendar calendar2 = Calendar.getInstance();
        calendar2.add(Calendar.MINUTE, 30);
        Date fechaActual = calendar2.getTime();

        // Query BEFORE creating the venta to avoid Hibernate flush issues
        List<Venta> ventasEnProceso = repository.findWithEstadoEnProceso(entidad.getIdDomicilio());
        List<Venta> ventasPendientes = repository.findWithEstadoPendiente(entidad.getIdDomicilio());

        // Cambiar ventas pendientes a canceladas
        ventasPendientes.forEach(v -> {
            v.setEstado("Cancelada");
            repository.save(v);
        });

        // Eliminar ventas en proceso
        repository.deleteAll(ventasEnProceso);

        // NOW create and save the new venta after queries
        Venta venta = Venta.builder()
                .fecha(fechaActual)
                .total(total[0])
                .estado("Completada")
                .domicilio(domicilio)
                .lineasPedido(lineasPedido)
                .build();

        // Save venta first
        Venta savedVenta = repository.save(venta);

        // Then set the saved venta on lineasPedido
        lineasPedido.forEach(lp -> lp.setVenta(savedVenta));
        if(!monto.isEmpty()) {
            Pago pago = new Pago().builder()
                    .monto(Float.parseFloat(monto))
                    .fecha(new Date())
                    .metodoPago("Efectivo")
                    .persona(domicilio.getPersona())
                    .estado("Completado")
                    .build();
            pagoRepository.save(pago);
        }

        domicilio.getPersona().setSaldo((float)(domicilio.getPersona().getSaldo() - total[0] + (monto != null ? Double.parseDouble(monto) : 0.0)));
        domicilioRepository.save(domicilio);

        return mapToDTO(savedVenta, populate);
    }

    public VentaDTOResponse update(Long id, VentaDTORequestPut entidad, String[] populate) {
        Venta existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Venta no encontrada con id: " + id));
        if( entidad.getIdDomicilio() != null) {
            Domicilio domicilio = findDomicilioOrThrow(entidad.getIdDomicilio());
            existing.setDomicilio(domicilio);
        }
        if(entidad.getLineasPedidoIds() != null) {
            List<LineaPedido> lineasPedido = entidad.getLineasPedidoIds().stream()
                    .map(this::findLineaPedidoOrThrow)
                    .toList();
            if (lineasPedido.isEmpty()) {
			    throw new IllegalArgumentException("Una venta debe tener al menos una linea de pedido");
		    }
            lineasPedido.forEach(lp -> lp.setVenta(existing));
        }

        if (entidad.getTotal() != null) {
            existing.setTotal(entidad.getTotal());
        }
        if(entidad.getFecha() != null) {
            existing.setFecha(entidad.getFecha());
        }
        if(entidad.getEstado() != null) {
            existing.setEstado(entidad.getEstado());
        }


        return mapToDTO(repository.save(existing), populate);
    }

    public void delete(Long id) {
        Venta venta = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Venta no encontrada con id: " + id));
        repository.delete(venta);
    }

    private Domicilio findDomicilioOrThrow(Long id) {
        return domicilioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Domicilio no encontrado con id: " + id));
    }

    private LineaPedido findLineaPedidoOrThrow(Long id) {
        return lineaPedidoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("LineaPedido no encontrado con id: " + id));
    }

    private VentaDTOResponse mapToDTO(Venta venta, String[] populate) {
        return mapToDTOMapper.mapToDTO(venta, populate);
    }


    public VentaDTOResponse getVentasHoyByDomicilioId(Long domicilioId, String[] populate) {
        Domicilio domicilio = findDomicilioOrThrow(domicilioId);
        List<Venta> ventas = ventaRepository.findWithEstadoEnProceso(domicilioId);
        List<PedidoSemanal> pedidosSemanales = domicilio.getPedidosSemanal();
        List<LineaPedido> lineasPedidos = new ArrayList<>();
        List<Producto> productos = productoRepository.findByZona(domicilio.getZona().getId());
        final Double[] total = {0.0};
        if (pedidosSemanales != null && !pedidosSemanales.isEmpty()) {
            for (PedidoSemanal ps : pedidosSemanales) {
                ProductoZona productoZona = ps.getProductoZona();
                Double precio = productoZona.getProducto().getPrecio();
                float subTotal = (float) (precio * ps.getCantidad());

                LineaPedido lineaPedido = LineaPedido.builder()
                        .cantidad(ps.getCantidad())
                        .subtotal(subTotal)
                        .productoZona(productoZona)
                        .build();

                lineasPedidos.add(lineaPedido);

                total[0] += subTotal;
            }
        }
        if(ventas != null && !ventas.isEmpty()) {
            for (Venta v : ventas) {
                if (v.getLineasPedido() != null) {
                    for (LineaPedido lp : v.getLineasPedido()) {

                        LineaPedido lineaExistente = lineasPedidos.stream()
                            .filter(lped -> lped.getProductoZona().getProducto().getId().equals(lp.getProductoZona().getProducto().getId()))
                            .findFirst()
                            .orElse(null);

                        if (lineaExistente != null) {
                            int nuevaCantidad = lineaExistente.getCantidad() + lp.getCantidad();
                            lineaExistente.setCantidad(nuevaCantidad);

                            Double precio = lp.getProductoZona().getProducto().getPrecio();
                            float nuevoSubTotal = (float) (precio * nuevaCantidad);
                            lineaExistente.setSubtotal(nuevoSubTotal);

                            total[0] -= lp.getSubtotal();
                            total[0] += nuevoSubTotal;
                        } else {
                            lineasPedidos.add(lp);
                            total[0] += lp.getSubtotal();
                        }

                    }
                }
            }
        }
        if(productos != null && !productos.isEmpty()) {
            for (Producto p : productos) {
                LineaPedido lineaExistente = lineasPedidos.stream()
                        .filter(lped -> lped.getProductoZona().getProducto().getId().equals(p.getId()))
                        .findFirst()
                        .orElse(null);

                if (lineaExistente == null) {
                    ProductoZona productoZona = productoZonaRepository.findByZonaIdAndProductoId(domicilio.getZona().getId(), p.getId())
                            .orElse(null);

                    if (productoZona != null) {
                        LineaPedido nuevaLinea = LineaPedido.builder()
                                .cantidad(0)
                                .subtotal(0.0f)
                                .productoZona(productoZona)
                                .build();
                        lineasPedidos.add(nuevaLinea);
                    }
                }
            }
        }

        Calendar calendar3 = Calendar.getInstance();
        calendar3.add(Calendar.MINUTE, 30);
        Date fechaActual = calendar3.getTime();
        Venta ventaConsolidada = Venta.builder()
                .fecha(fechaActual)
                .total(total[0])
                .estado("En proceso")
                .domicilio(domicilio)
                .lineasPedido(lineasPedidos)
                .build();

        lineasPedidos.forEach(lp -> lp.setVenta(ventaConsolidada));

        return mapToDTO(ventaConsolidada, populate);
    }
}
