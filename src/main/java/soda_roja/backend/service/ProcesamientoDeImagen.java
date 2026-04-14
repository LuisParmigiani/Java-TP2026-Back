package soda_roja.backend.service;

import org.imgscalr.Scalr;
import org.springframework.stereotype.Service;
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
	            throw new IOException("No se pudo leer la imagen");
	        }

	        // Redimensionar manteniendo proporción
	        BufferedImage scaledImage = Scalr.resize(originalImage, Scalr.Method.QUALITY, 
	            Scalr.Mode.AUTOMATIC, maxWidth, maxHeight);

	        // Comprimir imagen
	        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	        ImageIO.write(scaledImage, "jpg", outputStream);
	        
	        return outputStream.toByteArray();
	    }
}
