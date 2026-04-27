package soda_roja.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soda_roja.backend.dtoResponse.UploadResponse;
import soda_roja.backend.service.CloudinaryService;

@RestController
@RequestMapping("/api")
@Tag(name = "Image Upload", description = "Endpoints para subir y procesar imágenes")
public class UploadController {

    private static final Logger logger = LoggerFactory.getLogger(UploadController.class);

    @Autowired
    private CloudinaryService cloudinaryService;

    @PostMapping("/upload")
    @Operation(
        summary = "Subir imagen",
        description = "Sube una imagen, la procesa (redimensiona a 1200x1200) y la sube a Cloudinary con el nombre producto{id}.jpg"
    )
    @ApiResponse(responseCode = "200", description = "Imagen subida exitosamente", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "400", description = "Archivo vacío o no es una imagen")
    @ApiResponse(responseCode = "500", description = "Error al procesar la imagen")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file, @RequestParam("productoId") Long productoId) {
        try {
            logger.info("Iniciando upload de imagen para producto ID: {}", productoId);
            
            String imageUrl = cloudinaryService.uploadImage(file, productoId);
            
            logger.info("Imagen subida exitosamente a Cloudinary. URL: {}", imageUrl);
            return ResponseEntity.ok().body(new UploadResponse(imageUrl, "Imagen procesada y subida exitosamente"));

        } catch (IllegalArgumentException e) {
            logger.warn("Error de validación para producto ID: {}: {}", productoId, e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error al procesar o subir la imagen para producto ID: {}", productoId, e);
            return ResponseEntity.status(500).body("Error al procesar la imagen: " + e.getMessage());
        }
    }
}
