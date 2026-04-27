package soda_roja.backend.dtoResponse;


import lombok.*;

import java.util.List;


@Data //getters, setters, toString, equals y hashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class DiaZonaDTOResponse {
	private Long id;
	private Long diaId;
	private Long zonaId;
	
	//Aca va a ir la lista de ORdenDomicilioDTOResponse.
	private List<DiaZonaOrdenDTOResponse> diaZonaOrdenes;
	private DiaDTOResponse dia;
	private ZonaDTOResponse zona;
}
