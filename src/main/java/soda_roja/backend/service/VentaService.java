package soda_roja.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import soda_roja.backend.dtoRequest.VentaDTORequest;
import soda_roja.backend.dtoRequestPut.VentaDTORequestPut;
import soda_roja.backend.dtoResponse.VentaDTOResponse;
import soda_roja.backend.model.*;
import soda_roja.backend.repository.LineaPedidoRepository;
import soda_roja.backend.repository.DomicilioRepository;
import soda_roja.backend.repository.VentaRepository;

import java.util.List;

@Service
public class VentaService {

    @Autowired
    private VentaRepository repository;

    @Autowired
    private DomicilioRepository domicilioRepository;

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

    public List<VentaDTOResponse> getByUserId(Long userId, String[] populate) {
        return repository.findByDomicilioPersonaUsuarioId(userId).stream()
                .map(v -> mapToDTO(v, populate))
                .toList();
    }

    public VentaDTOResponse save(VentaDTORequest entidad, String[] populate) {
        Domicilio domicilio = findDomicilioOrThrow(entidad.getIdDomicilio());
        List<LineaPedido> lineasPedido = entidad.getLineasPedidoIds().stream()
                .map(this::findLineaPedidoOrThrow)
                .toList();
        if (lineasPedido.isEmpty()) {
			throw new IllegalArgumentException("Una venta debe tener al menos una linea de pedido");
		}

        Venta venta = Venta.builder()
                .fecha(entidad.getFecha())
                .total(entidad.getTotal())
                .estado(entidad.getEstado())
                .pagado(entidad.isPagado())
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
        if(entidad.getPagado() != null) {
            existing.setPagado(entidad.getPagado());
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
