package soda_roja.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import soda_roja.backend.dtoRequest.DomicilioDTORequest;
import soda_roja.backend.dtoRequestPut.DomicilioDTORequestPut;
import soda_roja.backend.dtoResponse.DomicilioDTOResponse;
import soda_roja.backend.dtoResponse.UsuarioDTOResponse;
import soda_roja.backend.model.*;
import soda_roja.backend.repository.*;
import soda_roja.backend.specification.DomicilioSpecification;
import soda_roja.backend.dtoRequest.DiaDomicilioDTORequest;
import java.util.List;
import java.util.Optional;
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
    private DiaZonaOrdenRepository diaZonaOrdenRepository;

    @Autowired
    private DiaZonaRepository diaZonaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private MapToDTO mapToDTOMapper;
    @Autowired
    private DiaRepository diaRepository;
    @Autowired
    private MailService mailService;

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
        if (((activo == null || activo.equals("Mostrar Todas")) || activo.isBlank()) && dias == null && orderBy == null && (nameSearch == null || nameSearch.isBlank())) {
            // Default case: return all domicilios with default pagination
            Pageable pageable = PageRequest.of(page, size);
            return repository.findAll(pageable)
                    .map(d -> mapToDTO(d, populate));
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
                new DomicilioSpecification.DomicilioFiltrosDTO(id, activo, dias, habilitado, nameSearch, null, null, null, null);

        return repository.findAll(DomicilioSpecification.filtrar(filtros), pageable)
                .map(d -> mapToDTO(d, populate));
    }

    public DomicilioDTOResponse save(DomicilioDTORequest dto,String userId,String[] populate) {
        Usuario usuario = usuarioRepository.findById(Long.parseLong(userId))
        						.orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con id: " + userId));
        Zona zona = findZonaOrThrow(dto.getZonaId());
        for(int i =0 ;i<usuario.getPersona().getDomicilios().size();i++){
            if(usuario.getPersona().getDomicilios().get(i).getCalle().equalsIgnoreCase(dto.getCalle()) && usuario.getPersona().getDomicilios().get(i).getNumero().equalsIgnoreCase(dto.getNumero()) && usuario.getPersona().getDomicilios().get(i).getCasa().equalsIgnoreCase(dto.getCasa()) ){
                throw new RuntimeException("El usuario ya tiene un domicilio con esa descripcion por cualquier consulta contactarse con el servicio.");
            }
        }

        Domicilio domicilio = Domicilio.builder()
                .calle(dto.getCalle())
                .numero(dto.getNumero())
                .casa(dto.getCasa())
                .habilitado("Pendiente")
                .zona(zona)
                .activo("Inactiva")
                .persona(usuario.getPersona())
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
        enviarMailDomicilio(savedDomicilio, "registrado");
        return d2;
    }

    public DomicilioDTOResponse saveByAdmin(DomicilioDTORequest dto,String[] populate){
        Usuario usuario = usuarioRepository.getById(dto.getPersonaId());
        Zona zona = findZonaOrThrow(dto.getZonaId());
        for(int i =0 ;i<usuario.getPersona().getDomicilios().size();i++){
            if(usuario.getPersona().getDomicilios().get(i).getCalle().equalsIgnoreCase(dto.getCalle()) && usuario.getPersona().getDomicilios().get(i).getNumero().equalsIgnoreCase(dto.getNumero()) && usuario.getPersona().getDomicilios().get(i).getCasa().equalsIgnoreCase(dto.getCasa()) ){
                throw new RuntimeException("El usuario ya tiene un domicilio con esa descripcion por cualquier consulta contactarse con el servicio.");
            }
        }

        Domicilio domicilio = Domicilio.builder()
                .calle(dto.getCalle())
                .numero(dto.getNumero())
                .casa(dto.getCasa())
                .habilitado("Habilitada")
                .zona(zona)
                .activo("Activa")
                .persona(usuario.getPersona())
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
        enviarMailDomicilio(savedDomicilio, "registrado");
        return d2;
    }


    @Transactional
    public DomicilioDTOResponse update(Long id, DomicilioDTORequestPut entidad, String[] populate) {
        Domicilio existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Domicilio no encontrado con id: " + id));

        if (entidad.getZonaId() != null) existing.setZona(findZonaOrThrow(entidad.getZonaId()));
        if (entidad.getPersonaId() != null) existing.setPersona(findPersonaOrThrow(entidad.getPersonaId()));
        if (entidad.getCalle() != null) existing.setCalle(entidad.getCalle());
        if (entidad.getNumero() != null) existing.setNumero(entidad.getNumero());
        if (entidad.getCasa() != null) existing.setCasa(entidad.getCasa());

        if (entidad.getHabilitado() != null) {
            if (entidad.getHabilitado().equalsIgnoreCase("Deshabilitado") && !existing.getHabilitado().equalsIgnoreCase("Deshabilitado")) {
                existing.getDiasDomicilio().forEach(dd -> {
                    if (dd.getEstado().equalsIgnoreCase("ACTIVO")) {
                        DiaZona diaZona = diaZonaRepository.getByDiaIdAndZonaId(dd.getDia().getId(), existing.getZona().getId());
                        sacarDeOrden(existing, diaZona);
                    }
                });
            }
            if (entidad.getHabilitado().equalsIgnoreCase("Habilitado") && !existing.getHabilitado().equalsIgnoreCase("Habilitado")) {
                existing.getDiasDomicilio().forEach(dd -> {
                    if (dd.getEstado().equalsIgnoreCase("ACTIVO") && existing.getActivo().equalsIgnoreCase("Activa")) {
                        DiaZona diaZona = diaZonaRepository.getByDiaIdAndZonaId(dd.getDia().getId(), existing.getZona().getId());
                        agregarAOrden(existing, diaZona);
                    }
                });
            }
            existing.setHabilitado(entidad.getHabilitado());
        }

        if (entidad.getActivo() != null) {
            if (entidad.getActivo().equalsIgnoreCase("Inactiva") && !existing.getActivo().equalsIgnoreCase("Inactiva")) {
                existing.getDiasDomicilio().forEach(dd -> {
                    if (dd.getEstado().equalsIgnoreCase("ACTIVO")) {
                        DiaZona diaZona = diaZonaRepository.getByDiaIdAndZonaId(dd.getDia().getId(), existing.getZona().getId());
                        sacarDeOrden(existing, diaZona);
                    }
                });
            }
            if (entidad.getActivo().equalsIgnoreCase("Activa") && !existing.getActivo().equalsIgnoreCase("Activa")) {
                existing.getDiasDomicilio().forEach(dd -> {
                    if (dd.getEstado().equalsIgnoreCase("ACTIVO") && existing.getHabilitado().equalsIgnoreCase("Habilitada")) {
                        DiaZona diaZona = diaZonaRepository.getByDiaIdAndZonaId(dd.getDia().getId(), existing.getZona().getId());
                        agregarAOrden(existing, diaZona);
                    }
                });
            }
            existing.setActivo(entidad.getActivo());
        }

        if (entidad.getDiasDomicilio() != null) {
            entidad.getDiasDomicilio().forEach(ddReq -> {
                DiaDomicilio diaDomicilio = diaDomicilioRepository.findByDomicilioIdAndDiaId(id, ddReq.getDiaId());
                if (ddReq.getEstado() == null) return;

                if (ddReq.getEstado().equalsIgnoreCase("ACTIVO") && !diaDomicilio.getEstado().equalsIgnoreCase("ACTIVO")) {
                    diaDomicilio.setEstado(ddReq.getEstado());
                    diaDomicilioRepository.save(diaDomicilio);
                    if (existing.getActivo().equalsIgnoreCase("Activa") && existing.getHabilitado().equalsIgnoreCase("Habilitada")) {
                        DiaZona diaZona = diaZonaRepository.getByDiaIdAndZonaId(diaDomicilio.getDia().getId(), existing.getZona().getId());
                        agregarAOrden(existing, diaZona);
                    }
                } else if (ddReq.getEstado().equalsIgnoreCase("INACTIVO") && !diaDomicilio.getEstado().equalsIgnoreCase("INACTIVO")) {
                    diaDomicilio.setEstado(ddReq.getEstado());
                    diaDomicilioRepository.save(diaDomicilio);
                    DiaZona diaZona = diaZonaRepository.getByDiaIdAndZonaId(diaDomicilio.getDia().getId(), existing.getZona().getId());
                    sacarDeOrden(existing, diaZona);
                }
            });
        }

        Domicilio saved = repository.save(existing);
        enviarMailDomicilio(saved, "actualizado");
        return mapToDTO(saved, populate);
    }



    @Transactional
    public DomicilioDTOResponse updateDias(List<DiaDomicilioDTORequest> dias, Long domicilioId) {
        Domicilio domicilio = repository.findById(domicilioId)
                .orElseThrow(() -> new EntityNotFoundException("Domicilio no encontrado con id: " + domicilioId));

        // Limpiar y reutilizar la lista existente
        domicilio.getDiasDomicilio().clear();

        for (DiaDomicilioDTORequest diaDom : dias) {
            Dia dia = diaRepository.findById(diaDom.getDiaId())
                    .orElseThrow(() -> new EntityNotFoundException("Dia no encontrado con id: " + diaDom.getDiaId()));
            DiaDomicilio nuevoDiaDomicilio = DiaDomicilio.builder()
                    .dia(dia)
                    .domicilio(domicilio)
                    .estado(diaDom.getEstado())
                    .build();
            // Agregar directamente a la lista existente
            domicilio.getDiasDomicilio().add(nuevoDiaDomicilio);
        }

        return mapToDTO(repository.save(domicilio), new String[]{"diaDomicilio"});
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

    private void agregarAOrden(Domicilio domicilio, DiaZona diaZona) {
        DiaZonaOrden max = diaZonaOrdenRepository.findTopByDiaZonaIdOrderByOrdenDesc(diaZona.getId());
        int nuevoOrden = max != null ? max.getOrden() + 1 : 1;
        diaZonaOrdenRepository.save(DiaZonaOrden.builder()
                .diaZona(diaZona)
                .domicilio(domicilio)
                .orden(nuevoOrden)
                .build());
    }

    private void sacarDeOrden(Domicilio domicilio, DiaZona diaZona) {
        DiaZonaOrden orden = diaZonaOrdenRepository.findByDomicilioIdAndDiaZonaId(domicilio.getId(), diaZona.getId());
        if (orden != null) {
            Integer ordenEliminado = orden.getOrden();
            Long diaZonaId = diaZona.getId();
            diaZonaOrdenRepository.delete(orden);
            diaZonaOrdenRepository.decrementarOrdenesPosteriores(diaZonaId, ordenEliminado);
        }
    }

    private void enviarMailDomicilio(Domicilio domicilio, String accion) {
        Persona persona = domicilio.getPersona();
        if (persona == null || persona.getEmail() == null) return;

        String casa = domicilio.getCasa() != null && !domicilio.getCasa().isBlank()
                ? "<p>Casa/Depto: <b>" + domicilio.getCasa() + "</b></p>" : "";

        StringBuilder diasHtml = new StringBuilder();
        if (domicilio.getDiasDomicilio() != null) {
            domicilio.getDiasDomicilio().forEach(dd -> {
                if (!"NODISPONIBLE".equalsIgnoreCase(dd.getEstado())) {
                    diasHtml.append("<li>").append(dd.getDia().getNombre())
                            .append(" — ").append(dd.getEstado()).append("</li>");
                }
            });
        }
        String diasSeccion = diasHtml.length() > 0
                ? "<br><p><b>Días disponibles:</b></p><ul>" + diasHtml + "</ul>" : "";

        String cuerpo = "<h1>Domicilio " + accion + "</h1>" +
            "<p>Hola <b>" + persona.getNombre() + " " + persona.getApellido() + "</b>, " +
            "te informamos que el siguiente domicilio fue <b>" + accion + "</b> en tu cuenta.</p>" +
            "<br>" +
            "<p>Calle: <b>" + domicilio.getCalle() + " " + domicilio.getNumero() + "</b></p>" +
            casa +
            "<p>Zona: <b>" + domicilio.getZona().getNombre() + "</b></p>" +
            "<p>Estado: <b>" + domicilio.getActivo() + "</b></p>" +
            "<p>Habilitado: <b>" + domicilio.getHabilitado() + "</b></p>" +
            diasSeccion;

        String asunto = accion.equals("registrado")
                ? "Nuevo domicilio registrado - Soda Roja"
                : "Domicilio actualizado - Soda Roja";

        mailService.enviarMail(persona.getEmail(), asunto, cuerpo);
    }

    private DomicilioDTOResponse mapToDTO(Domicilio domicilio, String[] populate) {
        return mapToDTOMapper.mapToDTO(domicilio, populate);
    }

    public List<DomicilioDTOResponse> getPendientes(String[] populate) {
        return repository.findDomicilioByHabilitado("Pendiente").stream().map(d -> mapToDTO(d, populate)).toList();
    }
}
