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

    @Autowired
    private MapToDTO mapToDTOMapper;

    public List<CargaDTOResponse> getAll(String[] populate) {
        return repository.findAll().stream().map(c -> mapToDTO(c, populate)).toList();
    }

    public CargaDTOResponse getById(Long id,String[] populate) {
        return repository.findById(id)
                .map(c -> mapToDTO(c, populate))
                .orElseThrow(() -> new EntityNotFoundException("Carga no encontrada con id: " + id));
    }

    public CargaDTOResponse save(CargaDTORequest entidad,String[] populate) {
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
        return mapToDTO(repository.save(carga), populate);
    }

    public CargaDTOResponse update(Long id, CargaDTORequestPut entidad,String[] populate) {
        Carga existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Carga no encontrada con id: " + id));
        if (entidad.getIdUsuario() != null) {
            Usuario usuario = usuarioRepository.findById(entidad.getIdUsuario())
                    .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con id: " + entidad.getIdUsuario()));
            existing.setUsuario(usuario);

        }
        if( entidad.getIdCamion() != null) {
            Camion camion = camionRepository.findById(entidad.getIdCamion())
                    .orElseThrow(() -> new EntityNotFoundException("Camion no encontrado con id: " + entidad.getIdCamion()));
                existing.setCamion(camion);
        }
        if(entidad.getTipo() != null) {
            existing.setTipo(entidad.getTipo());
        }
        if (entidad.getFechaHora() != null) {
            existing.setFechaHora(entidad.getFechaHora());
        }
        return mapToDTO(repository.save(existing), populate);
    }

    public void delete(Long id) {
        Carga existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Carga no encontrada con id: " + id));
        repository.delete(existing);
    }

    private CargaDTOResponse mapToDTO(Carga carga, String[] populate) {
        return mapToDTOMapper.mapToDTO(carga, populate);
    }

}
