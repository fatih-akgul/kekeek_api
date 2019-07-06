package com.kekeek.api.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Length;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.util.Collection;
import java.util.HashSet;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_page_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private SitePage parentPageId;

    @Column(name = "image")
    @Length(max = 255)
    private String image;

    @Column(name = "image_description")
    private String imageDescription;
}
