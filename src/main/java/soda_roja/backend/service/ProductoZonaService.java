package soda_roja.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import soda_roja.backend.dtoRequestPut.ProductoZonaDTORequestPut;
import soda_roja.backend.model.ProductoZona;
import soda_roja.backend.model.Producto;
import soda_roja.backend.model.Zona;
import soda_roja.backend.repository.ProductoZonaRepository;
import soda_roja.backend.repository.ProductoRepository;
import soda_roja.backend.repository.ZonaRepository;
import soda_roja.backend.dtoRequest.ProductoZonaDTORequest;
import soda_roja.backend.dtoResponse.ProductoZonaDTOResponse;

import java.util.List;

@Service
public class ProductoZonaService {

    @Autowired
    private ProductoZonaRepository repository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ZonaRepository zonaRepository;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private ZonaService zonaService;

    @Autowired
    private PedidoSemanalService pedidoSemanalService;

    public List<ProductoZonaDTOResponse> getAll() {
        return repository.findAll().stream().map(this::mapToDTO).toList();
    }

    public ProductoZonaDTOResponse getById(Long id) {
        return repository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new EntityNotFoundException("ProductoZona no encontrado con id: " + id));
    }

    public ProductoZonaDTOResponse save(ProductoZonaDTORequest entidad) {
        Producto producto = findProductoOrThrow(entidad.getProductoId());
        Zona zona = findZonaOrThrow(entidad.getZonaId());

        ProductoZona productoZona = ProductoZona.builder()
                .producto(producto)
                .zona(zona)
                .build();

        ProductoZona saved = repository.save(productoZona);
        return mapToDTO(saved);
    }

    public ProductoZonaDTOResponse update(Long id, ProductoZonaDTORequestPut entidad) {
        ProductoZona existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ProductoZona no encontrado con id: " + id));

        if(entidad.getProductoId() != null) {
            Producto producto = findProductoOrThrow(entidad.getProductoId());
            existing.setProducto(producto);
        }

        if(entidad.getZonaId() != null) {
            Zona zona = findZonaOrThrow(entidad.getZonaId());
            existing.setZona(zona);
        }

        return mapToDTO(repository.save(existing));
    }

    public void delete(Long id) {
        ProductoZona productoZona = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ProductoZona no encontrado con id: " + id));
        repository.delete(productoZona);
    }

    private Producto findProductoOrThrow(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con id: " + id));
    }

    private Zona findZonaOrThrow(Long id) {
        return zonaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Zona no encontrada con id: " + id));
    }

    public ProductoZonaDTOResponse mapToDTO(ProductoZona productoZona) {
        return ProductoZonaDTOResponse.builder()
                .id(productoZona.getId())
                .producto(productoZona.getProducto() != null ? productoService.mapToDTO(productoZona.getProducto()) : null)
                .zona(productoZona.getZona() != null ? zonaService.mapToDTO(productoZona.getZona()) : null)
                .pedidoSemanal(productoZona.getPedidoSemanales() != null ? productoZona.getPedidoSemanales().stream().map(pedidoSemanalService::mapToDTO).toList() : null)
                .build();
    }
}
