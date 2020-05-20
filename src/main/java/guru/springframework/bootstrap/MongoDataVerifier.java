package guru.springframework.bootstrap;

import guru.springframework.repositories.reactive.CategoryReactiveRepository;
import guru.springframework.repositories.reactive.RecipeReactiveRepository;
import guru.springframework.repositories.reactive.UnitOfMeasureReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MongoDataVerifier implements ApplicationListener<ContextRefreshedEvent>, Ordered {

    private final UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository;
    private final CategoryReactiveRepository categoryReactiveRepository;
    private final RecipeReactiveRepository recipeReactiveRepository;
    public MongoDataVerifier(UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository, CategoryReactiveRepository categoryReactiveRepository, RecipeReactiveRepository recipeReactiveRepository) {
        this.unitOfMeasureReactiveRepository = unitOfMeasureReactiveRepository;
        this.categoryReactiveRepository = categoryReactiveRepository;
        this.recipeReactiveRepository = recipeReactiveRepository;
    }


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.debug("Number of UOMs in reactive repo: {}", unitOfMeasureReactiveRepository.count().block());
        log.debug("Number of categories in reactive repo: {}", categoryReactiveRepository.count().block());
        log.debug("Number of recipes in reactive repo: {}", recipeReactiveRepository.count().block());
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }
}
