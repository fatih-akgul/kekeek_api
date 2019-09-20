package com.kekeek.api.controller;

import com.kekeek.api.exception.ResourceNotFoundException;
import com.kekeek.api.model.*;
import com.kekeek.api.repository.ContentRepository;
import com.kekeek.api.repository.PageHierarchyRepository;
import com.kekeek.api.repository.PageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.Field;
import java.util.*;

@RestController
@RequestMapping("/pages")
public class PageController {

    private PageRepository pageRepository;
    private ContentRepository contentRepository;
    private PageHierarchyRepository pageHierarchyRepository;

    @Autowired
    public PageController(PageRepository pageRepository, ContentRepository contentRepository, PageHierarchyRepository pageHierarchyRepository) {
        this.pageRepository = pageRepository;
        this.contentRepository = contentRepository;
        this.pageHierarchyRepository = pageHierarchyRepository;
    }

    @GetMapping
    public org.springframework.data.domain.Page getPages(Pageable pageable) {
        return pageRepository.findAll(pageable);
    }

    @PostMapping
    public SitePage createPage(@Valid @RequestBody SitePage page) {
//        fillAutoFields(page);
        return pageRepository.save(page);
    }

    @PostMapping("/hierarchy")
    public PageHierarchy createPageRelationship(@Valid @RequestBody PageHierarchyRequest pageHierarchyRequest) {
        String childIdentifier = pageHierarchyRequest.getChildIdentifier();
        String parentIdentifier = pageHierarchyRequest.getParentIdentifier();
        Integer sequence = pageHierarchyRequest.getSequence();
        if (!StringUtils.isEmpty(childIdentifier) && !StringUtils.isEmpty(parentIdentifier)) {
            var parentPage = pageRepository.findByIdentifier(parentIdentifier);
            var childPage = pageRepository.findByIdentifier(childIdentifier);

            if (childPage.isPresent() && parentPage.isPresent()) {
                SitePage parent = parentPage.get();
                SitePage child = childPage.get();

                PageHierarchyKey key = new PageHierarchyKey();
                key.setParentId(parent.getId());
                key.setChildId(child.getId());

                PageHierarchy pageHierarchy = new PageHierarchy();
                pageHierarchy.setId(key);
                pageHierarchy.setSequence(sequence);

                return pageHierarchyRepository.save(pageHierarchy);
            }
        }

        return null;
    }

//    private void fillAutoFields(SitePage page) {
//        if (page.getParentPageIdentifier() != null) {
//            var parentPage = pageRepository.findByIdentifier(page.getParentPageIdentifier());
//            parentPage.ifPresent(page::setParentPageId);
//        }
//    }

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
//                    fillAutoFields(page);

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
//                    fillAutoFields(existingPage);
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

    @GetMapping("/{pageIdentifier}/children")
    public Collection<SitePage> getChildren(@PathVariable String pageIdentifier) {
        return pageRepository.findChildren(pageIdentifier, "article");
    }

    @GetMapping("/{pageIdentifier}/pages")
    public Collection<SitePage> getPages(@PathVariable String pageIdentifier) {
        return pageRepository.findChildren(pageIdentifier, "article-page");
    }

    @GetMapping("/{pageIdentifier}/parents")
    public Collection<SitePage> getParents(@PathVariable String pageIdentifier) {
        return pageRepository.findParents(pageIdentifier);
    }

    @GetMapping("/{pageIdentifier}/parent")
    public SitePage getParent(@PathVariable String pageIdentifier) {
        return getParentPage(pageIdentifier);
    }

    @GetMapping("/{pageIdentifier}/breadcrumbs")
    public Collection<SitePage> getBreadcrumbs(@PathVariable String pageIdentifier) {
        List<SitePage> hierarchy = new ArrayList<>();

        Optional<SitePage> page = pageRepository.findByIdentifier(pageIdentifier);
        if (page.isPresent()) {
            hierarchy.add(page.get());
            SitePage parent = getParentPage(pageIdentifier);
            while (parent != null) {
                hierarchy.add(parent);
                parent = getParentPage(parent.getIdentifier());
            }
            Collections.reverse(hierarchy);
        }

        return hierarchy;
    }

    private SitePage getParentPage(String pageIdentifier) {
        Collection<SitePage> parents = pageRepository.findParents(pageIdentifier);
        if (parents.size() > 0) {
            return parents.iterator().next();
        }
        return null;
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
