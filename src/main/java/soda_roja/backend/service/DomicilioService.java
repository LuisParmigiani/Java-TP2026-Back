package soda_roja.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import soda_roja.backend.dtoRequest.DomicilioDTORequest;
import soda_roja.backend.dtoRequestPut.DomicilioDTORequestPut;
import soda_roja.backend.dtoResponse.DomicilioDTOResponse;
import soda_roja.backend.model.Domicilio;
import soda_roja.backend.model.Zona;
import soda_roja.backend.model.Persona;
import soda_roja.backend.model.Camion;
import soda_roja.backend.model.ProductoDomicilio;
import soda_roja.backend.repository.DomicilioRepository;
import soda_roja.backend.repository.ZonaRepository;
import soda_roja.backend.repository.PersonaRepository;
import soda_roja.backend.repository.CamionRepository;
import soda_roja.backend.repository.ProductoDomicilioRepository;
import soda_roja.backend.specification.DomicilioSpecification;

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
    private CamionRepository camionRepository;

    @Autowired
    private ProductoDomicilioRepository productoDomicilioRepository;

    @Autowired
    private ZonaService zonaService;

    @Autowired
    private ProductoDomicilioService productoDomicilioService;

    public List<DomicilioDTOResponse> getAll() {
        return repository.findAll().stream().map(this::mapToDTO).toList();
    }

    public DomicilioDTOResponse getById(Long id) {
        return repository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new EntityNotFoundException("Domicilio no encontrado con id: " + id));
    }


    public List<DomicilioDTOResponse> getByUserId(Long id, String activo, Integer dias) {
        List<Domicilio> resultados;

        Boolean activeBoolean = null;
        if (((activo == null || activo.equals("Mostrar Todas")) || activo.isBlank()) && dias == null) {
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
                    throw new IllegalArgumentException("El parámetro 'activo' debe ser 'true' o 'false'");
                    }
                }
            }

            DomicilioSpecification.DomicilioFiltrosDTO filtros =
                    new DomicilioSpecification.DomicilioFiltrosDTO(id, activeBoolean, dias);

            resultados = repository.findAll(DomicilioSpecification.filtrar(filtros));
        }

        return resultados.stream()
                .map(this::mapToDTO)
                .toList();
    }



    public DomicilioDTOResponse save(DomicilioDTORequest dto) {
        Zona zona = findZonaOrThrow(dto.getZonaId());
        Persona persona = findPersonaOrThrow(dto.getPersonaId());

        Camion camion = dto.getCamionId() != null
                ? findCamionOrThrow(dto.getCamionId())
                : null;

        Domicilio domicilio = Domicilio.builder()
                .calle(dto.getCalle())
                .numero(dto.getNumero())
                .casa(dto.getCasa())
                .zona(zona)
                .activo(dto.getActivo())
                .persona(persona)
                .camion(camion)
                .dia(dto.getDia())
                .build();
        return mapToDTO(repository.save(domicilio));
    }

    public DomicilioDTOResponse update(Long id, DomicilioDTORequestPut entidad) {
        Zona zona = findZonaOrThrow(entidad.getZonaId());
        Persona persona = findPersonaOrThrow(entidad.getPersonaId());
        Camion camion = entidad.getCamionId() != null
                ? findCamionOrThrow(entidad.getCamionId())
                : null;


        Domicilio existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Domicilio no encontrado con id: " + id));
        existing.setCalle(entidad.getCalle());
        existing.setNumero(entidad.getNumero());
        existing.setActivo(entidad.getActivo());
        existing.setCasa(entidad.getCasa());
        existing.setZona(zona);
        existing.setPersona(persona);
        existing.setCamion(camion);
        existing.setDia(entidad.getDia());
        return mapToDTO(repository.save(existing));
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

    private Camion findCamionOrThrow(Long id) {
        return camionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Camion no encontrado con id: " + id));
    }

    private ProductoDomicilio findProductoPersonaDomicilioOrThrow(Long id) {
        return productoDomicilioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ProductoPersonaDomicilio no encontrado con id: " + id));
    }

    public DomicilioDTOResponse mapToDTO(Domicilio domicilio) {
        return DomicilioDTOResponse.builder()
                .id(domicilio.getId())
                .calle(domicilio.getCalle())
                .numero(domicilio.getNumero())
                .casa(domicilio.getCasa())
                .personaId(domicilio.getPersona() != null ? domicilio.getPersona().getId() : null)
                .zona(domicilio.getZona() != null ? new ZonaService().mapToDTO(domicilio.getZona()) : null)
                .productosDomicilio(domicilio.getProductoDomicilio() != null
                        ? domicilio.getProductoDomicilio().stream().map(productoDomicilio -> new ProductoDomicilioService().mapToDTO(productoDomicilio)).toList()
                        : List.of())
                .dia(domicilio.getDia())
                .activo(domicilio.getActivo())
                .build();
    }
}
