package soda_roja.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import soda_roja.backend.model.CargaProducto;
import soda_roja.backend.repository.CargaProductoRepository;

import java.util.List;
@Service

public class CargaProductoService {
    @Autowired
    private CargaProductoRepository repository;

    public List<CargaProducto> getAll() {
        return repository.findAll();
    }

    public CargaProducto getById(Long id) {
        return repository.findById(id).orElseThrow();
    }

    public CargaProducto save(CargaProducto entidad) {
        return repository.save(entidad);
    }

    public CargaProducto update(Long id, CargaProducto entidad) {
        CargaProducto existing = repository.findById(id).orElseThrow();
        existing.setCantLleno(entidad.getCantLleno());
        existing.setCantVacio(entidad.getCantVacio());

        return repository.save(existing);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
