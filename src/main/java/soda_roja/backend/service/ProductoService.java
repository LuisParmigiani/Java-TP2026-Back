package soda_roja.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import soda_roja.backend.dtoRequest.ProductoDTORequest;
import soda_roja.backend.dtoRequestPut.ProductoDTORequestPut;
import soda_roja.backend.dtoResponse.ProductoDTOResponse;
import soda_roja.backend.repository.ProductoRepository;
import soda_roja.backend.model.Producto;

import java.util.List;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository repository;

    public List<ProductoDTOResponse> getAll() {
        return repository.findAll().stream().map(this::mapToDTO).toList();
    }

    public ProductoDTOResponse getById(Long id) {
        Producto producto = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con id: " + id));
        return mapToDTO(producto);
    }

    public ProductoDTOResponse save(ProductoDTORequest entidad) {
        Producto producto = Producto.builder()
                .nombre(entidad.getNombre())
                .detalle(entidad.getDetalle())
                .precio(entidad.getPrecio())
                .stock(entidad.getStock())
                .build();

        return mapToDTO(repository.save(producto));
    }

        
    public ProductoDTOResponse update(Long id, ProductoDTORequestPut entidad) {
        Producto existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con id: " + id));

        if(entidad.getNombre() != null) {
            existing.setNombre(entidad.getNombre());
        }
        if(entidad.getDetalle() != null) {
            existing.setDetalle(entidad.getDetalle());
        }
        if(entidad.getPrecio() != null) {
            existing.setPrecio(entidad.getPrecio());
        }
        if(entidad.getStock() != null) {
            existing.setStock(entidad.getStock());
        }
        if(entidad.getImagenUrl() != null) {
            existing.setImagenUrl(entidad.getImagenUrl());
        }
        if(entidad.getActivo() != null) {
            existing.setActivo(entidad.getActivo());
        }

        return mapToDTO(repository.save(existing));
    }

    
    public void delete(Long id) {
        Producto producto = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con id: " + id));
        repository.delete(producto);
    }

    public ProductoDTOResponse mapToDTO(Producto producto) {
        return ProductoDTOResponse.builder()
                .id(producto.getId())
                .nombre(producto.getNombre())
                .detalle(producto.getDetalle())
                .precio(producto.getPrecio())
                .stock(producto.getStock())
                .imagenUrl(producto.getImagenUrl())
                .activo(producto.isActivo())
                .build();
    }
}
