package soda_roja.backend.exception;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiError {
    private String message;
}
