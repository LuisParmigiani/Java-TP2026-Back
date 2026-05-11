package soda_roja.backend.service;

import org.springframework.beans.factory.annotation.Autowired;



import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import soda_roja.backend.dtoRequest.DiaDomicilioDTORequest;
import soda_roja.backend.dtoRequest.DiaZonaDTORequest;
import soda_roja.backend.dtoRequest.ZonaDTORequest;
import soda_roja.backend.dtoRequestPut.DiaZonaDTORequestPut;
import soda_roja.backend.dtoRequestPut.ZonaDTORequestPut;
import soda_roja.backend.dtoResponse.DomicilioDTOResponse;
import soda_roja.backend.dtoResponse.ZonaDTOResponse;
import soda_roja.backend.model.Zona;
import soda_roja.backend.model.Camion;
import soda_roja.backend.model.Dia;
import soda_roja.backend.model.DiaDomicilio;
import soda_roja.backend.model.DiaZona;
import soda_roja.backend.model.Domicilio;
import soda_roja.backend.model.Producto;
import soda_roja.backend.model.ProductoZona;
import soda_roja.backend.repository.ZonaRepository;
import soda_roja.backend.repository.CamionRepository;
import soda_roja.backend.repository.DiaRepository;
import soda_roja.backend.repository.ProductoRepository;
import soda_roja.backend.repository.ProductoZonaRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class ZonaService {

    @Autowired
    private ProductoZonaRepository pZonarepository;
    @Autowired
    private ProductoRepository prodRepository;
    @Autowired
    private ZonaRepository repository;
    @Autowired
    private MapToDTO mapToDTOMapper;
    @Autowired
    private CamionRepository camionRepository;
    @Autowired
    private DiaRepository diaRepository;

    public List<ZonaDTOResponse> getAll(String[] populate) {
        return repository.findAll().stream().map(z -> mapToDTO(z, populate)).toList();
    }

    public ZonaDTOResponse getById(Long id,String[] populate) {
        return repository.findById(id)
                .map(z -> mapToDTO(z, populate))
                .orElseThrow(() -> new EntityNotFoundException("Zona no encontrada con id: " + id));
    }
    
    @Transactional
    public ZonaDTOResponse save(ZonaDTORequest entidad,String[] populate) {
    	List<Producto> productos = prodRepository.findAll().stream().toList();
    	Camion camion = camionRepository.findById(entidad.getCamionId())
				.orElseThrow(() -> new EntityNotFoundException("Camion no encontrado con id: " + entidad.getCamionId()));
        Zona zona = Zona.builder()
                .nombre(entidad.getNombre())
                .detalle(entidad.getDetalle())
                .camion(camion)
                .build();
        if (zona.getDiasZona() == null) {
            zona.setDiasZona(new ArrayList<>());
        }

        if (entidad.getDiasZona() != null) {
			System.out.println("Recibiendo lista de dias para crear la zona");
			System.out.println(entidad.getDiasZona());
			for (DiaZonaDTORequestPut diaDom : entidad.getDiasZona()) {
				Dia dia = diaRepository.findById(diaDom.getDiaId())
						.orElseThrow(() -> new EntityNotFoundException("Dia no encontrado con id: " + diaDom.getDiaId()));
				DiaZona nuevoDiaZona = DiaZona.builder()
						.dia(dia)
						.zona(zona)
						.build();
				zona.getDiasZona().add(nuevoDiaZona);
			}
		}
        Zona zonaReponse = repository.save(zona);
        productos.forEach(p -> {
        	ProductoZona pz = new ProductoZona();
			pz.setProducto(p);
			pz.setZona(zonaReponse);
			pZonarepository.save(pz);
	
		});
        return mapToDTO(zonaReponse, populate);
    }

    public ZonaDTOResponse update(Long id, ZonaDTORequestPut entidad ,String[] populate) {
        Zona zona = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Zona no encontrada con id: " + id));
        System.out.println("Recibiendo lista de dias para actualizar la zona con id: " + id);
        System.out.println(entidad.getDiasZona());
        if(entidad.getNombre() != null) {
            zona.setNombre(entidad.getNombre());
        }
        if(entidad.getDetalle() != null) {
            zona.setDetalle(entidad.getDetalle());
        }
        if(entidad.getCamionId() != null) {
			Camion camion = camionRepository.findById(entidad.getCamionId())
					.orElseThrow(() -> new EntityNotFoundException("Camion no encontrado con id"));
			zona.setCamion(camion);
			}	
        if(entidad.getDiasZona() != null) {
        	System.out.println("Actualizando dias de la zona con id: " + id);
			updateDias(entidad.getDiasZona(), id);
		}
        return mapToDTO(repository.save(zona), populate);
    }
    @Transactional
    public ZonaDTOResponse updateDias(List<DiaZonaDTORequestPut> dias, Long zonaId) {
        Zona zona = repository.findById(zonaId)
                .orElseThrow(() -> new EntityNotFoundException("Zona no encontrada con id: " + zonaId));

        // Limpiar y reutilizar la lista existente
        zona.getDiasZona().clear();

        for (DiaZonaDTORequestPut diaDom : dias) {
            Dia dia = diaRepository.findById(diaDom.getDiaId())
                    .orElseThrow(() -> new EntityNotFoundException("Dia no encontrado con id: " + diaDom.getDiaId()));
            DiaZona nuevoDiaZona = DiaZona.builder()
                    .dia(dia)
                    .zona(zona)
                    .build();
            // Agregar directamente a la lista existente
            zona.getDiasZona().add(nuevoDiaZona);
        }

        return mapToDTO(repository.save(zona), new String[]{"diaZona"});
    }


    public void delete(Long id) {
        Zona zona = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Zona no encontrada con id: " + id));
        repository.delete(zona);
    }

    private ZonaDTOResponse mapToDTO(Zona zona, String[] populate) {
        return mapToDTOMapper.mapToDTO(zona, populate);
    }

}
