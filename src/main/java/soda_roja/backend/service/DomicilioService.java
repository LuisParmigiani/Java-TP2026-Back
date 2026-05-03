package soda_roja.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import soda_roja.backend.dtoRequest.DomicilioDTORequest;
import soda_roja.backend.dtoRequestPut.DomicilioDTORequestPut;
import soda_roja.backend.dtoResponse.DomicilioDTOResponse;
import soda_roja.backend.dtoResponse.UsuarioDTOResponse;
import soda_roja.backend.model.*;
import soda_roja.backend.repository.*;
import soda_roja.backend.specification.DomicilioSpecification;

import java.util.List;
import java.util.ArrayList;

@Service
public class DomicilioService {

    @Autowired
    private DomicilioRepository repository;

    @Autowired
    private ZonaRepository zonaRepository;

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private DiaDomicilioRepository diaDomicilioRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private MapToDTO mapToDTOMapper;
    @Autowired
    private DiaRepository diaRepository;

    public List<DomicilioDTOResponse> getAll(String[] populate) {
    	return repository.findAll().stream().map(d -> mapToDTO(d, populate)).toList();
    }
    public List<DomicilioDTOResponse> getAllByCalleOrNumero(String[] populate, String calleNumero) {
    	return repository.findDomicilioByCalleOrNumero(calleNumero).stream().map(d -> mapToDTO(d, populate)).toList();
    }
    

    public DomicilioDTOResponse getById(Long id,String[] populate) {
        return repository.findById(id)
                .map(d -> mapToDTO(d, populate))
                .orElseThrow(() -> new EntityNotFoundException("Domicilio no encontrado con id: " + id));
    }



    public Page<DomicilioDTOResponse> getByUserId(Long id, String orderBy, String nameSearch,String habilitado ,String activo, Integer dias, String[] populate, Integer page, Integer size) {
        Boolean activeBoolean = null;
        if (((activo == null || activo.equals("Mostrar Todas")) || activo.isBlank()) && dias == null && orderBy == null && (nameSearch == null || nameSearch.isBlank())) {
            // Default case: return all domicilios with default pagination
            Pageable pageable = PageRequest.of(page, size);
            return repository.findAll(pageable)
                    .map(d -> mapToDTO(d, populate));
        }

        if (activo != null && !activo.isBlank()) {
            if (activo.equalsIgnoreCase("true") || activo.equalsIgnoreCase("false")) {
                activeBoolean = Boolean.parseBoolean(activo);
            } else {
                if (activo.equalsIgnoreCase("Activas")) {
                    activeBoolean = true;
                } else if (activo.equals("Inactivas")) {
                    activeBoolean = false;
                }
            }
        }

        Sort sort = Sort.unsorted();
        if (orderBy != null && !orderBy.isBlank()) {
            sort = switch (orderBy) {
                case "Nombre A-Z" -> Sort.by("calle").ascending();
                case "Nombre Z-A" -> Sort.by("calle").descending();
                case "Número Ascendente" -> Sort.by("numero").ascending();
                case "Número Descendente" -> Sort.by("numero").descending();
                default -> Sort.by("id").descending();
            };
        }


        Pageable pageable = PageRequest.of(page, size, sort);
        DomicilioSpecification.DomicilioFiltrosDTO filtros =
                new DomicilioSpecification.DomicilioFiltrosDTO(id, activeBoolean, dias, habilitado, nameSearch);

        return repository.findAll(DomicilioSpecification.filtrar(filtros), pageable)
                .map(d -> mapToDTO(d, populate));
    }

    public DomicilioDTOResponse save(DomicilioDTORequest dto,String userId,String[] populate) {
        UsuarioDTOResponse usuario = usuarioService.getById(Long.parseLong(userId), new String[]{"persona"});
        Zona zona = findZonaOrThrow(dto.getZonaId());
        Persona persona = findPersonaOrThrow(usuario.getPersona().getId());
        Domicilio domicilio = Domicilio.builder()
                .calle(dto.getCalle())
                .numero(dto.getNumero())
                .casa(dto.getCasa())
                .habilitado(0)
                .zona(zona)
                .activo(false)
                .persona(persona)
                .build();

        Domicilio savedDomicilio = repository.save(domicilio);

        List<Long> DiasDisponiblesId = zona.getDiasZona().stream().map(dz -> dz.getDia().getId()).toList();
        List<DiaDomicilio> diaDomicilio = new ArrayList<>();
        for(long j =1; j<8;j++){
            final long diaId = j;
            DiaDomicilio dd =   DiaDomicilio.builder()
                    .dia(diaRepository.findById(diaId).orElseThrow(() -> new EntityNotFoundException("Dia no encontrado con id: " + diaId)))
                    .domicilio(savedDomicilio)
                    .estado(DiasDisponiblesId.contains(diaId) ? "INACTIVO" : "NODISPONIBLE")
                    .build();
            diaDomicilio.add(dd);
        }
        diaDomicilioRepository.saveAll(diaDomicilio);
        savedDomicilio.setDiasDomicilio(diaDomicilio);
        populate = populate != null ? populate : new String[]{"diaDomicilio"};


        DomicilioDTOResponse d2 = mapToDTO(savedDomicilio, populate);
        System.out.println("Domicilio guardado: " + d2);
        return d2;
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
            System.out.println("Valor de activo en DTO: " + entidad.getActivo());
            existing.setActivo(entidad.getActivo());
        }
        if(entidad.getCasa() != null) {
            existing.setCasa(entidad.getCasa());
        }
        if(entidad.getDiasDomicilio() != null) {
            entidad.getDiasDomicilio().forEach(dd -> {
                DiaDomicilio diaDomicilio = diaDomicilioRepository.findByDomicilioIdAndDiaId(id, dd.getDiaId());
                if (dd.getEstado() != null) {
                    diaDomicilio.setEstado(dd.getEstado());
                }

            });
        }
        System.out.println("Valor de activo en existing: " + existing.getActivo());
        DomicilioDTOResponse dt =  mapToDTO(repository.save(existing), populate);
        System.out.println("DTO Response: " + dt);
        return dt;
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
