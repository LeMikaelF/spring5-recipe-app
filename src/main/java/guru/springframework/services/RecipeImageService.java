package guru.springframework.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface RecipeImageService {
    boolean save(String recipeId, MultipartFile image) throws IOException;

    Byte[] findById(String recipeId);
}
