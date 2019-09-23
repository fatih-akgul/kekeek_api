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
import javax.persistence.UniqueConstraint;

@Entity
@Table(
        name = "content",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"page_id", "identifier"})}
)
@Data
@EqualsAndHashCode(callSuper = true)
public class Content extends KekeekModel {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "page_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private SitePage page;

    @Length(max = 63)
    private String identifier;

    @Length(max = 255)
    private String title;

    @Column(columnDefinition = "text")
    private String description;

    @Column(columnDefinition = "text")
    private String snippet;

    private Integer sequence = 1;

    @Column(name = "content_text", columnDefinition = "text")
    private String contentText;

    @Length(min = 2, max = 2)
    private String language = "en";
}
