package soda_roja.backend.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import soda_roja.backend.dtoRequestPut.LineaPedidoDTORequestPut;
import soda_roja.backend.model.LineaPedido;
import soda_roja.backend.model.ProductoZona;
import soda_roja.backend.repository.LineaPedidoRepository;
import soda_roja.backend.repository.ProductoZonaRepository;
import soda_roja.backend.repository.VentaRepository;
import soda_roja.backend.dtoRequest.LineaPedidoDTORequest;
import soda_roja.backend.dtoResponse.LineaPedidoDTOResponse;
import soda_roja.backend.model.Venta;

import java.util.List;

@Service
public class LineaPedidoService {

    @Autowired
    private LineaPedidoRepository repository;
    
    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private ProductoZonaRepository productoZonaRepository;

    @Autowired
    private ProductoZonaService productoZonaService;

    public List<LineaPedidoDTOResponse> getAll() {
        return repository.findAll().stream().map(this::mapToDTO).toList();
    }

    public LineaPedidoDTOResponse getById(Long id) {
        return repository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new EntityNotFoundException("LineaPedido no encontrado con id: " + id));
    }

    public LineaPedidoDTOResponse save(LineaPedidoDTORequest entidad) {
        ProductoZona productoZona = findProductoZonaOrThrow(entidad.getProductoZonaId());

        LineaPedido lineaPedido = LineaPedido.builder()
                .cantidad(entidad.getCantidad())
                .subtotal(entidad.getSubtotal())
                .productoZona(productoZona)
                .build();

        LineaPedido saved = repository.save(lineaPedido);
        return mapToDTO(saved);
    }

    public LineaPedidoDTOResponse update(Long id, LineaPedidoDTORequestPut entidad) {
        ProductoZona productoZona = findProductoZonaOrThrow(entidad.getProductoZonaId());

        LineaPedido existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("LineaPedido no encontrado con id: " + id));

        existing.setCantidad(entidad.getCantidad());
        existing.setSubtotal(entidad.getSubtotal());
        existing.setProductoZona(productoZona);

        return mapToDTO(repository.save(existing));
    }

    public void delete(Long id) {
        LineaPedido lineaPedido = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("LineaPedido no encontrado con id: " + id));
        repository.delete(lineaPedido);
    }
    private Venta findVentaOrThrow(Long id) {
    			return ventaRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Venta no encontrada con id: " + id));
	}
    private ProductoZona findProductoZonaOrThrow(Long id) {
        return productoZonaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ProductoZona no encontrado con id: " + id));
    }

    public LineaPedidoDTOResponse mapToDTO(LineaPedido lineaPedido) {
        return LineaPedidoDTOResponse.builder()
                .id(lineaPedido.getId())
                .cantidad(lineaPedido.getCantidad())
                .subtotal(lineaPedido.getSubtotal())
                .productoZona(lineaPedido.getProductoZona() != null ? productoZonaService.mapToDTO(lineaPedido.getProductoZona()) : null)
                .build();
    }
}
