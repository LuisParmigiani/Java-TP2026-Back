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
	private List<Long> domicilioIds;
	//Aca va a ir la lista de ORdenDomicilioDTOResponse.
	private DiaDTOResponse dia;
	private ZonaDTOResponse zona;
}
