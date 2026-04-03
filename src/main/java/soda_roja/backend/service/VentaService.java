package soda_roja.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import soda_roja.backend.dtoRequest.VentaDTORequest;
import soda_roja.backend.dtoResponse.VentaDTOResponse;
import soda_roja.backend.model.*;
import soda_roja.backend.repository.PersonaDomicilioRepository;
import soda_roja.backend.repository.VentaRepository;

import java.util.List;
@Service

public class VentaService {

    @Autowired
    private VentaRepository repository;
    @Autowired
    private PersonaDomicilioRepository PersonaDomicilioRepository;

    public List<VentaDTOResponse> getAll() {
        return repository.findAll().stream().map(this::mapToDTO).toList();
    }

    public VentaDTOResponse getById(Long id) {
        Venta venta = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrado con id: " + id));
        return mapToDTO(venta);
    }

    public VentaDTOResponse save(VentaDTORequest entidad) {
        PersonaDomicilio personaDomicilio = PersonaDomicilioRepository.findById(entidad.getIdPersonaDomicilio())
                .orElseThrow(() -> new RuntimeException("PersonaDomicilio no encontrada con id: " + entidad.getIdPersonaDomicilio()));
        Venta venta = Venta.builder()
                .fecha(entidad.getFecha())
                .total(entidad.getTotal())
                .pagado(entidad.isPagado())
                .personaDomicilio(personaDomicilio)
                .build();
        return mapToDTO(repository.save(venta));
    }

    public VentaDTOResponse update(Long id, VentaDTORequest entidad) {
        Venta existing = repository.findById(id).orElseThrow(()-> new RuntimeException("Venta no encontrado con id: " + id));
        PersonaDomicilio personaDomicilio = PersonaDomicilioRepository.findById(entidad.getIdPersonaDomicilio())
                .orElseThrow(() -> new RuntimeException("PersonaDomicilio no encontrada con id: " + entidad.getIdPersonaDomicilio()));
        existing.setTotal(entidad.getTotal());
        existing.setFecha(entidad.getFecha());
        existing.setPersonaDomicilio(personaDomicilio);
        existing.setPagado(entidad.isPagado());


        return mapToDTO(repository.save(existing));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public VentaDTOResponse mapToDTO(Venta venta) {
        return VentaDTOResponse.builder()
                .id(venta.getId())
                .fecha(venta.getFecha())
                .total(venta.getTotal())
                .pagado(venta.isPagado())
                .idPersonaDomicilio( venta.getPersonaDomicilio().getId() )
                .build();
    }
}
