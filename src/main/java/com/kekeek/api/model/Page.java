package com.kekeek.api.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.util.Collection;
import java.util.HashSet;

@Entity
@Table(name = "site_page")
@Data
@EqualsAndHashCode(callSuper = true)
public class Page extends KekeekModel {
    @Length(max=30)
    private String identifier;

    @Length(max=255)
    private String title;

    @NotBlank
    @Length(min=2, max=2)
    private String language;

    @ElementCollection
    private Collection<String> keywords = new HashSet<>();
}
