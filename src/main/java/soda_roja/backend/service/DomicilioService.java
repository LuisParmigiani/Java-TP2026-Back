package soda_roja.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import soda_roja.backend.dtoRequest.DomicilioDTORequest;
import soda_roja.backend.dtoRequestPut.DomicilioDTORequestPut;
import soda_roja.backend.dtoResponse.DomicilioDTOResponse;
import soda_roja.backend.dtoResponse.UsuarioDTOResponse;
import soda_roja.backend.model.Domicilio;
import soda_roja.backend.model.Zona;
import soda_roja.backend.model.Persona;
import soda_roja.backend.repository.DomicilioRepository;
import soda_roja.backend.repository.ZonaRepository;
import soda_roja.backend.repository.PersonaRepository;import soda_roja.backend.specification.DomicilioSpecification;

import java.util.List;

@Service
public class DomicilioService {

    @Autowired
    private DomicilioRepository repository;

    @Autowired
    private ZonaRepository zonaRepository;

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private MapToDTO mapToDTOMapper;

    public List<DomicilioDTOResponse> getAll(String[] populate) {
        return repository.findAll().stream().map(d -> mapToDTO(d, populate)).toList();
    }

    public DomicilioDTOResponse getById(Long id,String[] populate) {
        return repository.findById(id)
                .map(d -> mapToDTO(d, populate))
                .orElseThrow(() -> new EntityNotFoundException("Domicilio no encontrado con id: " + id));
    }


    public List<DomicilioDTOResponse> getByUserId(Long id, String activo,String orderBy,String nameSearch, Integer dias, String[] populate) {
        List<Domicilio> resultados;

        Boolean activeBoolean = null;
        if (((activo == null || activo.equals("Mostrar Todas")) || activo.isBlank()) && dias == null ) {
            resultados = repository.findDomicilioByPersonaUsuarioId(id);

        } else {
            if (activo != null && !activo.isBlank() ) {
                if (activo.equalsIgnoreCase("true") || activo.equalsIgnoreCase("false")) {
                    activeBoolean = Boolean.parseBoolean(activo);
                } else {
                    if(activo.equalsIgnoreCase("Activas")){
                        activeBoolean = true;
                    } else if (activo.equals( "Inactivas")) {
                        activeBoolean = false;
                    }else {
                    throw new IllegalArgumentException("El parámetro 'activo' debe ser 'true' o 'false'" + activo);
                    }
                }
            }

            DomicilioSpecification.DomicilioFiltrosDTO filtros =
                    new DomicilioSpecification.DomicilioFiltrosDTO(id, activeBoolean, dias);

            resultados = repository.findAll(DomicilioSpecification.filtrar(filtros));
        }

        return resultados.stream()
                .map(d -> mapToDTO(d, populate))
                .toList();
    }



    public DomicilioDTOResponse save(DomicilioDTORequest dto,String userId,String[] populate) {
        UsuarioDTOResponse usuario = usuarioService.getById(Long.parseLong(userId), new String[]{"persona"});

        Zona zona = findZonaOrThrow(dto.getZonaId());

        Persona persona = findPersonaOrThrow(usuario.getPersona().getId());
        Integer[] dias = new Integer[7];
        for(int i = 0 ; i<7; i++){
            if(zona.getDia()[i]){
                dias[i] =  0;
            }else{
                dias[i] =  3;
            }
        }

        Domicilio domicilio = Domicilio.builder()
                .calle(dto.getCalle())
                .numero(dto.getNumero())
                .casa(dto.getCasa())
                .habilitado(0)
                .zona(zona)
                .activo(false)
                .persona(persona)
                .dia(dias)
                .build();
        return mapToDTO(repository.save(domicilio), populate);
    }

    public DomicilioDTOResponse update(Long id, DomicilioDTORequestPut entidad,String[] populate) {
        Domicilio existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Domicilio no encontrado con id: " + id));
        if(entidad.getZonaId() != null) {
            Zona zona = findZonaOrThrow(entidad.getZonaId());
            existing.setZona(zona);
        }

        if(entidad.getPersonaId() != null) {
            Persona persona = findPersonaOrThrow(entidad.getPersonaId());
            existing.setPersona(persona);
        }
        if(entidad.getHabilitado() != null) {
            existing.setHabilitado(entidad.getHabilitado());
        }


        if(entidad.getCalle() != null) {
            existing.setCalle(entidad.getCalle());
        }
        if(entidad.getNumero() != null) {
            existing.setNumero(entidad.getNumero());
        }
        if(entidad.getActivo() != null) {
            existing.setActivo(entidad.getActivo());
        }
        if(entidad.getCasa() != null) {
            existing.setCasa(entidad.getCasa());
        }
        if(entidad.getDia() != null) {
            existing.setDia(entidad.getDia());
        }

        return mapToDTO(repository.save(existing), populate);
    }
    public void delete(Long id) {
        Domicilio domicilio = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Domicilio no encontrado con id: " + id));
        repository.delete(domicilio);
    }

    private Zona findZonaOrThrow(Long id) {
        return zonaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Zona no encontrada con id: " + id));
    }

    private Persona findPersonaOrThrow(Long id) {
        return personaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Persona no encontrada con id: " + id));
    }


    private DomicilioDTOResponse mapToDTO(Domicilio domicilio, String[] populate) {
        return mapToDTOMapper.mapToDTO(domicilio, populate);
    }

}
