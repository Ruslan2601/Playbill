package ru.practicum.main.service.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.main.service.dto.compilation.CompilationResponse;
import ru.practicum.main.service.dto.compilation.NewCompilationRequest;
import ru.practicum.main.service.dto.event.EventShortResponse;
import ru.practicum.main.service.model.Compilation;

import java.util.List;

@Component
public class CompilationMapper {

    public Compilation toCompilation(NewCompilationRequest newCompilationRequest) {
        Compilation compilation = new Compilation();
        compilation.setPinned(newCompilationRequest.isPinned());
        compilation.setTitle(newCompilationRequest.getTitle());
        compilation.setEvents(newCompilationRequest.getEvents());
        return compilation;
    }

    public CompilationResponse toCompilationResponse(Compilation compilation, List<EventShortResponse> events) {
        CompilationResponse compilationResponse = new CompilationResponse();
        compilationResponse.setId(compilation.getId());
        compilationResponse.setEvents(events);
        compilationResponse.setPinned(compilation.isPinned());
        compilationResponse.setTitle(compilation.getTitle());
        return compilationResponse;
    }
}
