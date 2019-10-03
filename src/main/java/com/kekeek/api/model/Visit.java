package com.kekeek.api.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "visit")
@Data
@EqualsAndHashCode(callSuper = true)
public class Visit extends BaseModel {
    @Length(max = 63)
    private String identifier;

    @Length(max = 255)
    private String title;

    private Integer counter = 0;
}
