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
import soda_roja.backend.dtoResponse.CargaDTOResponse;
import soda_roja.backend.dtoResponse.VentaDTOResponse;
import soda_roja.backend.model.*;
import soda_roja.backend.repository.*;
import soda_roja.backend.specification.VentaSpecification;

import java.text.SimpleDateFormat;
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
    private CargaRepository cargaRepository;

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
    @Autowired
    private MailService mailService;

    public List<VentaDTOResponse> getAll(String[] populate) {
        return repository.findAll().stream().map(v -> mapToDTO(v, populate)).toList();
    }

    public Page<VentaDTOResponse> getPendientes(String[] populate, String zona, String ordenBy, Integer page, Integer size) {
        Sort sort = Sort.by("id").descending();
        if (ordenBy != null) {
            switch (ordenBy) {
                case "Mas Recientes" -> sort = Sort.by("fecha").descending();
                case "Mas Antiguos" -> sort = Sort.by("fecha").ascending();
                case "Menor Precio" -> sort = Sort.by("total").ascending();
                case "Mayor Precio" -> sort = Sort.by("total").descending();
                default -> sort = Sort.by("id").descending();
            }
        }
        Pageable pageable = PageRequest.of(page != null ? page : 0, size != null ? size : 10, sort);

        VentaSpecification.VentaFiltrosDTO filtros = new VentaSpecification.VentaFiltrosDTO("Pendientes", null, zona);

        return repository.findAll(VentaSpecification.filtrar(filtros), pageable)
                .map(v -> mapToDTO(v, populate));
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
        VentaSpecification.VentaFiltrosDTO filtros = new VentaSpecification.VentaFiltrosDTO(state, userId, null);

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
        Venta saved = repository.save(venta);
        enviarMailVentaNueva(saved);
        return mapToDTO(saved, populate);
    }


    @Transactional
    public VentaDTOResponse saveByDriver(VentaDTORequest entidad, List<ProductoDomicilioDTORequest> productoDomicilio, String monto, String[] populate,String driverId) {
    	Carga carga = cargaRepository.findByUsuarioIdAndFechaHoraBetween(Long.parseLong(driverId), getStartOfDay(), getEndOfDay())
				.stream()
				.findFirst()
				.orElseThrow(() -> new EntityNotFoundException("No se encontró una carga para el driver con id: " + driverId + " en el día de hoy"));
    	
    	List<CargaProducto> cargaProductos = carga.getCargasProducto();
    	
    	
    	
        // Se restan todos los retornables que se devolvieron.
        productoDomicilio.forEach(pd -> {
        	
            Domicilio domicilioEntity = domicilioRepository.findById(pd.getDomicilioId())
                    .orElseThrow(() -> new EntityNotFoundException("Domicilio no encontrado con id: " + pd.getDomicilioId()));

            ProductoDomicilio productoDomicilioEntity = domicilioEntity.getProductoDomicilio().stream()
                    .filter(p -> p.getProducto().getId().equals(pd.getProductoId()))
                    .findFirst()
                    .orElseThrow(() -> new EntityNotFoundException("ProductoDomicilio no encontrado para el productoId: " + pd.getProductoId() + " y domicilioId: " + pd.getDomicilioId()));
            
            Optional<CargaProducto> cargaProducto = cargaProductos.stream()
					.filter(cp -> cp.getProducto().getId().equals(pd.getProductoId()))
					.findFirst();
        		if(cargaProducto.isPresent()) {
        			CargaProducto cp = cargaProducto.get();
        			cp.setCantDevueltos(cp.getCantDevueltos() + pd.getCantVaciosActuales());
        			

        			}else {
        				CargaProducto nuevoCargaProducto = new CargaProducto().builder()
								.cantLleno(0)
								.cantVacio(0)
								.cantDevueltos(pd.getCantVaciosActuales())
								.carga(carga)
								.producto(productoRepository.findById(pd.getProductoId())
										.orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con id: " + pd.getProductoId())))
								.build();
        				
        			}
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
            	Optional<CargaProducto> cargaProducto = cargaProductos.stream()
						.filter(cp -> cp.getProducto().getId().equals(lp.getProductoZonaId()))
						.findFirst();
            		if(cargaProducto.isPresent()) {
            			CargaProducto cp = cargaProducto.get();
            			cp.setCantVendidos(cp.getCantVendidos() + lp.getCantidad());
            			

            			}else {
            				CargaProducto nuevoCargaProducto = new CargaProducto().builder()
									.cantLleno(0)
									.cantVacio(0)
									.cantVendidos(lp.getCantidad())
									.carga(carga)
									.producto(productoRepository.findById(lp.getProductoZonaId())
											.orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con id: " + lp.getProductoZonaId())))
									.build();
            				
            			}
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
        if(monto != null &&!monto.isEmpty()) {
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

        enviarMailVentaCompletada(savedVenta);
        if (monto != null && !monto.isEmpty()) {
            enviarMailPago(domicilio.getPersona(), Float.parseFloat(monto), "Efectivo", new Date(), domicilio.getPersona().getSaldo());
        }

        return mapToDTO(savedVenta, populate);
    }

    public VentaDTOResponse update(Long id, VentaDTORequestPut entidad, String[] populate) {
        Venta existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Venta no encontrada con id: " + id));

        if (entidad.getIdDomicilio() != null) existing.setDomicilio(findDomicilioOrThrow(entidad.getIdDomicilio()));
        if (entidad.getLineasPedidoIds() != null) {
            List<LineaPedido> lineasPedido = entidad.getLineasPedidoIds().stream()
                    .map(this::findLineaPedidoOrThrow)
                    .toList();
            if (lineasPedido.isEmpty()) throw new IllegalArgumentException("Una venta debe tener al menos una linea de pedido");
            lineasPedido.forEach(lp -> lp.setVenta(existing));
        }
        if (entidad.getTotal() != null) existing.setTotal(entidad.getTotal());
        if (entidad.getFecha() != null) existing.setFecha(entidad.getFecha());

        String estadoAnterior = existing.getEstado();
        if (entidad.getEstado() != null) existing.setEstado(entidad.getEstado());

        Venta saved = repository.save(existing);

        if (entidad.getEstado() != null && !entidad.getEstado().equalsIgnoreCase(estadoAnterior)) {
            if (entidad.getEstado().equalsIgnoreCase("Completada")) {
                enviarMailVentaCompletada(saved);
            } else if (entidad.getEstado().equalsIgnoreCase("Cancelada")) {
                enviarMailVentaCancelada(saved);
            }
        }

        return mapToDTO(saved, populate);
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

    private void enviarMailVentaNueva(Venta venta) {
        Persona persona = venta.getDomicilio().getPersona();
        String fecha = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(venta.getFecha());

        StringBuilder filas = new StringBuilder();
        venta.getLineasPedido().forEach(lp -> filas.append(
            "<tr>" +
            "<td style='border:1px solid #ddd;padding:8px;'>" + lp.getProductoZona().getProducto().getNombre() + "</td>" +
            "<td style='border:1px solid #ddd;padding:8px;text-align:center;'>" + lp.getCantidad() + "</td>" +
            "<td style='border:1px solid #ddd;padding:8px;text-align:right;'>$" + String.format("%.2f", lp.getSubtotal()) + "</td>" +
            "</tr>"
        ));

        String cuerpo = "<h1>¡Recibimos tu pedido!</h1>" +
            "<p>Hola <b>" + persona.getNombre() + " " + persona.getApellido() + "</b>, tu pedido fue registrado y está pendiente de entrega.</p>" +
            "<br><h3>Detalle del pedido #" + venta.getId() + "</h3>" +
            "<table style='border-collapse:collapse;width:100%;'>" +
            "<thead><tr>" +
            "<th style='border:1px solid #ddd;padding:8px;text-align:left;'>Producto</th>" +
            "<th style='border:1px solid #ddd;padding:8px;text-align:center;'>Cantidad</th>" +
            "<th style='border:1px solid #ddd;padding:8px;text-align:right;'>Subtotal</th>" +
            "</tr></thead><tbody>" + filas + "</tbody></table>" +
            "<br><p><b>Total: $" + String.format("%.2f", venta.getTotal()) + "</b></p>" +
            "<p>Fecha estimada: <b>" + fecha + "</b></p>";

        mailService.enviarMail(persona.getEmail(), "Recibimos tu pedido - Soda Roja", cuerpo);
    }

    private void enviarMailVentaCompletada(Venta venta) {
        Persona persona = venta.getDomicilio().getPersona();
        String fecha = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(venta.getFecha());

        StringBuilder filas = new StringBuilder();
        venta.getLineasPedido().forEach(lp -> filas.append(
            "<tr>" +
            "<td style='border:1px solid #ddd;padding:8px;'>" + lp.getProductoZona().getProducto().getNombre() + "</td>" +
            "<td style='border:1px solid #ddd;padding:8px;text-align:center;'>" + lp.getCantidad() + "</td>" +
            "<td style='border:1px solid #ddd;padding:8px;text-align:right;'>$" + String.format("%.2f", lp.getSubtotal()) + "</td>" +
            "</tr>"
        ));

        String cuerpo = "<h1>¡Tu pedido fue completado!</h1>" +
            "<p>Hola <b>" + persona.getNombre() + " " + persona.getApellido() + "</b>, te confirmamos que tu pedido fue procesado exitosamente.</p>" +
            "<br><h3>Detalle del pedido #" + venta.getId() + "</h3>" +
            "<table style='border-collapse:collapse;width:100%;'>" +
            "<thead><tr>" +
            "<th style='border:1px solid #ddd;padding:8px;text-align:left;'>Producto</th>" +
            "<th style='border:1px solid #ddd;padding:8px;text-align:center;'>Cantidad</th>" +
            "<th style='border:1px solid #ddd;padding:8px;text-align:right;'>Subtotal</th>" +
            "</tr></thead><tbody>" + filas + "</tbody></table>" +
            "<br><p><b>Total: $" + String.format("%.2f", venta.getTotal()) + "</b></p>" +
            "<p>Fecha: <b>" + fecha + "</b></p>";

        mailService.enviarMail(persona.getEmail(), "Tu pedido fue completado - Soda Roja", cuerpo);
    }

    private void enviarMailVentaCancelada(Venta venta) {
        Persona persona = venta.getDomicilio().getPersona();
        String fecha = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(venta.getFecha());

        StringBuilder filas = new StringBuilder();
        venta.getLineasPedido().forEach(lp -> filas.append(
            "<tr>" +
            "<td style='border:1px solid #ddd;padding:8px;'>" + lp.getProductoZona().getProducto().getNombre() + "</td>" +
            "<td style='border:1px solid #ddd;padding:8px;text-align:center;'>" + lp.getCantidad() + "</td>" +
            "<td style='border:1px solid #ddd;padding:8px;text-align:right;'>$" + String.format("%.2f", lp.getSubtotal()) + "</td>" +
            "</tr>"
        ));

        String cuerpo = "<h1>Tu pedido fue cancelado</h1>" +
            "<p>Hola <b>" + persona.getNombre() + " " + persona.getApellido() + "</b>, te informamos que tu pedido fue cancelado.</p>" +
            "<br><h3>Detalle del pedido #" + venta.getId() + "</h3>" +
            "<table style='border-collapse:collapse;width:100%;'>" +
            "<thead><tr>" +
            "<th style='border:1px solid #ddd;padding:8px;text-align:left;'>Producto</th>" +
            "<th style='border:1px solid #ddd;padding:8px;text-align:center;'>Cantidad</th>" +
            "<th style='border:1px solid #ddd;padding:8px;text-align:right;'>Subtotal</th>" +
            "</tr></thead><tbody>" + filas + "</tbody></table>" +
            "<br><p><b>Total: $" + String.format("%.2f", venta.getTotal()) + "</b></p>" +
            "<p>Fecha: <b>" + fecha + "</b></p>" +
            "<br><p>Si tenés alguna consulta, no dudes en contactarnos.</p>";

        mailService.enviarMail(persona.getEmail(), "Tu pedido fue cancelado - Soda Roja", cuerpo);
    }

    void enviarMailPago(Persona persona, float monto, String metodoPago, Date fecha, float saldoActual) {
        String fechaStr = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(fecha);
        String cuerpo = "<h1>Registro de pago</h1>" +
            "<p>Hola <b>" + persona.getNombre() + " " + persona.getApellido() + "</b>, te confirmamos que se registró un pago en tu cuenta.</p>" +
            "<br>" +
            "<p>Monto abonado: <b>$" + String.format("%.2f", monto) + "</b></p>" +
            "<p>Método de pago: <b>" + metodoPago + "</b></p>" +
            "<p>Fecha: <b>" + fechaStr + "</b></p>" +
            "<br>" +
            "<p>Saldo actual: <b>$" + String.format("%.2f", saldoActual) + "</b></p>";

        mailService.enviarMail(persona.getEmail(), "Registro de pago - Soda Roja", cuerpo);
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
    private Date getStartOfDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    private Date getEndOfDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

}
