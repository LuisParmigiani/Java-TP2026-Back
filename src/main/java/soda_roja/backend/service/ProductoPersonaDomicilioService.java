package soda_roja.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import soda_roja.backend.model.ProductoPersonaDomicilio;
import soda_roja.backend.model.Producto;
import soda_roja.backend.model.PersonaDomicilio;
import soda_roja.backend.repository.ProductoPersonaDomicilioRepository;
import soda_roja.backend.repository.ProductoRepository;
import soda_roja.backend.repository.PersonaDomicilioRepository;
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
    private PersonaDomicilioRepository personaDomicilioRepository;

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

        PersonaDomicilio personaDomicilio = personaDomicilioRepository.findById(entidad.getPersonaDomicilioId())
                .orElseThrow(() -> new RuntimeException("PersonaDomicilio no encontrado con id: " + entidad.getPersonaDomicilioId()));

        ProductoPersonaDomicilio productoPersonaDomicilio = ProductoPersonaDomicilio.builder()
                .producto(producto)
                .personaDomicilio(personaDomicilio)
                .cantVaciosActuales(entidad.getCantVaciosActuales())
                .aproxSemanal(entidad.getAproxSemanal())
                .build();

        ProductoPersonaDomicilio saved = repository.save(productoPersonaDomicilio);
        return mapToDTO(saved);
    }

    public ProductoPersonaDomicilioDTOResponse update(Long id, ProductoPersonaDomicilioDTORequest entidad) {
        Producto producto = productoRepository.findById(entidad.getProductoId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + entidad.getProductoId()));

        PersonaDomicilio personaDomicilio = personaDomicilioRepository.findById(entidad.getPersonaDomicilioId())
                .orElseThrow(() -> new RuntimeException("PersonaDomicilio no encontrado con id: " + entidad.getPersonaDomicilioId()));

        ProductoPersonaDomicilio existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("ProductoPersonaDomicilio no encontrado con id: " + id));

        existing.setProducto(producto);
        existing.setPersonaDomicilio(personaDomicilio);
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
                .personaDomicilioId(productoPersonaDomicilio.getPersonaDomicilio() != null ? productoPersonaDomicilio.getPersonaDomicilio().getId() : null)
                .cantVaciosActuales(productoPersonaDomicilio.getCantVaciosActuales())
                .aproxSemanal(productoPersonaDomicilio.getAproxSemanal())
                .build();
    }
}
