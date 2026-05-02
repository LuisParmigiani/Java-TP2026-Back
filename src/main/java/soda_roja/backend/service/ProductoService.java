package soda_roja.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.EntityNotFoundException;
import soda_roja.backend.dtoRequest.ProductoDTORequest;
import soda_roja.backend.dtoRequestPut.ProductoDTORequestPut;
import soda_roja.backend.dtoResponse.DomicilioDTOResponse;
import soda_roja.backend.dtoResponse.ProductoDTOResponse;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
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
    @Autowired
    private DomicilioService domicilioService;
    @Autowired
    private CloudinaryService cloudinaryService;

    public List<ProductoDTOResponse> getAll(String[] populate) {
        return repository.findAll().stream().map(p -> mapToDTO(p, populate)).toList();
    }

    public Page<ProductoDTOResponse> getActive(
            String userId, String sortOption, String searchTerm,
            Double minPrice, Double maxPrice, String directionId,
            String[] populate, int page, int size) {

        Sort sort = Sort.unsorted();
        DomicilioDTOResponse domicilio = null;

        if (directionId != null) {
            domicilio = domicilioService.getById(Long.parseLong(directionId), null);
        }

        if (sortOption != null) {
            switch (sortOption) {
                case "Menor Precio" -> sort = Sort.by("precio").ascending();
                case "Mayor Precio" -> sort = Sort.by("precio").descending();
                case "Nombre A-Z" -> sort = Sort.by("nombre").ascending();
                case "Nombre Z-A" -> sort = Sort.by("nombre").descending();
                default -> sort = Sort.by("id").descending();
            }
        } else {
            sort = Sort.by("id").descending(); // siempre ordenar algo
        }

        Pageable pageable = PageRequest.of(page, size, sort);

        if (sortOption == null && searchTerm == null && minPrice == null
                && maxPrice == null && directionId == null && userId == null) {

            return repository.findByActivoTrue(pageable)
                    .map(p -> mapToDTO(p, populate));
        } else {

            String zone = domicilio != null ? domicilio.getZonaId().toString() : null;
            String estado = "activo";
            ProductoSpecification.ProductoFiltrosDTO filtros =
                    new ProductoSpecification.ProductoFiltrosDTO(
                            userId,
                            zone,
                            estado,
                            searchTerm,
                            minPrice,
                            maxPrice
                    );

            return repository.findAll(ProductoSpecification.filtrar(filtros), pageable)
                    .map(p -> mapToDTO(p, populate));
        }
    }

    public ProductoDTOResponse getById(Long id ,String[] populate) {
        Producto producto = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con id: " + id));
        return mapToDTO(producto, populate);
    }

    public ProductoDTOResponse save(ProductoDTORequest entidad, MultipartFile file, String[] populate) {
        Producto producto = Producto.builder()
                .nombre(entidad.getNombre())
                .detalle(entidad.getDetalle())
                .precio(entidad.getPrecio())
                .stock(entidad.getStock())
                .activo(entidad.isActivo())
                .build();

        producto = repository.save(producto);

        if (file != null && !file.isEmpty()) {
            try {
                String imageUrl = cloudinaryService.uploadImage(file, producto.getId());
                producto.setImagenUrl(imageUrl);
                producto = repository.save(producto);
            } catch (Exception e) {
                throw new RuntimeException("Error uploading image: " + e.getMessage());
            }
        }

        return mapToDTO(producto, populate);
    }
        
    public ProductoDTOResponse update(Long id, ProductoDTORequestPut entidad, MultipartFile file, String[] populate) {
        Producto existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con id: " + id));

        if (entidad.getNombre() != null) existing.setNombre(entidad.getNombre());
        if (entidad.getDetalle() != null) existing.setDetalle(entidad.getDetalle());
        if (entidad.getPrecio() != null) existing.setPrecio(entidad.getPrecio());
        if (entidad.getStock() != null) existing.setStock(entidad.getStock());
        if (entidad.getActivo() != null) existing.setActivo(entidad.getActivo());

        if (file != null && !file.isEmpty()) {
            try {
                String imageUrl = cloudinaryService.uploadImage(file, existing.getId());
                existing.setImagenUrl(imageUrl);
            } catch (Exception e) {
                throw new RuntimeException("Error uploading image: " + e.getMessage());
            }
        } else if (entidad.getImagenUrl() != null) {
            existing.setImagenUrl(entidad.getImagenUrl());
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
