package soda_roja.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import soda_roja.backend.dtoRequest.VentaDTORequest;
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
    private LineaPedidoService lineaPedidoService;

    public List<VentaDTOResponse> getAll() {
        return repository.findAll().stream().map(this::mapToDTO).toList();
    }

    public VentaDTOResponse getById(Long id) {
        return repository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new EntityNotFoundException("Venta no encontrada con id: " + id));
    }

    public VentaDTOResponse save(VentaDTORequest entidad) {
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
                .pagado(entidad.isPagado())
                .domicilio(domicilio)
                .lineasPedido(lineasPedido)
                .build();

        lineasPedido.forEach(lp -> lp.setVenta(venta));
        return mapToDTO(repository.save(venta));
    }

    public VentaDTOResponse update(Long id, VentaDTORequest entidad) {
        Venta existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Venta no encontrada con id: " + id));

        Domicilio domicilio = findDomicilioOrThrow(entidad.getIdDomicilio());
        List<LineaPedido> lineasPedido = entidad.getLineasPedidoIds().stream()
                .map(this::findLineaPedidoOrThrow)
                .toList();
        
        if (lineasPedido.isEmpty()) {
			throw new IllegalArgumentException("Una venta debe tener al menos una linea de pedido");
		}


        existing.setTotal(entidad.getTotal());
        existing.setFecha(entidad.getFecha());
        existing.setDomicilio(domicilio);
        existing.setPagado(entidad.isPagado());
        lineasPedido.forEach(lp -> lp.setVenta(existing));

        return mapToDTO(repository.save(existing));
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

    public VentaDTOResponse mapToDTO(Venta venta) {
        return VentaDTOResponse.builder()
                .id(venta.getId())
                .fecha(venta.getFecha())
                .total(venta.getTotal())
                .pagado(venta.isPagado())
                .idDomicilio(venta.getDomicilio().getId())
                .lineasPedido(venta.getLineasPedido().stream().map(lineaPedidoService::mapToDTO).toList())
                .build();
    }
}
