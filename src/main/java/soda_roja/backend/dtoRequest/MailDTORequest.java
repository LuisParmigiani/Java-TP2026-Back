package soda_roja.backend.dtoRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data //getters, setters, toString, equals y hashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class MailDTORequest {

	    private String destino;
	    private String asunto;
	    private String cuerpo;

}
