package soda_roja.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import soda_roja.backend.dtoRequestPut.ProductoDomicilioDTORequestPut;
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
    @Autowired
    private MapToDTO mapToDTOMapper;

    public List<ProductoDomicilioDTOResponse> getAll(String[] populate) {
        return repository.findAll().stream().map(p -> mapToDTO(p, populate)).toList();
    }

    public ProductoDomicilioDTOResponse getById(Long id,String[] populate) {
        return repository.findById(id)
                .map(p -> mapToDTO(p, populate))
                .orElseThrow(() -> new EntityNotFoundException("ProductoPersonaDomicilio no encontrado con id: " + id));
    }

    public ProductoDomicilioDTOResponse save(ProductoDomicilioDTORequest entidad,String[] populate) {
        Producto producto = productoRepository.findById(entidad.getProductoId())
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con id: " + entidad.getProductoId()));

        Domicilio domicilio = DomicilioRepository.findById(entidad.getDomicilioId())
                .orElseThrow(() -> new EntityNotFoundException("PersonaDomicilio no encontrado con id: " + entidad.getDomicilioId()));

        ProductoDomicilio productoDomicilio = ProductoDomicilio.builder()
                .producto(producto)
                .domicilio(domicilio)
                .cantVaciosActuales(entidad.getCantVaciosActuales())
                .build();

        ProductoDomicilio saved = repository.save(productoDomicilio);
        return mapToDTO(saved, populate);
    }

    public ProductoDomicilioDTOResponse update(Long id, ProductoDomicilioDTORequestPut entidad,String[] populate) {
        ProductoDomicilio existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ProductoPersonaDomicilio no encontrado con id: " + id));

        if(entidad.getProductoId() != null) {
            Producto producto = productoRepository.findById(entidad.getProductoId())
                    .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con id: " + entidad.getProductoId()));
            existing.setProducto(producto);
        }

        if(entidad.getDomicilioId() != null) {
            Domicilio Domicilio = DomicilioRepository.findById(entidad.getDomicilioId())
                    .orElseThrow(() -> new EntityNotFoundException("PersonaDomicilio no encontrado con id: " + entidad.getDomicilioId()));
            existing.setDomicilio(Domicilio);
        }

        if(entidad.getCantVaciosActuales() != null) {
            existing.setCantVaciosActuales(entidad.getCantVaciosActuales());
        }


        return mapToDTO(repository.save(existing), populate);
    }

    public void delete(Long id) {
        ProductoDomicilio existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ProductoPersonaDomicilio no encontrado con id: " + id));
        repository.delete(existing);
    }

    private ProductoDomicilioDTOResponse mapToDTO(ProductoDomicilio productoDomicilio, String[] populate) {
        return mapToDTOMapper.mapToDTO(productoDomicilio, populate);
    }

}
