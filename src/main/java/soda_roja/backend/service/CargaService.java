package soda_roja.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import soda_roja.backend.dtoRequest.CargaDTORequest;
import soda_roja.backend.dtoRequestPut.CargaDTORequestPut;
import soda_roja.backend.dtoResponse.CargaDTOResponse;
import soda_roja.backend.model.Camion;
import soda_roja.backend.model.Carga;
import soda_roja.backend.model.Usuario;
import soda_roja.backend.repository.CamionRepository;
import soda_roja.backend.repository.CargaRepository;
import soda_roja.backend.repository.UsuarioRepository;

import java.util.List;

@Service
public class CargaService {

    @Autowired
    private CargaRepository repository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private CamionRepository camionRepository;

    public List<CargaDTOResponse> getAll() {
        return repository.findAll().stream().map(this::mapToDTO).toList();
    }

    public CargaDTOResponse getById(Long id) {
        return repository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new EntityNotFoundException("Carga no encontrada con id: " + id));
    }

    public CargaDTOResponse save(CargaDTORequest entidad) {
        Usuario usuario = usuarioRepository.findById(entidad.getIdUsuario())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con id: " + entidad.getIdUsuario()));
        
        Camion camion = camionRepository.findById(entidad.getIdCamion())
                .orElseThrow(() -> new EntityNotFoundException("Camion no encontrado con id: " + entidad.getIdCamion()));
        
        Carga carga = Carga.builder()
                .tipo(entidad.getTipo())
                .fechaHora(entidad.getFechaHora())
                .usuario(usuario)
                .camion(camion)
                .build();
        return mapToDTO(repository.save(carga));
    }

    public CargaDTOResponse update(Long id, CargaDTORequestPut entidad) {
        Usuario usuario = usuarioRepository.findById(entidad.getIdUsuario())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con id: " + entidad.getIdUsuario()));
        
        Camion camion = camionRepository.findById(entidad.getIdCamion())
                .orElseThrow(() -> new EntityNotFoundException("Camion no encontrado con id: " + entidad.getIdCamion()));
        
        Carga existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Carga no encontrada con id: " + id));
        
        existing.setTipo(entidad.getTipo());
        existing.setFechaHora(entidad.getFechaHora());
        existing.setUsuario(usuario);
        existing.setCamion(camion);

        return mapToDTO(repository.save(existing));
    }

    public void delete(Long id) {
        Carga existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Carga no encontrada con id: " + id));
        repository.delete(existing);
    }

    public CargaDTOResponse mapToDTO(Carga carga) {
        return CargaDTOResponse.builder()
                .id(carga.getId())
                .tipo(carga.getTipo())
                .fechaHora(carga.getFechaHora())
                .usuario(new UsuarioService().mapToDTO(carga.getUsuario()))
                .camion(new CamionService().mapToDTO(carga.getCamion()))
                .build();
    }
}
