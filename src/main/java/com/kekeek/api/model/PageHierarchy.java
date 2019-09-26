package com.kekeek.api.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class PageHierarchy {

    @EmbeddedId
    PageHierarchyKey id;
 
    @ManyToOne
    @MapsId("child_id")
    @JoinColumn(name = "child_id")
    SitePage child;

    @ManyToOne
    @MapsId("parent_id")
    @JoinColumn(name = "parent_id")
    SitePage parent;
 
    private Integer sequence;

    @Column(name = "is_primary")
    private Boolean primary = true;
}
