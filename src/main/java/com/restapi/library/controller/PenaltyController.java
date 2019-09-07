package com.restapi.library.controller;

import com.restapi.library.dto.PenaltyDto;
import com.restapi.library.service.PenaltyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/penalties")
public class PenaltyController {

    private final PenaltyService penaltyService;

    @Autowired
    public PenaltyController(final PenaltyService penaltyService) {
        this.penaltyService = penaltyService;
    }

    @GetMapping
    public List<PenaltyDto> getAllPenalties(@RequestParam(required = false) Boolean paid) {
        return penaltyService.getAllPenalties(paid).stream()
                .map(PenaltyDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public PenaltyDto getPenalty(@PathVariable Long id) {
        return new PenaltyDto(penaltyService.getPenalty(id));
    }

    @PutMapping
    public PenaltyDto updatePenalty(@RequestBody PenaltyDto penaltyDto) {
        return new PenaltyDto(penaltyService.markPenaltyPaid(penaltyDto.getId()));
    }

}
