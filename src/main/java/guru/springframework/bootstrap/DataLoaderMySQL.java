package guru.springframework.bootstrap;

import guru.springframework.domain.Category;
import guru.springframework.domain.UnitOfMeasure;
import guru.springframework.repositories.CategoryRepository;
import guru.springframework.repositories.UnitOfMeasureRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Profile({"dev", "prod"})
@Slf4j
public class DataLoaderMySQL implements ApplicationListener<ContextRefreshedEvent> {

    private final CategoryRepository categoryRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;

    public DataLoaderMySQL(CategoryRepository categoryRepository, UnitOfMeasureRepository unitOfMeasureRepository) {
        this.categoryRepository = categoryRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (categoryRepository.count() == 0) {
            initCategories();
            log.debug("Categories initialized in database.");
        }
        if (unitOfMeasureRepository.count() == 0) {
            initUoMs();
            log.debug("UOM's initialized in database");
        }
    }

    private void initCategories() {
        final Set<Category> categories = Stream.of("American", "Italian", "Mexican", "Fast Food")
                .map(Category::new).collect(Collectors.toSet());
        categoryRepository.saveAll(categories);
    }

    private void initUoMs() {
        final Set<UnitOfMeasure> uoms =
                Stream.of("Teaspoon", "Tablespoon", "Cup", "Pinch", "Ounce", "Dash", "Each", "Pint")
                .map(UnitOfMeasure::new).collect(Collectors.toSet());
        unitOfMeasureRepository.saveAll(uoms);
    }

    /*
    INSERT INTO category (category_name) VALUES ('American');
INSERT INTO category (category_name) VALUES ('Italian');
INSERT INTO category (category_name) VALUES ('Mexican');
INSERT INTO category (category_name) VALUES ('Fast Food');
INSERT INTO unit_of_measure (description) VALUES ('Teaspoon');
INSERT INTO unit_of_measure (description) VALUES ('Tablespoon');
INSERT INTO unit_of_measure (description) VALUES ('Cup');
INSERT INTO unit_of_measure (description) VALUES ('Pinch');
INSERT INTO unit_of_measure (description) VALUES ('Ounce');
INSERT INTO unit_of_measure (description) VALUES ('Dash');
INSERT INTO unit_of_measure (description) VALUES ('Each');
INSERT INTO unit_of_measure (description) VALUES ('Pint');

     */
}
