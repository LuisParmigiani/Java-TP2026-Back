package soda_roja.backend.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import soda_roja.backend.model.LineaPedido;
import soda_roja.backend.repository.LineaPedidoRepository;

import java.util.List;
@Service
public class LineaPedidoService {

    @Autowired
    private LineaPedidoRepository repository;

    public List<LineaPedido> getAll() {
        return repository.findAll();
    }

    public LineaPedido getById(Long id) {
        return repository.findById(id).orElseThrow();
    }

    public LineaPedido save(LineaPedido entidad) {
        return repository.save(entidad);
    }

    public LineaPedido update(Long id, LineaPedido entidad) {
        LineaPedido existing = repository.findById(id).orElseThrow();
        existing.setCantidad(entidad.getCantidad());
        existing.setSubtotal(entidad.getSubtotal());


        return repository.save(existing);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

}
