package br.com.codenation.central_de_erros.service.Impl;

import br.com.codenation.central_de_erros.entity.Level;
import br.com.codenation.central_de_erros.entity.Event;
import br.com.codenation.central_de_erros.entity.LevelConverter;
import br.com.codenation.central_de_erros.exception.WrongUserInputException;
import br.com.codenation.central_de_erros.repository.EventRepository;
import br.com.codenation.central_de_erros.service.interfaces.EventServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventService implements EventServiceInterface {

    private final EventRepository repository;

    @Override
    public Event save(Event event){
        return repository.save(event);
    }

    @Override
    public Event saveNew(Event event) {
        return repository.findByLevelAndDescriptionAndLogAndOrigin(
                event.getLevel(), event.getDescription(),
                event.getLog(), event.getOrigin()
        )
                .map(e -> {
                    e.setNumber(e.getNumber() + event.getNumber());
                    return repository.save(e);
                })
                .orElseGet(() -> repository.save(event));
    }

    @Override
    public Optional<Event> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Page<Event> findAll(Pageable pageable) { return repository.findAll(pageable); }

    @Override
    public Page<Event> findByLevel(String levelString, Pageable pageable, LevelConverter typeConverter) {
        try {
            Level level = typeConverter.convertToEntityAttribute(levelString);
            return repository.findByLevel(level, pageable);
        } catch (IllegalArgumentException e){
            throw new WrongUserInputException("Input '" + levelString +
                    "' is not valid.  Try: 'WARNING', 'INFO', 'ERROR' or 'W', 'I', 'E'.");
        }
    }

    @Override
    public Page<Event> findByDescription(String description, Pageable pageable){
        return repository.findByDescriptionContaining(description, pageable);
    }

    @Override
    public Page<Event> findByOrigin(String origin, Pageable pageable){
        return repository.findByOriginContaining(origin, pageable);
    }

    @Override
    public Page<Event> findByLog(String log, Pageable pageable){
        return repository.findByLogContaining(log,pageable);
    }

    @Override
    public Page<Event> findByDateTime(String stringDate, Pageable pageable){
        try {
            LocalDateTime date = LocalDateTime.parse(stringDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            return repository.findByDateTime(date, pageable);
        }catch (DateTimeParseException e){
            throw new WrongUserInputException("Input '" + stringDate +
                    "' is not valid. Try date format: 'yyyy-MM-dd HH:mm'.");
        }
    }

    @Override
    public Page<Event> findByNumber(String numberString, Pageable pageable) {
        try {
            Long number = Long.valueOf(numberString);
            return repository.findByNumber(number, pageable);
        }catch (NumberFormatException n){
            throw new WrongUserInputException("Input '" + numberString +
                    "' is not valid. It should be a integer number.");
        }
    }

    @Override
    public void delete(Long id) { repository.deleteById(id); }

}
