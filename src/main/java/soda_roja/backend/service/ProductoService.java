package soda_roja.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import soda_roja.backend.dtoResponse.DomicilioDTOResponse;
import soda_roja.backend.dtoResponse.ProductoDTOResponse;
import soda_roja.backend.model.Domicilio;
import soda_roja.backend.repository.ProductoRepository;
import soda_roja.backend.model.Producto;
import java.util.List;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository repository;

    public List<Producto> getAll() {
        return repository.findAll();
    }

    public Producto getById(int id) {
        return repository.findById(id).orElseThrow();
    }

    public Producto save(Producto entidad) {
        return repository.save(entidad);
    }

    public Producto update(int id, Producto entidad) {
        Producto existing = repository.findById(id).orElseThrow();
        existing.setNombre(entidad.getNombre());
        existing.setDetalle(entidad.getDetalle());
        existing.setPrecio(entidad.getPrecio());
        existing.setStock(entidad.getStock());
        return repository.save(existing);
    }

    public void delete(int id) {
        repository.deleteById(id);
    }
    public ProductoDTOResponse mapToDTO(Producto producto) {
        return ProductoDTOResponse.builder()
                .id(producto.getId())
                .nombre(producto.getNombre())
                .detalle(producto.getDetalle())
                .precio(producto.getPrecio())
                .stock(producto.getStock())
                .build();
    }
}