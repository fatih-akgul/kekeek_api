package com.kekeek.api.controller;

import com.kekeek.api.exception.ResourceNotFoundException;
import com.kekeek.api.model.Content;
import com.kekeek.api.model.SitePage;
import com.kekeek.api.repository.ContentRepository;
import com.kekeek.api.repository.PageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

@RestController
@RequestMapping("/pages")
public class PageController {

    private PageRepository pageRepository;
    private ContentRepository contentRepository;

    @Autowired
    public PageController(PageRepository pageRepository, ContentRepository contentRepository) {
        this.pageRepository = pageRepository;
        this.contentRepository = contentRepository;
    }

    @GetMapping
    public org.springframework.data.domain.Page getPages(Pageable pageable) {
        return pageRepository.findAll(pageable);
    }

    @PostMapping
    public SitePage createPage(@Valid @RequestBody SitePage page) {
        fillAutoFields(page);
        return pageRepository.save(page);
    }

    private void fillAutoFields(SitePage page) {
        if (page.getParentPageIdentifier() != null) {
            var parentPage = pageRepository.findByIdentifier(page.getParentPageIdentifier());
            parentPage.ifPresent(page::setParentPageId);
        }
    }

    @GetMapping("/{pageIdentifier}")
    public SitePage getPage(@PathVariable String pageIdentifier) {
        return pageRepository.findByIdentifier(pageIdentifier)
                .orElseThrow(() -> new ResourceNotFoundException("Site page not found with id " + pageIdentifier));
    }

    @PutMapping("/{pageIdentifier}")
    public SitePage replacePage(@PathVariable String pageIdentifier,
                                @Valid @RequestBody SitePage page) {
        return pageRepository.findByIdentifier(pageIdentifier)
                .map(existingPage -> {
                    Long id = existingPage.getId();
                    page.setId(id);
                    page.setIdentifier(pageIdentifier);
                    fillAutoFields(page);

                    return pageRepository.save(page);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Page not found: " + pageIdentifier));
    }

    @PatchMapping("/{pageIdentifier}")
    public SitePage updatePage(@PathVariable String pageIdentifier,
                               @Valid @RequestBody Map<String, Object> updates) {
        return pageRepository.findByIdentifier(pageIdentifier)
                .map(existingPage -> {
                    updates.forEach((k, v) -> {
                        Field field = ReflectionUtils.findField(SitePage.class, k);
                        if (field != null) {
                            ReflectionUtils.setField(field, existingPage, v);
                        }
                    });
                    fillAutoFields(existingPage);
                    return pageRepository.save(existingPage);
                }).orElseThrow(() -> new ResourceNotFoundException(
                        "Page not found: " + pageIdentifier
                ));
    }

    @DeleteMapping("/{pageIdentifier}")
    public ResponseEntity<?> deletePage(@PathVariable String pageIdentifier) {
        return pageRepository.findByIdentifier(pageIdentifier)
                .map(page -> {
                    pageRepository.delete(page);
                    return ResponseEntity.ok().build();
                }).orElseThrow(() -> new ResourceNotFoundException("Site page not found with id " + pageIdentifier));
    }

    @GetMapping("/{pageIdentifier}/contents")
    public Collection<Content> getContents(@PathVariable String pageIdentifier) {
        return contentRepository.findByPageIdentifier(pageIdentifier);
    }

    @PostMapping("/{pageIdentifier}/contents")
    public Content createContent(@PathVariable String pageIdentifier,
                                 @Valid @RequestBody Content content) {
        SitePage parentPage = pageRepository.findByIdentifier(pageIdentifier)
                .orElseThrow(() -> new ResourceNotFoundException(
                                "Page not found: " + pageIdentifier
                        )
                );
        content.setPage(parentPage);

        return contentRepository.save(content);
    }

    @GetMapping("/{pageIdentifier}/contents/{contentIdentifier}")
    public Content getContent(@PathVariable String pageIdentifier, @PathVariable String contentIdentifier) {
        return contentRepository.findByPageIdentifierAndIdentifier(pageIdentifier, contentIdentifier)
                .orElseThrow(() -> new ResourceNotFoundException("Content not found with page " + pageIdentifier + " and id " + contentIdentifier));
    }

    @PutMapping("/{pageIdentifier}/contents/{contentIdentifier}")
    public Content replaceContent(@PathVariable String pageIdentifier,
                                  @PathVariable String contentIdentifier,
                                  @Valid @RequestBody Content content) {
        SitePage parentPage = pageRepository.findByIdentifier(pageIdentifier)
                .orElseThrow(() -> new ResourceNotFoundException(
                                "Page not found: " + pageIdentifier
                        )
                );
        content.setPage(parentPage);

        return contentRepository.findByPageIdentifierAndIdentifier(pageIdentifier, contentIdentifier)
                .map(existingContent -> {
                    Long contentId = existingContent.getId();
                    content.setIdentifier(contentIdentifier);
                    content.setId(contentId);

                    return contentRepository.save(content);
                })
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Content not found: " + pageIdentifier + "/" + content.getIdentifier()));
    }

    @PatchMapping("/{pageIdentifier}/contents/{contentIdentifier}")
    public Content updateContent(@PathVariable String pageIdentifier,
                                 @PathVariable String contentIdentifier,
                                 @Valid @RequestBody Map<String, Object> updates) {
        if (pageRepository.existsByIdentifier(pageIdentifier)) {
            throw new ResourceNotFoundException("Page not found: " + pageIdentifier);
        }

        return contentRepository.findByPageIdentifierAndIdentifier(pageIdentifier, contentIdentifier)
                .map(existingContent -> {
                    updates.forEach((k, v) -> {
                        Field field = ReflectionUtils.findField(Content.class, k);
                        if (field != null) {
                            ReflectionUtils.setField(field, existingContent, v);
                        }
                    });

                    return contentRepository.save(existingContent);
                }).orElseThrow(() -> new ResourceNotFoundException(
                        "Content not found: " + pageIdentifier + "/" + contentIdentifier
                ));
    }

    @DeleteMapping("/{pageIdentifier}/contents/{contentIdentifier}")
    public ResponseEntity<?> deleteContent(@PathVariable String pageIdentifier,
                                        @PathVariable String contentIdentifier) {
        if (pageRepository.existsByIdentifier(pageIdentifier)) {
            throw new ResourceNotFoundException("Page not found: " + pageIdentifier);
        }

        return contentRepository.findByPageIdentifierAndIdentifier(pageIdentifier, contentIdentifier)
                .map(content -> {
                    contentRepository.delete(content);
                    return ResponseEntity.ok().build();
                }).orElseThrow(() -> new ResourceNotFoundException(
                        "Content not found: " + pageIdentifier + "/" + contentIdentifier
                ));
    }
}
