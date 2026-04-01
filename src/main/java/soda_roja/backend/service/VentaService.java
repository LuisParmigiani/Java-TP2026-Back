package soda_roja.backend.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import soda_roja.backend.model.Venta;
import soda_roja.backend.repository.CargaRepository;
import soda_roja.backend.model.Carga;
import soda_roja.backend.repository.VentaRepository;

import java.util.List;
@Service
public class VentaService {

    @Autowired
    private VentaRepository repository;

    public List<Venta> getAll() {
        return repository.findAll();
    }

    public Venta getById(Long id) {
        return repository.findById(id).orElseThrow();
    }

    public Venta save(Venta entidad) {
        return repository.save(entidad);
    }

    public Venta update(Long id, Venta entidad) {
        Venta existing = repository.findById(id).orElseThrow();
        existing.setFecha(entidad.getFecha());
        existing.setPagado(entidad.isPagado());
        existing.setTotal(entidad.getTotal());



        return repository.save(existing);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

}
