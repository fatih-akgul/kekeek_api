package com.kekeek.api.controller;

import com.kekeek.api.exception.ResourceNotFoundException;
import com.kekeek.api.model.Content;
import com.kekeek.api.model.SitePage;
import com.kekeek.api.repository.ContentRepository;
import com.kekeek.api.repository.SitePageRepository;
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
import java.util.Collection;

@RestController
@RequestMapping("/site_pages")
public class SitePageController {

    private SitePageRepository sitePageRepository;
    private ContentRepository contentRepository;

    @Autowired
    public SitePageController(SitePageRepository pageRepository, ContentRepository contentRepository) {
        this.sitePageRepository = pageRepository;
        this.contentRepository = contentRepository;
    }

    @GetMapping
    public org.springframework.data.domain.Page getSitePages(Pageable pageable) {
        return sitePageRepository.findAll(pageable);
    }

    @PostMapping
    public SitePage createPage(@Valid @RequestBody SitePage page) {
        return sitePageRepository.save(page);
    }

    @GetMapping("/{pageIdentifier}")
    public SitePage getPage(@PathVariable String pageIdentifier) {
        return sitePageRepository.findByIdentifier(pageIdentifier)
                .orElseThrow(() -> new ResourceNotFoundException("Site page not found with id " + pageIdentifier));
    }

    @GetMapping("/{pageIdentifier}/contents")
    public Collection<Content> getPageContents(@PathVariable String pageIdentifier) {
        return contentRepository.findByPageIdentifier(pageIdentifier);
    }

    @PutMapping("/{pageIdentifier}")
    public SitePage updatePage(@PathVariable String pageIdentifier,
                           @Valid @RequestBody SitePage pageRequest) {
        SitePage page = sitePageRepository.findByIdentifier(pageIdentifier)
                .orElseThrow(() -> new ResourceNotFoundException("Site page not found with id " + pageIdentifier));
        page.setTitle(pageRequest.getTitle());
        return sitePageRepository.save(page);
    }

    @DeleteMapping("/{pageIdentifier}")
    public ResponseEntity<?> deletePage(@PathVariable String pageIdentifier) {
        return sitePageRepository.findByIdentifier(pageIdentifier)
                .map(page -> {
                    sitePageRepository.delete(page);
                    return ResponseEntity.ok().build();
                }).orElseThrow(() -> new ResourceNotFoundException("Site page not found with id " + pageIdentifier));
    }
}
