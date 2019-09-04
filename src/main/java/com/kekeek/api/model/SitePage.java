package com.kekeek.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
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
    @Length(max = 30)
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

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "parent_page_id")
//    @OnDelete(action = OnDeleteAction.CASCADE)
//    private SitePage parentPageId;

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

    @OneToMany(mappedBy = "parent")
    Set<PageHierarchy> parents;

    @OneToMany(mappedBy = "child")
    Set<PageHierarchy> children;
}
