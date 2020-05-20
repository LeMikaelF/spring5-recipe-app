package guru.springframework.services;

import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.converters.UnitOfMeasureToUnitOfMeasureCommand;
import guru.springframework.domain.UnitOfMeasure;
import guru.springframework.repositories.UnitOfMeasureRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class UnitOfMeasureServiceImplTest {

    @Mock
    UnitOfMeasureRepository repository;
    UnitOfMeasureService service;
    UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        unitOfMeasureToUnitOfMeasureCommand = new UnitOfMeasureToUnitOfMeasureCommand();
        service = new UnitOfMeasureServiceImpl(repository, unitOfMeasureToUnitOfMeasureCommand);
    }

    @Test
    public void findAllCommands() {
        //given
        UnitOfMeasure unitOfMeasure1 = new UnitOfMeasure();
        unitOfMeasure1.setId("1");
        final UnitOfMeasure unitOfMeasure2 = new UnitOfMeasure();
        unitOfMeasure2.setId("2");
        final UnitOfMeasure unitOfMeasure3 = new UnitOfMeasure();
        unitOfMeasure3.setId("3");
        Set<UnitOfMeasure> set = Stream.of(unitOfMeasure1, unitOfMeasure2, unitOfMeasure3)
                .collect(Collectors.toSet());
        when(repository.findAll()).thenReturn(set);

        //when
        final Set<UnitOfMeasureCommand> all = service.findAllCommands();

        //then
        assertEquals(set.size(), all.size());
    }

    @Test
    public void findById() {
        //given
        final String uomId = String.valueOf(111L);
        final UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
        unitOfMeasure.setId(uomId);
        when(repository.findById(eq(uomId))).thenReturn(Optional.of(unitOfMeasure));

        //when
        final UnitOfMeasureCommand found = service.findCommandById(uomId);

        //then
        assertEquals(uomId, found.getId());
    }
}
