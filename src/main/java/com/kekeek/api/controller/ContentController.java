package com.kekeek.api.controller;

import com.kekeek.api.exception.ResourceNotFoundException;
import com.kekeek.api.model.Content;
import com.kekeek.api.model.ContentPage;
import com.kekeek.api.repository.ContentPageRepository;
import com.kekeek.api.repository.ContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/content")
public class ContentController {

    private ContentRepository contentRepository;
    private ContentPageRepository pageRepository;

    @Autowired
    public ContentController(ContentRepository contentRepository, ContentPageRepository pageRepository) {
        this.contentRepository = contentRepository;
        this.pageRepository = pageRepository;
    }

    @GetMapping
    public Page<Content> getContents(Pageable pageable) {
        return contentRepository.findAll(pageable);
    }

    @PostMapping
    public Content createContent(@Valid @RequestBody Content content) {
        return contentRepository.save(content);
    }

    @GetMapping("/{contentIdentifier}")
    public Content getContent(@PathVariable String contentIdentifier) {
        return contentRepository.findByIdentifier(contentIdentifier)
                .orElseThrow(() -> new ResourceNotFoundException("Content not found with id " + contentIdentifier));
    }

    @PutMapping("/{contentIdentifier}")
    public Content updateContent(@PathVariable String contentIdentifier,
                                   @Valid @RequestBody Content contentRequest) {
        Content content = contentRepository.findByIdentifier(contentIdentifier)
                .orElseThrow(() -> new ResourceNotFoundException("Content not found with id " + contentIdentifier));
        content.setTitle(contentRequest.getTitle());
        content.setDescription(contentRequest.getDescription());
        return contentRepository.save(content);
    }

    @DeleteMapping("/{contentIdentifier}")
    public ResponseEntity<?> deleteContent(@PathVariable String contentIdentifier) {
        return contentRepository.findByIdentifier(contentIdentifier)
                .map(content -> {
                    contentRepository.delete(content);
                    return ResponseEntity.ok().build();
                }).orElseThrow(() -> new ResourceNotFoundException("Content not found with id " + contentIdentifier));
    }

    @GetMapping("/{contentIdentifier}/pages")
    public List<ContentPage> getPagesByContentId(@PathVariable String contentIdentifier) {
        return pageRepository.findByContentIdentifier(contentIdentifier);
    }

    @PostMapping("/{contentIdentifier}/pages")
    public ContentPage addPage(@PathVariable String contentIdentifier,
                            @Valid @RequestBody ContentPage page) {
        return contentRepository.findByIdentifier(contentIdentifier)
                .map(content -> {
                    page.setContent(content);
                    return pageRepository.save(page);
                }).orElseThrow(() -> new ResourceNotFoundException("Content not found with id " + contentIdentifier));
    }

    @PutMapping("/{contentIdentifier}/pages/{pageIdentifier}")
    public ContentPage updatePage(@PathVariable String contentIdentifier,
                               @PathVariable String pageIdentifier,
                               @Valid @RequestBody ContentPage pageRequest) {
        if(!contentRepository.existsByIdentifier(contentIdentifier)) {
            throw new ResourceNotFoundException("Content not found with id " + contentIdentifier);
        }

        return pageRepository.findByIdentifier(pageIdentifier)
                .map(page -> {
                    page.setContentText(pageRequest.getContentText());
                    return pageRepository.save(page);
                }).orElseThrow(() -> new ResourceNotFoundException("Page not found with id " + pageIdentifier));
    }

    @DeleteMapping("/{contentIdentifier}/pages/{pageIdentifier}")
    public ResponseEntity<?> deletePage(@PathVariable String contentIdentifier,
                                          @PathVariable String pageIdentifier) {
        if(!contentRepository.existsByIdentifier(contentIdentifier)) {
            throw new ResourceNotFoundException("Content not found with id " + contentIdentifier);
        }

        return pageRepository.findByIdentifier(pageIdentifier)
                .map(page -> {
                    pageRepository.delete(page);
                    return ResponseEntity.ok().build();
                }).orElseThrow(() -> new ResourceNotFoundException("Page not found with id " + pageIdentifier));
    }
}
