package soda_roja.backend.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import soda_roja.backend.model.Pago;
import soda_roja.backend.repository.PagoRepository;

import java.util.List;
@Service
public class PagoService {

    @Autowired
    private PagoRepository repository;

    public List<Pago> getAll() {
        return repository.findAll();
    }

    public Pago getById(Long id) {
        return repository.findById(id).orElseThrow();
    }

    public Pago save(Pago entidad) {
        return repository.save(entidad);
    }

    public Pago update(Long id, Pago entidad) {
        Pago existing = repository.findById(id).orElseThrow();
        existing.setMetodoPago(entidad.getMetodoPago());
        existing.setMonto(entidad.getMonto());
        existing.setFecha(entidad.getFecha());


        return repository.save(existing);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

}
