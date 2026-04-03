package soda_roja.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import soda_roja.backend.dtoRequest.ProductoDTORequest;
import soda_roja.backend.dtoResponse.DomicilioDTOResponse;
import soda_roja.backend.dtoResponse.ProductoDTOResponse;
import soda_roja.backend.dtoResponse.ZonaDTOResponse;
import soda_roja.backend.model.Domicilio;
import soda_roja.backend.repository.ProductoRepository;
import soda_roja.backend.repository.ZonaRepository;
import soda_roja.backend.model.Producto;
import soda_roja.backend.model.Zona;

import java.util.List;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository repository;
    
    @Autowired
    private ZonaRepository zonaRepository;

    public List<ProductoDTOResponse> getAll() {
        return repository.findAll().stream().map(this::mapToDTO).toList();
    }

    public ProductoDTOResponse getById(long id) {
    	Producto producto = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id));
        return mapToDTO(producto);
    }

    public ProductoDTOResponse save(ProductoDTORequest entidad) {
    	List <Zona> zonas = zonaRepository.findAllById(entidad.getZonasId());

        Producto producto = Producto.builder()
                .nombre(entidad.getNombre())
                .detalle(entidad.getDetalle())
                .precio(entidad.getPrecio())
                .stock(entidad.getStock())
                .zonas(zonas)
                .build();
        
        for (Zona zona : zonas) { //como zona es padre, hay que agregar el producto a la zona para que se guarde la relación en la base de datos
            zona.getProductos().add(producto);
        }


        return mapToDTO(repository.save(producto));
    }

    public ProductoDTOResponse update(long id, ProductoDTORequest entidad) {
        Producto existing = repository.findById(id).orElseThrow();
        
     // Remove producto de las zonas viejas
        for (Zona zona : existing.getZonas()) {
            zona.getProductos().remove(existing);
        }
        
        List <Zona> zonas = zonaRepository.findAllById(entidad.getZonasId());
        
        existing.setNombre(entidad.getNombre());
        existing.setDetalle(entidad.getDetalle());
        existing.setPrecio(entidad.getPrecio());
        existing.setStock(entidad.getStock());
        existing.setZonas(zonas);

        for (Zona zona : zonas) {//agrego producto actualizado a las zonas (padres)
            zona.getProductos().add(existing);
        }
        
        return mapToDTO(repository.save(existing));
    }

    public void delete(long id) {
        Producto producto = repository.findById(id).orElseThrow();
        for (Zona zona : producto.getZonas()) { //borro los productos de las zonas (padre)
            zona.getProductos().remove(producto);
        }
        repository.deleteById(id);
    }
    
    public ProductoDTOResponse mapToDTO(Producto producto) {
        return ProductoDTOResponse.builder()
                .id(producto.getId())
                .nombre(producto.getNombre())
                .detalle(producto.getDetalle())
                .precio(producto.getPrecio())
                .stock(producto.getStock())
                .zonas(producto.getZonas().stream().map(zona -> ZonaDTOResponse.builder()
				.id(zona.getId())
				.nombre(zona.getNombre())
				.detalle(zona.getDetalle())
				.build()).toList())
                .build();
    }
}