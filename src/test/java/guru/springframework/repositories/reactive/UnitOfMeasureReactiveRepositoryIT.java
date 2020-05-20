package guru.springframework.repositories.reactive;


import guru.springframework.domain.UnitOfMeasure;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataMongoTest
public class UnitOfMeasureReactiveRepositoryIT {

    @Autowired
    UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository;
    UnitOfMeasure testUom;
    String testUomId = "testUomIdString";

    @Before
    public void setup() {
        testUom = new UnitOfMeasure("TestUnit");
        testUom.setId(testUomId);

        unitOfMeasureReactiveRepository.deleteAll().block();
    }

    @Test
    public void addThenRetrieveDocument() {
        assertEquals((Long) 0L, unitOfMeasureReactiveRepository.findAll().count().block());
        unitOfMeasureReactiveRepository.save(testUom).block();
        assertEquals((Long) 1L, unitOfMeasureReactiveRepository.findAll().count().block());
    }

}

