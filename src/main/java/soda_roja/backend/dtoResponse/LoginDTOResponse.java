package soda_roja.backend.dtoResponse;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginDTOResponse {
    private boolean success;
    private String token;
    private String error; // Optional, for error messages
}

