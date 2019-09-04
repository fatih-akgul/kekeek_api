package com.kekeek.api.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
public
class PageHierarchyKey implements Serializable {
 
    @Column(name = "parent_id")
    Long parentId;
 
    @Column(name = "child_id")
    Long childId;
}