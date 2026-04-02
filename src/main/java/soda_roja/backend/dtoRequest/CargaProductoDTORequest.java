package soda_roja.backend.dtoRequest;


import lombok.*;

@Data //getters, setters, toString, equals y hashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder


public class CargaProductoDTORequest {
    private long id;
    private int cantLleno;
    private int cantVacio;
}
