package guru.springframework.services;

import guru.springframework.commands.UnitOfMeasureCommand;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UnitOfMeasureService {
    Flux<UnitOfMeasureCommand> findAllCommands();

    Mono<UnitOfMeasureCommand> findCommandById(String id);
}
