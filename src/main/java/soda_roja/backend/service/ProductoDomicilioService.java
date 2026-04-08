package soda_roja.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import soda_roja.backend.model.Domicilio;
import soda_roja.backend.model.ProductoDomicilio;
import soda_roja.backend.model.Producto;
import soda_roja.backend.repository.DomicilioRepository;
import soda_roja.backend.repository.ProductoDomicilioRepository;
import soda_roja.backend.repository.ProductoRepository;
import soda_roja.backend.dtoRequest.ProductoDomicilioDTORequest;
import soda_roja.backend.dtoResponse.ProductoDomicilioDTOResponse;

import java.util.List;

@Service
public class ProductoDomicilioService {

    @Autowired
    private ProductoDomicilioRepository repository;

    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private DomicilioRepository DomicilioRepository;


    public List<ProductoDomicilioDTOResponse> getAll() {
        return repository.findAll().stream().map(this::mapToDTO).toList();
    }

    public ProductoDomicilioDTOResponse getById(Long id) {
        return repository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new EntityNotFoundException("ProductoPersonaDomicilio no encontrado con id: " + id));
    }

    public ProductoDomicilioDTOResponse save(ProductoDomicilioDTORequest entidad) {
        Producto producto = productoRepository.findById(entidad.getProductoId())
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con id: " + entidad.getProductoId()));

        Domicilio domicilio = DomicilioRepository.findById(entidad.getDomicilioId())
                .orElseThrow(() -> new EntityNotFoundException("PersonaDomicilio no encontrado con id: " + entidad.getDomicilioId()));

        ProductoDomicilio productoDomicilio = ProductoDomicilio.builder()
                .producto(producto)
                .domicilio(domicilio)
                .cantVaciosActuales(entidad.getCantVaciosActuales())
                .aproxSemanal(entidad.getAproxSemanal())
                .build();

        ProductoDomicilio saved = repository.save(productoDomicilio);
        return mapToDTO(saved);
    }

    public ProductoDomicilioDTOResponse update(Long id, ProductoDomicilioDTORequest entidad) {
        Producto producto = productoRepository.findById(entidad.getProductoId())
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con id: " + entidad.getProductoId()));

        Domicilio Domicilio = DomicilioRepository.findById(entidad.getDomicilioId())
                .orElseThrow(() -> new EntityNotFoundException("PersonaDomicilio no encontrado con id: " + entidad.getDomicilioId()));

        ProductoDomicilio existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ProductoPersonaDomicilio no encontrado con id: " + id));

        existing.setProducto(producto);
        existing.setDomicilio(Domicilio);
        existing.setCantVaciosActuales(entidad.getCantVaciosActuales());
        existing.setAproxSemanal(entidad.getAproxSemanal());

        return mapToDTO(repository.save(existing));
    }

    public void delete(Long id) {
        ProductoDomicilio existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ProductoPersonaDomicilio no encontrado con id: " + id));
        repository.delete(existing);
    }

    public ProductoDomicilioDTOResponse mapToDTO(ProductoDomicilio productoDomicilio) {
        return ProductoDomicilioDTOResponse.builder()
                .id(productoDomicilio.getId())
                .productoId(productoDomicilio.getProducto() != null ? productoDomicilio.getProducto().getId() : null)
                .nombreProducto(productoDomicilio.getProducto() != null ? productoDomicilio.getProducto().getNombre() : null)
                .DomicilioId(productoDomicilio.getDomicilio() != null ? productoDomicilio.getDomicilio().getId() : null)
                .cantVaciosActuales(productoDomicilio.getCantVaciosActuales())
                .aproxSemanal(productoDomicilio.getAproxSemanal())
                .build();
    }
}
