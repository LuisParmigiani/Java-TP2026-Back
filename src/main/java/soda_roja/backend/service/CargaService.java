package soda_roja.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import soda_roja.backend.dtoRequest.CargaDTORequest;
import soda_roja.backend.dtoRequestPut.CargaDTORequestPut;
import soda_roja.backend.dtoResponse.CargaDTOResponse;
import soda_roja.backend.model.Camion;
import soda_roja.backend.model.Carga;
import soda_roja.backend.model.CargaProducto;
import soda_roja.backend.model.Producto;
import soda_roja.backend.model.Usuario;
import soda_roja.backend.repository.CamionRepository;
import soda_roja.backend.repository.CargaRepository;
import soda_roja.backend.repository.ProductoRepository;
import soda_roja.backend.repository.UsuarioRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CargaService {

    @Autowired
    private CargaRepository repository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private CamionRepository camionRepository;
    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private MapToDTO mapToDTOMapper;

    public List<CargaDTOResponse> getAll(String[] populate) {
        return repository.findAll().stream().map(c -> mapToDTO(c, populate)).toList();
    }
    public List<CargaDTOResponse> getCargasHoy(String[] populate) {
        return repository.findCargasHoy().stream().map(c -> mapToDTO(c, populate)).toList();
    }

    public CargaDTOResponse getById(Long id,String[] populate) {
        return repository.findById(id)
                .map(c -> mapToDTO(c, populate))
                .orElseThrow(() -> new EntityNotFoundException("Carga no encontrada con id: " + id));
    }
    
    public List<CargaDTOResponse> getByUsuarioIdAndFechaHoraBetween(Long usuarioId, java.util.Date startOfDay, java.util.Date endOfDay, String[] populate) {
		return repository.findByUsuarioIdAndFechaHoraBetween(usuarioId, startOfDay, endOfDay)
				.stream()
				.map(c -> mapToDTO(c, populate))
				.toList();
	}

    public CargaDTOResponse save(CargaDTORequest entidad,String[] populate) {
        Usuario usuario = usuarioRepository.findById(entidad.getIdUsuario())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con id: " + entidad.getIdUsuario()));
        
        Camion camion = camionRepository.findById(entidad.getIdCamion())
                .orElseThrow(() -> new EntityNotFoundException("Camion no encontrado con id: " + entidad.getIdCamion()));
        Carga carga = new Carga();
        carga.setTipo(entidad.getTipo());
        carga.setFechaHora(entidad.getFechaHora());
        carga.setUsuario(usuario);
        carga.setCamion(camion);
        // Guardar la Carga primero
        Carga cargaSaved = repository.save(carga);
        
        
        List<CargaProducto> cargaProductos = entidad.getCargaProductos().stream()
            .map(dto -> {
		        Producto prod = productoRepository.findById(dto.getIdProducto())
		                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con id: " + dto.getIdProducto()));
                if(entidad.getTipo().equals("Carga")) {
                        prod.setStock(prod.getStock() - dto.getCantLleno() - dto.getCantVacio());;
                        productoRepository.save(prod);
                }
                if(entidad.getTipo().equals("Descarga") ) {
                    prod.setStock(prod.getStock() + dto.getCantLleno() + dto.getCantVacio());
                    productoRepository.save(prod);
                }
                CargaProducto cp = new CargaProducto();
                cp.setCantLleno(dto.getCantLleno());
                cp.setCantVacio(dto.getCantVacio());
                cp.setProducto(prod);
                cp.setCarga(cargaSaved);
                return cp;
            })
            .collect(Collectors.toList());
        
        cargaSaved.setCargasProducto(cargaProductos);
        Carga saved = repository.save(cargaSaved);

        return mapToDTO(saved, populate);
    }
    public CargaDTOResponse updateWithProducto(Long id, CargaDTORequest entidad, String[] populate) {
        Carga existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Carga no encontrada con id: " + id));

        Usuario usuario = usuarioRepository.findById(entidad.getIdUsuario())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con id: " + entidad.getIdUsuario()));

        Camion camion = camionRepository.findById(entidad.getIdCamion())
                .orElseThrow(() -> new EntityNotFoundException("Camion no encontrado con id: " + entidad.getIdCamion()));

        existing.setUsuario(usuario);
        existing.setCamion(camion);
        existing.getCargasProducto().forEach(cp -> {
                    Producto prod = cp.getProducto();
                    if (entidad.getTipo().equals("Carga")) {
                        prod.setStock(prod.getStock() + cp.getCantLleno() + cp.getCantVacio());
                        ;
                        productoRepository.save(prod);
                    }
                    if (entidad.getTipo().equals("Descarga")) {
                        prod.setStock(prod.getStock() - cp.getCantLleno() + cp.getCantVacio());
                        productoRepository.save(prod);
                    }
                });

        // 1. Vaciamos la colección gestionada por Hibernate, SIN cambiar la referencia
        existing.getCargasProducto().clear();

        // 2. Mapeamos los nuevos CargaProducto
        List<CargaProducto> nuevosProductos = entidad.getCargaProductos().stream()
            .map(dto -> {
                Producto prod = productoRepository.findById(dto.getIdProducto())
                        .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con id: " + dto.getIdProducto()));
                if(entidad.getTipo().equals("Carga")) {
                    prod.setStock(prod.getStock() - dto.getCantLleno() - dto.getCantVacio());;
                    productoRepository.save(prod);
                }
                if(entidad.getTipo().equals("Descarga") ) {
                    prod.setStock(prod.getStock() + dto.getCantLleno() + dto.getCantVacio());
                    productoRepository.save(prod);
                }
                CargaProducto cp = new CargaProducto();
                cp.setCantLleno(dto.getCantLleno());
                cp.setCantVacio(dto.getCantVacio());
                cp.setProducto(prod);
                // Asociamos la entidad padre existente
                cp.setCarga(existing); 
                return cp;
            })
            .collect(Collectors.toList());

        // 3. Añadimos los elementos a la MISMA colección que Hibernate está rastreando
        existing.getCargasProducto().addAll(nuevosProductos);

        // 4. Guardamos una sola vez al final
        Carga updated = repository.save(existing);
        
        return mapToDTO(updated, populate);
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
