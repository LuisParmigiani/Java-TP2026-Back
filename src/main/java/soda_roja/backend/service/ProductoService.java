package soda_roja.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import soda_roja.backend.dtoRequest.ProductoDTORequest;
import soda_roja.backend.dtoRequestPut.ProductoDTORequestPut;
import soda_roja.backend.dtoResponse.ProductoDTOResponse;
import org.springframework.data.domain.Sort;
import soda_roja.backend.repository.ProductoRepository;
import soda_roja.backend.model.Producto;
import soda_roja.backend.specification.ProductoSpecification;

import java.util.List;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository repository;
    @Autowired
    private MapToDTO mapToDTOMapper;

    public List<ProductoDTOResponse> getAll(String[] populate) {
        return repository.findAll().stream().map(p -> mapToDTO(p, populate)).toList();
    }

    public List<ProductoDTOResponse> getActive(String userId,String sortOption,String searchTerm, Double minPrice, Double maxPrice,String directionId,String[] populate) {
        Sort sort = Sort.unsorted();

        if(directionId != null){
            // Get domicilio by id for zone filtering
        }
        if (sortOption != null) {
            switch (sortOption) {
                case "Menor Precio" -> sort = Sort.by("precio").ascending();
                case "Mayor Precio" -> sort = Sort.by("precio").descending();
                case "Nombre A-Z" -> sort = Sort.by("nombre").ascending();
                case "Nombre Z-A" -> sort = Sort.by("nombre").descending();
                default -> sort = Sort.by("id").descending(); // Orden por defecto
            }
        }

        if(sortOption == null && searchTerm == null && minPrice == null && maxPrice == null && directionId == null && userId == null) {
            return repository.findByActivoTrue().stream().map(p -> mapToDTO(p, populate)).toList();
        }else {
            List<Producto> resultados;
            String estado = "activo";
            String zone = null;
            ProductoSpecification.ProductoFiltrosDTO filtros =
                    new ProductoSpecification.ProductoFiltrosDTO(
                            userId,
                            zone,
                            estado,
                            searchTerm,
                            minPrice,
                            maxPrice
                    );

            resultados = repository.findAll(ProductoSpecification.filtrar(filtros), sort);
            return resultados.stream()
                    .map(p -> mapToDTO(p, populate))
                    .toList();
        }
    }

    public ProductoDTOResponse getById(Long id ,String[] populate) {
        Producto producto = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con id: " + id));
        return mapToDTO(producto, populate);
    }

    public ProductoDTOResponse save(ProductoDTORequest entidad ,String[] populate) {
        Producto producto = Producto.builder()
                .nombre(entidad.getNombre())
                .detalle(entidad.getDetalle())
                .precio(entidad.getPrecio())
                .stock(entidad.getStock())
                .activo(entidad.isActivo())
                .build();

        return mapToDTO(repository.save(producto), populate);
    }

        
    public ProductoDTOResponse update(Long id, ProductoDTORequestPut entidad ,String[] populate) {
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

        return mapToDTO(repository.save(existing), populate);
    }

    
    public void delete(Long id) {
        Producto producto = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con id: " + id));
        repository.delete(producto);
    }

    private ProductoDTOResponse mapToDTO(Producto producto, String[] populate) {
        return mapToDTOMapper.mapToDTO(producto, populate);
    }

}
