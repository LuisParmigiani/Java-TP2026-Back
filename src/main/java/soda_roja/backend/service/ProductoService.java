package soda_roja.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import soda_roja.backend.dtoRequest.ProductoDTORequest;
import soda_roja.backend.dtoRequestPut.ProductoDTORequestPut;
import soda_roja.backend.dtoResponse.DomicilioDTOResponse;
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
    private DomicilioService domicilioService;

    public List<ProductoDTOResponse> getAll() {
        return repository.findAll().stream().map(this::mapToDTO).toList();
    }

    public List<ProductoDTOResponse> getActive(String userId,String sortOption,String searchTerm, Double minPrice, Double maxPrice,String directionId) {
        Sort sort = Sort.unsorted();
        DomicilioDTOResponse domicilio = null;
        System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ ") ;
        System.out.println("User ID: " + directionId);
        if(directionId != null){
            domicilio = domicilioService.getById(Long.parseLong(directionId));
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
            return repository.findByActivoTrue().stream().map(this::mapToDTO).toList();
        }else {
            List<Producto> resultados;
            String estado = "activo";
            String zone = domicilio != null ? domicilio.getZona().getId().toString() : null;
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
                    .map(this::mapToDTO)
                    .toList();
        }
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
                .activo(entidad.isActivo())
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
