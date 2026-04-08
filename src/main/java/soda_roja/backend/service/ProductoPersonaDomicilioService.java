package soda_roja.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import soda_roja.backend.model.Domicilio;
import soda_roja.backend.model.ProductoPersonaDomicilio;
import soda_roja.backend.model.Producto;
import soda_roja.backend.repository.DomicilioRepository;
import soda_roja.backend.repository.ProductoPersonaDomicilioRepository;
import soda_roja.backend.repository.ProductoRepository;
import soda_roja.backend.dtoRequest.ProductoPersonaDomicilioDTORequest;
import soda_roja.backend.dtoResponse.ProductoPersonaDomicilioDTOResponse;

import java.util.List;

@Service
public class ProductoPersonaDomicilioService {

    @Autowired
    private ProductoPersonaDomicilioRepository repository;

    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private DomicilioRepository DomicilioRepository;


    public List<ProductoPersonaDomicilioDTOResponse> getAll() {
        return repository.findAll().stream().map(this::mapToDTO).toList();
    }

    public ProductoPersonaDomicilioDTOResponse getById(Long id) {
        return repository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("ProductoPersonaDomicilio no encontrado con id: " + id));
    }

    public ProductoPersonaDomicilioDTOResponse save(ProductoPersonaDomicilioDTORequest entidad) {
        Producto producto = productoRepository.findById(entidad.getProductoId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + entidad.getProductoId()));

        Domicilio Domicilio = DomicilioRepository.findById(entidad.getDomicilioId())
                .orElseThrow(() -> new RuntimeException("PersonaDomicilio no encontrado con id: " + entidad.getDomicilioId()));

        ProductoPersonaDomicilio productoPersonaDomicilio = ProductoPersonaDomicilio.builder()
                .producto(producto)
                .domicilio(Domicilio)
                .cantVaciosActuales(entidad.getCantVaciosActuales())
                .aproxSemanal(entidad.getAproxSemanal())
                .build();

        ProductoPersonaDomicilio saved = repository.save(productoPersonaDomicilio);
        return mapToDTO(saved);
    }

    public ProductoPersonaDomicilioDTOResponse update(Long id, ProductoPersonaDomicilioDTORequest entidad) {
        Producto producto = productoRepository.findById(entidad.getProductoId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + entidad.getProductoId()));

        Domicilio Domicilio = DomicilioRepository.findById(entidad.getDomicilioId())
                .orElseThrow(() -> new RuntimeException("PersonaDomicilio no encontrado con id: " + entidad.getDomicilioId()));

        ProductoPersonaDomicilio existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("ProductoPersonaDomicilio no encontrado con id: " + id));

        existing.setProducto(producto);
        existing.setDomicilio(Domicilio);
        existing.setCantVaciosActuales(entidad.getCantVaciosActuales());
        existing.setAproxSemanal(entidad.getAproxSemanal());

        return mapToDTO(repository.save(existing));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public ProductoPersonaDomicilioDTOResponse mapToDTO(ProductoPersonaDomicilio productoPersonaDomicilio) {
        return ProductoPersonaDomicilioDTOResponse.builder()
                .id(productoPersonaDomicilio.getId())
                .productoId(productoPersonaDomicilio.getProducto() != null ? productoPersonaDomicilio.getProducto().getId() : null)
                .nombreProducto(productoPersonaDomicilio.getProducto() != null ? productoPersonaDomicilio.getProducto().getNombre() : null)
                .DomicilioId(productoPersonaDomicilio.getDomicilio() != null ? productoPersonaDomicilio.getDomicilio().getId() : null)
                .cantVaciosActuales(productoPersonaDomicilio.getCantVaciosActuales())
                .aproxSemanal(productoPersonaDomicilio.getAproxSemanal())
                .build();
    }
}
