package guru.springframework.services;

import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.converters.UnitOfMeasureToUnitOfMeasureCommand;
import guru.springframework.domain.UnitOfMeasure;
import guru.springframework.repositories.UnitOfMeasureRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
class UnitOfMeasureServiceImpl implements UnitOfMeasureService {
    private final UnitOfMeasureRepository repository;
    private final UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand;

    UnitOfMeasureServiceImpl(UnitOfMeasureRepository repository, UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand) {
        this.repository = repository;
        this.unitOfMeasureToUnitOfMeasureCommand = unitOfMeasureToUnitOfMeasureCommand;
    }


    @Override
    public Set<UnitOfMeasureCommand> findAllCommands() {
        Set<UnitOfMeasure> set = new HashSet<>();
        repository.findAll().forEach(set::add);
        return set.stream().map(unitOfMeasureToUnitOfMeasureCommand::convert).collect(Collectors.toSet());
    }

    @Override
    public UnitOfMeasureCommand findCommandById(String id) {
        return repository.findById(id).map(unitOfMeasureToUnitOfMeasureCommand::convert).orElse(null);
    }
}
