package com.kekeek.api.controller;

import com.kekeek.api.exception.ResourceNotFoundException;
import com.kekeek.api.model.Page;
import com.kekeek.api.repository.PageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/pages")
public class PageController {

    private PageRepository pageRepository;

    @Autowired
    public PageController(PageRepository pageRepository) {
        this.pageRepository = pageRepository;
    }

    @GetMapping
    public org.springframework.data.domain.Page getSitePages(Pageable pageable) {
        return pageRepository.findAll(pageable);
    }

    @PostMapping
    public Page createPage(@Valid @RequestBody Page page) {
        return pageRepository.save(page);
    }

    @GetMapping("/{pageIdentifier}")
    public Page getPage(@PathVariable String pageIdentifier) {
        return pageRepository.findByIdentifier(pageIdentifier)
                .orElseThrow(() -> new ResourceNotFoundException("Site page not found with id " + pageIdentifier));
    }

    @PutMapping("/{pageIdentifier}")
    public Page updatePage(@PathVariable String pageIdentifier,
                           @Valid @RequestBody Page pageRequest) {
        Page page = pageRepository.findByIdentifier(pageIdentifier)
                .orElseThrow(() -> new ResourceNotFoundException("Site page not found with id " + pageIdentifier));
        page.setTitle(pageRequest.getTitle());
        return pageRepository.save(page);
    }

    @DeleteMapping("/{pageIdentifier}")
    public ResponseEntity<?> deletePage(@PathVariable String pageIdentifier) {
        return pageRepository.findByIdentifier(pageIdentifier)
                .map(page -> {
                    pageRepository.delete(page);
                    return ResponseEntity.ok().build();
                }).orElseThrow(() -> new ResourceNotFoundException("Site page not found with id " + pageIdentifier));
    }
}
