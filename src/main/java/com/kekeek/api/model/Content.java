package com.kekeek.api.model;

import com.kekeek.api.type.ContentStatusType;
import com.kekeek.api.type.ContentType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "content")
@Data
@EqualsAndHashCode(callSuper = true)
public class Content extends KekeekModel {
    @Length(max = 255)
    private String title;

    @Column(columnDefinition = "text")
    private String description;

    @Column(columnDefinition = "text")
    private String snippet;

    @Column(name = "page_count")
    private Integer pageCount;

    @NotBlank
    @Length(min = 2, max = 2)
    private String language;

    @Column(name = "content_type")
    private ContentType contentType;

    private ContentStatusType status;

    @Column(name = "comment_count")
    private Integer commentCount;

    @Column(name = "like_count")
    private Integer likeCount;

    @Length(max = 30)
    private String identifier;

    @Column(name = "content_location")
    private String contentLocation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_content_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Content parentContent;

    @Column(name = "img_full")
    @Length(max = 255)
    private String fullImage;

    @Column(name = "img_thumbnail")
    @Length(max = 255)
    private String thumbnailImage;

    @Column(name = "img_description")
    private String imageDescription;
}
