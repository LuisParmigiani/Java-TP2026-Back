package soda_roja.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    public List<ProductoZonaDTOResponse> getAll() {
        return repository.findAll().stream().map(this::mapToDTO).toList();
    }

    public ProductoZonaDTOResponse getById(Long id) {
        return repository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("ProductoZona no encontrado con id: " + id));
    }

    public ProductoZonaDTOResponse save(ProductoZonaDTORequest entidad) {
        Producto producto = productoRepository.findById(entidad.getProductoId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + entidad.getProductoId()));

        Zona zona = zonaRepository.findById(entidad.getZonaId())
                .orElseThrow(() -> new RuntimeException("Zona no encontrada con id: " + entidad.getZonaId()));

        ProductoZona productoZona = ProductoZona.builder()
                .producto(producto)
                .zona(zona)
                .build();

        ProductoZona saved = repository.save(productoZona);
        return mapToDTO(saved);
    }

    public ProductoZonaDTOResponse update(Long id, ProductoZonaDTORequest entidad) {
        Producto producto = productoRepository.findById(entidad.getProductoId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + entidad.getProductoId()));

        Zona zona = zonaRepository.findById(entidad.getZonaId())
                .orElseThrow(() -> new RuntimeException("Zona no encontrada con id: " + entidad.getZonaId()));

        ProductoZona existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("ProductoZona no encontrado con id: " + id));

        existing.setProducto(producto);
        existing.setZona(zona);

        return mapToDTO(repository.save(existing));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public ProductoZonaDTOResponse mapToDTO(ProductoZona productoZona) {
        return ProductoZonaDTOResponse.builder()
                .id(productoZona.getId())
                .producto(productoZona.getProducto() != null ? productoService.mapToDTO(productoZona.getProducto()) : null)
                .zona(productoZona.getZona() != null ? zonaService.mapToDTO(productoZona.getZona()) : null)
                .build();
    }
}
