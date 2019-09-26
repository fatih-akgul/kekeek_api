package com.kekeek.api.model;

import lombok.Data;

@Data
public class PageHierarchyRequest {
    private String childIdentifier;
 
    private String parentIdentifier;
 
    private Integer sequence;

    private Boolean primary;
}