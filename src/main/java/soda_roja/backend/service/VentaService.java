package soda_roja.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import soda_roja.backend.dtoRequest.VentaDTORequest;
import soda_roja.backend.dtoResponse.VentaDTOResponse;
import soda_roja.backend.model.*;
import soda_roja.backend.repository.LineaPedidoRepository;
import soda_roja.backend.repository.DomicilioRepository;
import soda_roja.backend.repository.VentaRepository;

import java.util.List;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service

public class VentaService {

    @Autowired
    private VentaRepository repository;
    @Autowired
    private DomicilioRepository DomicilioRepository;
    @Autowired
    private LineaPedidoRepository lineaPedidoRepository;
    @Autowired
    private LineaPedidoService lineaPedidoService;

    public List<VentaDTOResponse> getAll() {
        return repository.findAll().stream().map(this::mapToDTO).toList();
    }

    public VentaDTOResponse getById(Long id) {
        Venta venta = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrado con id: " + id));
        return mapToDTO(venta);
    }

    public VentaDTOResponse save(VentaDTORequest entidad) {
        Domicilio Domicilio = DomicilioRepository.findById(entidad.getIdDomicilio())
                .orElseThrow(() -> new RuntimeException("Domicilio no encontrada con id: " + entidad.getIdDomicilio()));
        List<LineaPedido> lineasPedido =
                entidad.getLineasPedidoIds().stream().map(id -> lineaPedidoRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("LineaPedido no encontrada con id: " + id)))
                        .toList();
        Venta venta = Venta.builder()
                .fecha(entidad.getFecha())
                .total(entidad.getTotal())
                .pagado(entidad.isPagado())
                .domicilio(Domicilio)
                .lineasPedido(lineasPedido)
                .build();
        return mapToDTO(repository.save(venta));
    }

    public VentaDTOResponse update(Long id, VentaDTORequest entidad) {
        Venta existing = repository.findById(id).orElseThrow(()-> new RuntimeException("Venta no encontrado con id: " + id));
        Domicilio Domicilio = DomicilioRepository.findById(entidad.getIdDomicilio())
                .orElseThrow(() -> new RuntimeException("Domicilio no encontrada con id: " + entidad.getIdDomicilio()));
        List<LineaPedido> lineasPedido =
                entidad.getLineasPedidoIds().stream().map(idlp -> lineaPedidoRepository.findById(idlp)
                                .orElseThrow(() -> new RuntimeException("LineaPedido no encontrada con id: " + idlp)))
                        .toList();
        existing.setTotal(entidad.getTotal());
        existing.setFecha(entidad.getFecha());
        existing.setDomicilio(Domicilio);
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
                .idDomicilio( venta.getDomicilio().getId() )
                .lineasPedido(venta.getLineasPedido().stream().map(lp -> lineaPedidoService.mapToDTO(lp)).toList())
                .build();
    }
}
