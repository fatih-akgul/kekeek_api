package com.kekeek.api.model;

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
@Table(name = "content_page")
@Data
@EqualsAndHashCode(callSuper = true)
public class ContentPage extends KekeekModel {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "content_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Content content;

    @Column(name = "page_number")
    private Integer pageNumber;

    @Length(max=255)
    private String title;

    @Column(name = "content_text", columnDefinition = "text")
    private String contentText;

    @Length(max=30)
    private String identifier;

    @NotBlank
    @Length(min=2, max=2)
    private String language;
}
