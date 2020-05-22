package guru.springframework.services;

import guru.springframework.domain.Recipe;
import guru.springframework.repositories.reactive.RecipeReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.Exceptions;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.stream.IntStream;

@Slf4j
@Service
public class RecipeImageServiceImpl implements RecipeImageService {
    private final RecipeReactiveRepository recipeReactiveRepository;

    public RecipeImageServiceImpl(RecipeReactiveRepository recipeReactiveRepository) {
        this.recipeReactiveRepository = recipeReactiveRepository;
    }

    @Override
    public Mono<Void> save(String recipeId, MultipartFile image) throws IOException {
        return recipeReactiveRepository.findById(recipeId)
                .flatMap(recipe -> {
                    final byte[] bytes;
                    try {
                        bytes = image.getBytes();
                        final Byte[] wrappedByteArray = IntStream.range(0, bytes.length).mapToObj(i -> bytes[i]).toArray(Byte[]::new);
                        recipe.setImage(wrappedByteArray);
                        return recipeReactiveRepository.save(recipe);
                    } catch (IOException e) {
                        return Mono.error(Exceptions.propagate(e));
                    }
                })
                .then();
    }

    @Override
    public Mono<Byte[]> findById(String recipeId) {
        return recipeReactiveRepository.findById(recipeId).map(Recipe::getImage);
    }

}
