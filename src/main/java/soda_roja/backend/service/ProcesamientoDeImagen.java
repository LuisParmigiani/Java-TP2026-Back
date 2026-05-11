package soda_roja.backend.service;

import org.imgscalr.Scalr;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
@Service
public class ProcesamientoDeImagen {
	
    public byte[] processImage(MultipartFile file, int maxWidth, int maxHeight, float quality) throws IOException {
        // Leer imagen original
        BufferedImage originalImage = ImageIO.read(file.getInputStream());
        
        if (originalImage == null) {
            throw new IOException("No se pudo leer la imagen o formato no soportado");
        }

        // Obtener la extensión del archivo original (png, jpg, jpeg, webp)
        String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        if (extension == null || extension.isEmpty()) {
            extension = "jpg"; // Formato por defecto
        } else {
            extension = extension.toLowerCase();
            // Normalizar jpeg a jpg para ImageIO, esto es un estandar de Java
            if (extension.equals("jpeg")) {
                extension = "jpg";
            }
        }

        // Redimensionar manteniendo proporción
        BufferedImage scaledImage = Scalr.resize(originalImage, Scalr.Method.QUALITY, 
            Scalr.Mode.AUTOMATIC, maxWidth, maxHeight);

        // Escribir imagen en su formato original
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        boolean writerFound = ImageIO.write(scaledImage, extension, outputStream);
        
        if (!writerFound) {
            throw new IOException("No se encontró un escritor apropiado para el formato: " + extension);
        }
        
        return outputStream.toByteArray();
    }
}

