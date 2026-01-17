package ru.practicum.compilation.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.service.CompilationsPublicGetRequest;
import ru.practicum.compilation.service.CompilationsService;

import java.util.Collection;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/compilations")
public class CompilationsPublicController {
    private final CompilationsService compService;

    @GetMapping
    public Collection<CompilationDto> getCompilations(@RequestParam boolean pinned,
                                                     @RequestParam(defaultValue = "0") int from,
                                                     @RequestParam(defaultValue = "10") int size
    ) {
        CompilationsPublicGetRequest getRequest = new CompilationsPublicGetRequest(pinned, from, size);
        log.info("Public get compilations requested with params= {}", getRequest);
        return compService.findAll(getRequest);
    }

    @GetMapping("/{compId}")
    public CompilationDto getById(@PathVariable long compId) {
        log.info("Public get compilation by id requested with id={}", compId);
        return compService.findById(compId);
    }
}
