package soda_roja.backend.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;

@Service
public class CloudinaryService {
    
    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private ProcesamientoDeImagen procesadorImagen;

    public String uploadImage(MultipartFile file, Long productoId) throws Exception {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("El archivo está vacío");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("El archivo debe ser una imagen");
        }

        byte[] processedImageBytes = procesadorImagen.processImage(file, 1200, 1200, 0.85f);
        String fileName = "producto" + productoId;

        Map uploadResult = cloudinary.uploader().upload(
            processedImageBytes,
            ObjectUtils.asMap("folder", "productos","public_id", fileName, "overwrite", true, "resource_type", "image")
        );
        
        return (String) uploadResult.get("secure_url");
    }
    
    public boolean deleteImage(String publicId) throws Exception {
        Map result = cloudinary.uploader().destroy(
            publicId,
            ObjectUtils.asMap("resource_type", "image")
        );
        return "ok".equals(result.get("result"));
    }

}
