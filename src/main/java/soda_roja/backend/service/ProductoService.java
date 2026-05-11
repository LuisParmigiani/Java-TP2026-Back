package soda_roja.backend.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import soda_roja.backend.dtoRequest.ProductoDTORequest;
import soda_roja.backend.dtoRequestPut.ProductoDTORequestPut;
import soda_roja.backend.dtoResponse.DomicilioDTOResponse;
import soda_roja.backend.dtoResponse.ProductoDTOResponse;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import soda_roja.backend.model.Zona;
import soda_roja.backend.model.ProductoZona;
import soda_roja.backend.repository.ProductoRepository;
import soda_roja.backend.repository.ProductoZonaRepository;
import soda_roja.backend.repository.ZonaRepository;
import soda_roja.backend.model.Producto;
import soda_roja.backend.specification.ProductoSpecification;

import java.util.List;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository repository;
    @Autowired
    private ProductoZonaRepository pZonarepository;
    @Autowired
    private ZonaRepository zonaRepository;
    @Autowired
    private MapToDTO mapToDTOMapper;
    @Autowired
    private DomicilioService domicilioService;

    @Autowired
    private CloudinaryService cloudinaryService;

    public List<ProductoDTOResponse> getAll(String[] populate) {
        return repository.findAll().stream().map(p -> mapToDTO(p, populate)).toList();
    }

    public List<ProductoDTOResponse> getActiveSinPag(
            String[] populate) {
            return repository.findByActivoTrue().stream()
                    .map(p -> mapToDTO(p, populate)).toList();
    }
    
    public Page<ProductoDTOResponse> getActive(
            String userId, String sortOption, String searchTerm,
            Double minPrice, Double maxPrice, String directionId,
            String[] populate, int page, int size) {
    	System.out.println("getActive called with parameters:");
    			System.out.println("userId: " + userId);
    			System.out.println("sortOption: " + sortOption);
    			System.out.println("searchTerm: " + searchTerm);
    			System.out.println("minPrice: " + minPrice);
    			System.out.println("maxPrice: " + maxPrice);
    			System.out.println("directionId: " + directionId);
    			
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
            System.out.println("Filtering with parameters:");
            System.out.println("userId: " + userId);
            System.out.println("zone: " + zone);
            System.out.println("estado: " + estado);
            System.out.println("searchTerm: " + searchTerm);
            System.out.println("minPrice: " + minPrice);
            System.out.println("maxPrice: " + maxPrice);
            	
            
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
    @Transactional
    public ProductoDTOResponse save(ProductoDTORequest entidad, MultipartFile file, String[] populate) {
    	List<Zona> zonas = zonaRepository.findAll().stream()
				.toList();
    	
        Producto producto = Producto.builder()
                .nombre(entidad.getNombre())
                .detalle(entidad.getDetalle())
                .precio(entidad.getPrecio())
                .stock(entidad.getStock())
                .activo(entidad.isActivo())
                .build();

       
        
    
        
        if (file != null && !file.isEmpty()) {
            try {
                String imageUrl = cloudinaryService.uploadImage(file, "producto",producto.getId());
                producto.setImagenUrl(imageUrl);
            } catch (Exception e) {
                throw new RuntimeException("Error uploading image: " + e.getMessage());
            }
        }

            Producto productoResponse = repository.save(producto);
        	zonas.forEach(z -> {
        	ProductoZona pz = new ProductoZona();
			pz.setProducto(productoResponse);
			pz.setZona(z);
			
			pZonarepository.save(pz);
		});
        	
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
                String imageUrl = cloudinaryService.uploadImage(file, "producto" ,existing.getId());
                existing.setImagenUrl(imageUrl);
            } catch (Exception e) {
                throw new RuntimeException("Error uploading image: " + e.getMessage());
            }
        } else if (entidad.getImagenUrl() != null) {
            existing.setImagenUrl(entidad.getImagenUrl());
        }

        return mapToDTO(repository.save(existing), populate);
    }
    public List<ProductoDTOResponse> getByZona(Long zonaId, String[] populate) {

        return repository.findByZona(zonaId).stream().map(p -> mapToDTO(p, populate)).toList();
    }

    
    public void delete(Long id) {
        Producto producto = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con id: " + id));

        // Delete image from Cloudinary if exists
        String imagenUrl = producto.getImagenUrl();
        if (imagenUrl != null && !imagenUrl.isEmpty()) {
            try {
                // Extract publicId from the URL (adjust extraction as needed)
            	String publicId = imagenUrl.substring(
            		    imagenUrl.indexOf("/", imagenUrl.indexOf("/upload/") + 8) + 1, 
            		    imagenUrl.lastIndexOf(".")
            		);
                cloudinaryService.deleteImage(publicId);
            } catch (Exception e) {
                // Log or handle the error as needed
                throw new RuntimeException("Error deleting image: " + e.getMessage());
            }
        }

        repository.delete(producto);
    }

    private ProductoDTOResponse mapToDTO(Producto producto, String[] populate) {
        return mapToDTOMapper.mapToDTO(producto, populate);
    }

}
