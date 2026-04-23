package soda_roja.backend.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import soda_roja.backend.dtoRequest.PedidoSemanalDTORequest;
import soda_roja.backend.dtoRequestPut.PedidoSemanalDTORequestPut;
import soda_roja.backend.dtoResponse.PedidoSemanalDTOResponse;
import soda_roja.backend.model.Domicilio;
import soda_roja.backend.model.PedidoSemanal;
import soda_roja.backend.model.ProductoZona;
import soda_roja.backend.repository.DomicilioRepository;
import soda_roja.backend.repository.PedidoSemanalRepository;
import soda_roja.backend.repository.ProductoZonaRepository;

import java.util.List;

@Service
public class PedidoSemanalService {

    @Autowired
    private PedidoSemanalRepository repository;

    @Autowired
    private DomicilioRepository domicilioRepository;
    @Autowired
    private ProductoZonaRepository productoZonaRepository;
    @Autowired
    private MapToDTO mapToDTOMapper;

    public List<PedidoSemanalDTOResponse> getAll(String[] populate) {
        return repository.findAll().stream().map(p -> mapToDTO(p, populate)).toList();
    }

    public PedidoSemanalDTOResponse getById(Long id,String[] populate) {
        return repository.findById(id)
                .map(p -> mapToDTO(p, populate))
                .orElseThrow(() -> new EntityNotFoundException("PedidoSemanal no encontrado con id: " + id));
    }

    public PedidoSemanalDTOResponse save(PedidoSemanalDTORequest entidad,String[] populate) {
        Domicilio domicilio = domicilioRepository.findById(entidad.getDomicilioId())
                .orElseThrow(() -> new EntityNotFoundException("Domicilio no encontrado con id: " + entidad.getDomicilioId()));
        ProductoZona productoZona = productoZonaRepository.findById(entidad.getProductoZonaId())
                .orElseThrow(() -> new EntityNotFoundException("ProductoZona no encontrado con id: " + entidad.getProductoZonaId()));
        PedidoSemanal pedidoSemanal = PedidoSemanal.builder()
                .domicilio(domicilio)
                .productoZona(productoZona)
                .cantidad(entidad.getCantidad())
                .build();
        return mapToDTO(repository.save(pedidoSemanal), populate);
    }

    public PedidoSemanalDTOResponse update(Long id, PedidoSemanalDTORequestPut entidad,String[] populate) {
        PedidoSemanal existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("PedidoSemanal no encontrado con id: " + id));
        if(entidad.getCantidad() != existing.getCantidad()) {
            existing.setCantidad(entidad.getCantidad());
        }
        if(entidad.getDomicilioId() != null) {
             Domicilio domicilio = domicilioRepository.findById(entidad.getDomicilioId())
                    .orElseThrow(() -> new EntityNotFoundException("Domicilio no encontrado con id: " + entidad.getDomicilioId()));
            existing.setDomicilio(domicilio);
        }
        if (entidad.getProductoZonaId() != null) {
             ProductoZona productoZona = productoZonaRepository.findById(entidad.getProductoZonaId())
                    .orElseThrow(() -> new EntityNotFoundException("ProductoZona no encontrado con id: " + entidad.getProductoZonaId()));
                existing.setProductoZona(productoZona);
        }
        return mapToDTO(repository.save(existing), populate);
    }
    @Transactional
    public List<PedidoSemanalDTOResponse> saveList(List<PedidoSemanalDTORequest> entidades,String domicilioId,String[] populate) {
        Domicilio domicilio = domicilioRepository.findById(Long.parseLong(domicilioId))
                .orElseThrow(() -> new EntityNotFoundException("Domicilio no encontrado con id: " + domicilioId));

        // Obtener los IDs de ProductoZona que vienen en la nueva lista
        List<Long> nuevosProductoZonaIds = entidades.stream()
                .map(e -> {
                    ProductoZona pz = productoZonaRepository.findByZonaIdAndProductoId(domicilio.getZona().getId(), e.getProductoId())
                            .orElseThrow(() -> new EntityNotFoundException("ProductoZona no encontrado con id: " + e.getProductoZonaId()));
                    return pz.getId();
                })
                .toList();

        // Obtener los pedidos existentes para este domicilio
        List<PedidoSemanal> pedidosExistentes = repository.findAllByDomicilioId(domicilio.getId());

        // Poner en cantidad 0 los pedidos que no vienen en la nueva lista
        pedidosExistentes.stream()
                .filter(p -> !nuevosProductoZonaIds.contains(p.getProductoZona().getId()))
                .forEach(p -> {
                    p.setCantidad(0);
                    repository.save(p);
                });

        // Guardar o actualizar los pedidos de la nueva lista
        return entidades.stream().map(e -> {
            ProductoZona productoZona = productoZonaRepository.findByZonaIdAndProductoId(domicilio.getZona().getId(), e.getProductoId())
                    .orElseThrow(() -> new EntityNotFoundException("ProductoZona no encontrado con id: " + e.getProductoZonaId()));

            // Buscar si ya existe un pedido con este domicilio y productoZona
            PedidoSemanal pedidoSemanal = pedidosExistentes.stream()
                    .filter(p -> p.getProductoZona().getId().equals(productoZona.getId()))
                    .findFirst()
                    .orElse(null);

            if (pedidoSemanal != null) {
                // Si existe, actualizar la cantidad
                pedidoSemanal.setCantidad(e.getCantidad());
            } else {
                // Si no existe, crear uno nuevo
                pedidoSemanal = PedidoSemanal.builder()
                        .domicilio(domicilio)
                        .productoZona(productoZona)
                        .cantidad(e.getCantidad())
                        .build();
            }
            return mapToDTO(repository.save(pedidoSemanal), populate);
        }).toList();
    }

    public void delete(Long id) {
        PedidoSemanal pedidoSemanal = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("PedidoSemanal no encontrado con id: " + id));
        repository.delete(pedidoSemanal);
    }

    private PedidoSemanalDTOResponse mapToDTO(PedidoSemanal pedidoSemanal, String[] populate) {
        return mapToDTOMapper.mapToDTO(pedidoSemanal, populate);
    }

}
