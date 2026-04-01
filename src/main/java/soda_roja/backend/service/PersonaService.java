package soda_roja.backend.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import soda_roja.backend.repository.PersonaRepository;
import soda_roja.backend.model.Persona;
import java.util.List;

@Service
public class PersonaService {

    @Autowired
    private PersonaRepository repository;

    public List<Persona> getAll() {
        return repository.findAll();
    }

    public Persona getById(Long id) {
        return repository.findById(id).orElseThrow();
    }

    public Persona save(Persona entidad) {
        return repository.save(entidad);
    }

    public Persona update(Long id, Persona entidad) {
        Persona existing = repository.findById(id).orElseThrow();
        existing.setTipoDoc(entidad.getTipoDoc());
        existing.setNroDocumento(entidad.getNroDocumento());
        existing.setNombre(entidad.getNombre());
        existing.setApellido(entidad.getApellido());
        existing.setEmail(entidad.getEmail());
        existing.setTelefono(entidad.getTelefono());
        existing.setDeuda(entidad.getDeuda());

        return repository.save(existing);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

}
