package com.kekeek.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "page")
@Data
@EqualsAndHashCode(callSuper = true)
public class SitePage extends KekeekModel {
    @Length(max = 63)
    @Column(unique = true)
    private String identifier;

    @NotBlank
    @Length(min = 2, max = 2)
    private String language = "en";

    @Length(max = 255)
    private String title;

    @Column(columnDefinition = "text")
    private String description;

    @Column(columnDefinition = "text")
    private String snippet;

    @Column(name = "page_count")
    private Integer pageCount = 1;

    @Column(name = "content_type")
    private String contentType = "page";

    private String status = "active";

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "page_keywords", joinColumns = @JoinColumn(name = "page_id"))
    private Collection<String> keywords = new HashSet<>();

    @Column(name = "comment_count")
    private Integer commentCount = 0;

    @Column(name = "like_count")
    private Integer likeCount = 0;

    @Transient // Exclude field
    @JsonInclude // Still include in Json
    private String parentPageIdentifier;

    @Transient // Exclude field
    @JsonInclude // Still include in Json
    private Integer sequenceUnderParent = 1;

    @Column(name = "image")
    @Length(max = 255)
    private String image;

    @Column(name = "image_description")
    private String imageDescription;

    private Integer sequence = 1;

    @Column(name = "top_level")
    private Boolean topLevel = Boolean.FALSE;

    @OneToMany(mappedBy = "parent")
    @JsonIgnore
    Set<PageHierarchy> parents;

    @OneToMany(mappedBy = "child")
    @JsonIgnore
    Set<PageHierarchy> children;
}
