package soda_roja.backend.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import soda_roja.backend.dtoRequestPut.LineaPedidoDTORequestPut;
import soda_roja.backend.model.LineaPedido;
import soda_roja.backend.model.ProductoZona;
import soda_roja.backend.repository.LineaPedidoRepository;
import soda_roja.backend.repository.ProductoZonaRepository;
import soda_roja.backend.dtoRequest.LineaPedidoDTORequest;
import soda_roja.backend.dtoResponse.LineaPedidoDTOResponse;

import java.util.List;

@Service
public class LineaPedidoService {

    @Autowired
    private LineaPedidoRepository repository;

    @Autowired
    private ProductoZonaRepository productoZonaRepository;

    @Autowired
    private MapToDTO mapToDTOMapper;

    public List<LineaPedidoDTOResponse> getAll(String[] populate) {
        return repository.findAll().stream().map(l -> mapToDTO(l, populate)).toList();
    }

    public LineaPedidoDTOResponse getById(Long id,String[] populate) {
        return repository.findById(id)
                .map(l -> mapToDTO(l, populate))
                .orElseThrow(() -> new EntityNotFoundException("LineaPedido no encontrado con id: " + id));
    }

    public LineaPedidoDTOResponse save(LineaPedidoDTORequest entidad,String[] populate) {
        ProductoZona productoZona;

        productoZona = findProductoZonaOrThrow(entidad.getProductoZonaId());

        Double precio = productoZona.getProducto().getPrecio();

        float subTotal = (float) (precio * entidad.getCantidad());
        LineaPedido lineaPedido = LineaPedido.builder()
                .cantidad(entidad.getCantidad())
                .subtotal(subTotal)
                .productoZona(productoZona)
                .build();

        LineaPedido saved = repository.save(lineaPedido);
        return mapToDTO(saved, populate);
    }

    public LineaPedidoDTOResponse update(Long id, LineaPedidoDTORequestPut entidad,String[] populate) {
        LineaPedido existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("LineaPedido no encontrado con id: " + id));

        if(entidad.getProductoZonaId() != null) {
            ProductoZona productoZona = findProductoZonaOrThrow(entidad.getProductoZonaId());
            existing.setProductoZona(productoZona);
        }

        if(entidad.getCantidad() != null) {
            existing.setCantidad(entidad.getCantidad());
            Double precio = existing.getProductoZona().getProducto().getPrecio();
            float subTotal = (float) (precio * entidad.getCantidad());
            existing.setSubtotal(subTotal);
        }

        return mapToDTO(repository.save(existing), populate);
    }

    public void delete(Long id) {
        LineaPedido lineaPedido = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("LineaPedido no encontrado con id: " + id));
        repository.delete(lineaPedido);
    }


    private ProductoZona findProductoZonaOrThrow(Long id) {
        return productoZonaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ProductoZona no encontrado con id: " + id));
    }

    private LineaPedidoDTOResponse mapToDTO(LineaPedido lineaPedido, String[] populate) {
        return mapToDTOMapper.mapToDTO(lineaPedido, populate);
    }

}
