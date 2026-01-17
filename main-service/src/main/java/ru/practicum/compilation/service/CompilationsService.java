package ru.practicum.compilation.service;

import jakarta.validation.Valid;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;

import java.util.Collection;

public interface CompilationsService {
    Collection<CompilationDto> findAll(CompilationsPublicGetRequest getRequest);

    CompilationDto findById(long compId);

    CompilationDto save(@Valid NewCompilationDto newCompilationDto);

    void deleteById(long compId);

    CompilationDto update(long compId, UpdateCompilationRequest updateRequest);
}
