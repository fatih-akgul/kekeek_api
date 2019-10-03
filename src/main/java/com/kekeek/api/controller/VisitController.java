package com.kekeek.api.controller;

import com.kekeek.api.model.Visit;
import com.kekeek.api.service.VisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/visits")
public class VisitController {

    private VisitService visitService;

    @Autowired
    public VisitController(VisitService visitService) {
        this.visitService = visitService;
    }

    @PostMapping
    public Visit postVisit(@Valid @RequestBody Visit visit) {
        return visitService.addVisit(visit);
    }

    @GetMapping("/top/{topVisitsCount}")
    public Collection<Visit> getTopVisits(@PathVariable Integer topVisitsCount) {
        return visitService.getTopVisits(topVisitsCount);
    }
}
