package soda_roja.backend.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import soda_roja.backend.dtoRequest.VentaDTORequest;
import soda_roja.backend.dtoRequestPut.VentaDTORequestPut;
import soda_roja.backend.dtoResponse.VentaDTOResponse;
import soda_roja.backend.model.*;
import soda_roja.backend.repository.LineaPedidoRepository;
import soda_roja.backend.repository.DomicilioRepository;
import soda_roja.backend.repository.ProductoZonaRepository;
import soda_roja.backend.repository.VentaRepository;
import soda_roja.backend.specification.VentaSpecification;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

@Service
public class VentaService {

    @Autowired
    private VentaRepository repository;

    @Autowired
    private DomicilioRepository domicilioRepository;

    @Autowired
    private ProductoZonaRepository productoZonaRepository;

    @Autowired
    private LineaPedidoRepository lineaPedidoRepository;

    @Autowired
    private MapToDTO mapToDTOMapper;

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


        Date fechaActual = new Date();
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

}
