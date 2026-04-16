package soda_roja.backend.dtoResponse;

import lombok.*;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorResponseDTO {
    private String mensaje;
    private Map<String, String> errores;
    private int codigo;
}
