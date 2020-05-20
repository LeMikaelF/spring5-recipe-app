package guru.springframework.repositories;

import guru.springframework.domain.UnitOfMeasure;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataMongoTest
public class UnitOfMeasureRepositoryTestIT {

    @Autowired
    UnitOfMeasureRepository unitOfMeasureRepository;

    @Before
    public void setUp() throws Exception {
        unitOfMeasureRepository.save(new UnitOfMeasure("Ounce"));
    }

    @Test
    public void findByDescription() {
        final Optional<UnitOfMeasure> ounce = unitOfMeasureRepository.findByDescription("Ounce");
        assertEquals("Ounce", ounce.get().getDescription());
    }
}
