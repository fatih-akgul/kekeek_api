package com.kekeek.api.model;

import lombok.Data;

import javax.persistence.*;

@Data
public class PageHierarchyRequest {
    private String childIdentifier;
 
    private String parentIdentifier;
 
    private Integer sequence;
}