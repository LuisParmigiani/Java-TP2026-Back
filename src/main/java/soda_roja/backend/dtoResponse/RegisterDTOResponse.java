package soda_roja.backend.dtoResponse;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterDTOResponse {
    private boolean success;
    private String error; // Optional, for error messages
}
