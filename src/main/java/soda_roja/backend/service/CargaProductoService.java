package soda_roja.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import soda_roja.backend.dtoRequest.CargaProductoDTORequest;
import soda_roja.backend.dtoRequestPut.CargaProductoDTORequestPut;
import soda_roja.backend.dtoResponse.CargaProductoDTOResponse;
import soda_roja.backend.model.Carga;
import soda_roja.backend.model.CargaProducto;
import soda_roja.backend.model.Producto;
import soda_roja.backend.repository.CargaProductoRepository;
import soda_roja.backend.repository.CargaRepository;
import soda_roja.backend.repository.ProductoRepository;

import java.util.List;

@Service
public class CargaProductoService {

    @Autowired
    private CargaProductoRepository repository;
    @Autowired
    private CargaRepository cargaRepository;
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private MapToDTO  mapToDTOMapper;

    public List<CargaProductoDTOResponse> getAll(String[] populate) {
        return repository.findAll().stream().map(cargaProducto -> mapToDTOMapper.mapToDTO(cargaProducto, null)).toList();
    }

    public CargaProductoDTOResponse getById(Long id,String[] populate) {
        return repository.findById(id)
                .map(cargaProducto -> mapToDTOMapper.mapToDTO(cargaProducto, null))
                .orElseThrow(() -> new EntityNotFoundException("CargaProducto no encontrado con id: " + id));
    }

    public CargaProductoDTOResponse save(CargaProductoDTORequest entidad,String[] populate) {
        Carga carga = cargaRepository.findById(entidad.getIdCarga())
                .orElseThrow(() -> new EntityNotFoundException("Carga no encontrada con id: " + entidad.getIdCarga()));

        Producto producto = productoRepository.findById(entidad.getIdProducto())
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con id: " + entidad.getIdProducto()));

        CargaProducto cargaProducto = CargaProducto.builder()
                .cantLleno(entidad.getCantLleno())
                .cantVacio(entidad.getCantVacio())
                .carga(carga)
                .producto(producto)
                .build();

        return mapToDTOMapper.mapToDTO(repository.save(cargaProducto), null);
    }

    public CargaProductoDTOResponse update(Long id, CargaProductoDTORequestPut entidad,String[] populate) {
        CargaProducto existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("CargaProducto no encontrado con id: " + id));

        if(entidad.getIdCarga() != null) {
            Carga carga = cargaRepository.findById(entidad.getIdCarga())
                    .orElseThrow(() -> new EntityNotFoundException("Carga no encontrada con id: " + entidad.getIdCarga()));
            existing.setCarga(carga);
        }

        if(entidad.getIdProducto() != null) {
            Producto producto = productoRepository.findById(entidad.getIdProducto())
                    .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con id: " + entidad.getIdProducto()));
            existing.setProducto(producto);
        }

        if(entidad.getCantLleno() != null) {
            existing.setCantLleno(entidad.getCantLleno());
        }
        if(entidad.getCantVacio() != null) {
            existing.setCantVacio(entidad.getCantVacio());
        }

        return mapToDTOMapper.mapToDTO(repository.save(existing), null);
    }

    public void delete(Long id) {
        CargaProducto existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("CargaProducto no encontrado con id: " + id));
        repository.delete(existing);
    }



}
