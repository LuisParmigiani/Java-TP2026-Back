package soda_roja.backend.dto;

import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CargaDTO {
    private Long id;
    private String tipo;
    private Date fechaHora;
}
