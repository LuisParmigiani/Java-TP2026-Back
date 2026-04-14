package soda_roja.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soda_roja.backend.dtoResponse.UploadResponse;
import soda_roja.backend.service.ProcesamientoDeImagen;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
@Tag(name = "Image Upload", description = "Endpoints para subir y procesar imágenes")
public class UploadController {

	private static final Logger logger = LoggerFactory.getLogger(UploadController.class);

	@Value("${file.upload-dir:src/main/resources/static/uploads}")
    private String uploadDir;

    @Autowired
    private ProcesamientoDeImagen procesadorImagen;

    @PostMapping("/upload")
    @Operation(
        summary = "Subir imagen",
        description = "Sube una imagen, la procesa (redimensiona a 1200x1200) y la guarda con el nombre producto{id}.jpg"
    )
    @ApiResponse(responseCode = "200", description = "Imagen subida exitosamente", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "400", description = "Archivo vacío o no es una imagen")
    @ApiResponse(responseCode = "500", description = "Error al procesar la imagen")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file, @RequestParam("productoId") Long productoId) {
        try {
            logger.info("Iniciando upload de imagen para producto ID: {}", productoId);
            logger.debug("Upload directory configurado: {}", uploadDir);

            if (file.isEmpty()) {
                logger.warn("Archivo vacío recibido para producto ID: {}", productoId);
                return ResponseEntity.badRequest().body("El archivo está vacío");
            }

            String contentType = file.getContentType();
            logger.debug("Tipo de contenido del archivo: {}", contentType);

            if (!contentType.startsWith("image/")) {
                logger.warn("Archivo no es imagen. Content-Type: {}", contentType);
                return ResponseEntity.badRequest().body("El archivo debe ser una imagen");
            }

            logger.info("Procesando imagen para producto ID: {}", productoId);
            byte[] processedImageBytes = procesadorImagen.processImage(file, 1200, 1200, 0.85f);
            logger.debug("Imagen procesada. Tamaño: {} bytes", processedImageBytes.length);

            String fileName = "producto" + productoId + ".jpg";
            logger.debug("Nombre de archivo generado: {}", fileName);

            File uploadDirectory = new File(uploadDir);
            logger.debug("Ruta absoluta del directorio: {}", uploadDirectory.getAbsolutePath());

            if (!uploadDirectory.exists()) {
                logger.info("Directorio no existe. Creando: {}", uploadDirectory.getAbsolutePath());
                boolean created = uploadDirectory.mkdirs();
                if (!created) {
                    logger.error("No se pudo crear el directorio: {}", uploadDirectory.getAbsolutePath());
                    return ResponseEntity.status(500).body("Error al crear directorio de uploads");
                }
            }

            File destinationFile = new File(uploadDirectory, fileName);
            logger.debug("Archivo destino: {}", destinationFile.getAbsolutePath());

            // Verificar si ya existe una imagen y eliminarla
            if (destinationFile.exists() ) {
                logger.info("Imagen existente encontrada. Eliminando: {}", destinationFile.getAbsolutePath());
                boolean deleted = destinationFile.delete();
                if (!deleted) {
                    logger.warn("No se pudo eliminar la imagen existente: {}", destinationFile.getAbsolutePath());
                } else {
                    logger.info("Imagen existente eliminada correctamente");
                }
            }

            try (FileOutputStream fos = new FileOutputStream(destinationFile)) {
                fos.write(processedImageBytes);
                logger.info("Archivo guardado exitosamente: {}", destinationFile.getAbsolutePath());
            }

            String filePath = "/uploads/" + fileName;
            logger.info("Upload completado. Ruta retornada: {}", filePath);
            return ResponseEntity.ok().body(new UploadResponse(filePath, "Imagen procesada y subida exitosamente"));

        } catch (IOException e) {
            logger.error("Error al procesar la imagen para producto ID: {}", productoId, e);
            return ResponseEntity.status(500).body("Error al procesar la imagen: " + e.getMessage());
        }
    }
}
