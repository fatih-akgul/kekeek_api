package com.kekeek.api.model;

import com.kekeek.api.type.ContentStatusType;
import com.kekeek.api.type.ContentType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "content")
@Data
@EqualsAndHashCode(callSuper = true)
public class Content extends KekeekModel {
    @Length(max=255)
    private String title;

    @Column(columnDefinition = "text")
    private String description;

    @Column(columnDefinition = "text")
    private String snippet;

    @Column(name = "page_count")
    private Integer pageCount;

    @NotBlank
    @Length(min=2, max=2)
    private String language;

    @Column(name = "content_type")
    private ContentType contentType;

    private ContentStatusType status;

    @Column(name = "comment_count")
    private Integer commentCount;

    @Column(name = "like_count")
    private Integer likeCount;

    @Length(max=30)
    private String identifier;
}
