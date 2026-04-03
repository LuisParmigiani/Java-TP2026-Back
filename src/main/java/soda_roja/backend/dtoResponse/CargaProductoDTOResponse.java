package soda_roja.backend.dtoResponse;


import lombok.*;

@Data //getters, setters, toString, equals y hashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder


public class CargaProductoDTOResponse {
    private long id;
    private int cantLleno;
    private int cantVacio;
    private long idCarga;
    private long idProducto;
}
