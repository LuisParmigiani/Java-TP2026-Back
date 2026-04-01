package soda_roja.backend.dto;


import lombok.*;

@Data //getters, setters, toString, equals y hashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder


public class CargaProductoDTO {
    private long id;
    private int cantLleno;
    private int cantVacio;
}
