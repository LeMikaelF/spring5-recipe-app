package guru.springframework.services;

import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;

public interface RecipeImageService {
    Mono<Void> save(String recipeId, MultipartFile image) throws IOException;

    Mono<Byte[]> findById(String recipeId);
}
