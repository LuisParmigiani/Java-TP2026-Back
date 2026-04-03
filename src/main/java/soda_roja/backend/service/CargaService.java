package soda_roja.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import soda_roja.backend.dtoRequest.CargaDTORequest;
import soda_roja.backend.dtoResponse.CargaDTOResponse;
import soda_roja.backend.model.Carga;
import soda_roja.backend.model.Usuario;
import soda_roja.backend.repository.CargaRepository;
import soda_roja.backend.repository.UsuarioRepository;

import java.util.List;
@Service
public class CargaService {

    @Autowired
    private CargaRepository repository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<CargaDTOResponse> getAll() {
        return repository.findAll().stream().map(this::mapToDTO).toList();

    }

    public CargaDTOResponse getById(Long id) {
        Carga carga = repository.findById(id).orElseThrow(() -> new RuntimeException("Carga no encontrado con id: " + id));
        return mapToDTO(carga);
    }

    public CargaDTOResponse save(CargaDTORequest entidad) {
        Usuario usuario = usuarioRepository.findById(entidad.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrada con id: " + entidad.getIdUsuario()));
        Carga carga = Carga.builder()
                .tipo(entidad.getTipo())
                .fechaHora(entidad.getFechaHora())
                .usuario(usuario)
                .build();
        return mapToDTO(repository.save(carga));
    }

    public CargaDTOResponse update(Long id, CargaDTORequest entidad) {
        Usuario usuario = usuarioRepository.findById(entidad.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrada con id: " + entidad.getIdUsuario()));

        Carga existing = repository.findById(id).orElseThrow();
        existing.setTipo(entidad.getTipo());
        existing.setFechaHora(entidad.getFechaHora());
        existing.setUsuario(usuario);

        return mapToDTO(repository.save(existing)) ;
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
    public CargaDTOResponse mapToDTO(Carga carga) {
        return CargaDTOResponse.builder()
                .id(carga.getId())
                .tipo(carga.getTipo())
                .fechaHora(carga.getFechaHora())
                .usuario(new UsuarioService().mapToDTO(carga.getUsuario()))
                .build();
    }

}
