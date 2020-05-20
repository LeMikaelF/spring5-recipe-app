package guru.springframework.repositories.reactive;

import guru.springframework.domain.Category;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@DataMongoTest
public class CategoryReactiveRepositoryIT {

    final String categoryName = "testCategory";
    @Autowired
    CategoryReactiveRepository categoryReactiveRepository;
    Category testCategory;

    @Before
    public void setup() {
        this.testCategory = new Category(categoryName);
        categoryReactiveRepository.deleteAll().block();
    }

    @Test
    public void saveAndRetrieveCategory() {
        assertEquals((Long) 0L, categoryReactiveRepository.count().block());
        categoryReactiveRepository.save(testCategory).block();
        assertEquals((Long) 1L, categoryReactiveRepository.count().block());
    }

    @Test
    public void findByCategoryName() {
        categoryReactiveRepository.save(testCategory).block();
        final Category found = categoryReactiveRepository.findByCategoryName(categoryName).block();
        assertNotNull(found);
        assertEquals(categoryName, found.getCategoryName());
    }
}
