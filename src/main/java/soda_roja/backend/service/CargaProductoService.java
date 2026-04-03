package soda_roja.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import soda_roja.backend.dtoRequest.CargaProductoDTORequest;
import soda_roja.backend.dtoResponse.CargaDTOResponse;
import soda_roja.backend.dtoResponse.CargaProductoDTOResponse;
import soda_roja.backend.dtoResponse.ProductoDTOResponse;
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
    

    public List<CargaProductoDTOResponse> getAll() {
        return repository.findAll().stream().map(this::mapToDTO).toList();
    }

    public CargaProductoDTOResponse getById(long id) {
    	CargaProducto cargaProducto = repository.findById(id)
				.orElseThrow(() -> new RuntimeException("CargaProducto no encontrado con id: " + id));
        return mapToDTO(cargaProducto);
    }

    public CargaProductoDTOResponse save(CargaProductoDTORequest entidad) {
    			Carga carga = cargaRepository.findById(entidad.getIdCarga()).orElseThrow(() -> new RuntimeException
    					("Carga no encontrada con id: " + entidad.getIdCarga()));
    			Producto producto = productoRepository.findById(entidad.getIdProducto()).orElseThrow(() -> new RuntimeException
    					("Producto no encontrado con id: " + entidad.getIdProducto()));
    			CargaProducto cargaProducto = CargaProducto.builder()
				.cantLleno(entidad.getCantLleno())
				.cantVacio(entidad.getCantVacio())
				.carga(carga)
				.producto(producto)
				.build();
        return mapToDTO(repository.save(cargaProducto));
    }

    public CargaProductoDTOResponse update(long id, CargaProductoDTORequest entidad) {
    	CargaProducto existing = repository.findById(id).orElseThrow();
        existing.setCantLleno(entidad.getCantLleno());
        existing.setCantVacio(entidad.getCantVacio());
        
        Carga carga = cargaRepository.findById(entidad.getIdCarga()).orElseThrow(() -> new RuntimeException
        ("Carga no encontrada con id: " + entidad.getIdCarga()));
        Producto producto = productoRepository.findById(entidad.getIdProducto()).orElseThrow(() -> new RuntimeException
		("Producto no encontrado con id: " + entidad.getIdProducto()));
        
        existing.setCarga(carga);
        existing.setProducto(producto);
        

        return mapToDTO(repository.save(existing));
    }

    public void delete(long id) {
        repository.deleteById(id);
    }
    
    private CargaProductoDTOResponse mapToDTO(CargaProducto cargaProducto) {
        return CargaProductoDTOResponse.builder()
        		.id(cargaProducto.getId())
        		.cantLleno(cargaProducto.getCantLleno())
        		.cantVacio(cargaProducto.getCantVacio())
        		.idCarga(cargaProducto.getCarga().getId())
        		.idProducto(cargaProducto.getProducto().getId())
        		.build();
                  }
}
