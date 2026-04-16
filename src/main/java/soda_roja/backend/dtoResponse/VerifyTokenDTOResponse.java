package soda_roja.backend.dtoResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data //getters, setters, toString, equals y hashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VerifyTokenDTOResponse {
	
	private boolean valid;
	
}
