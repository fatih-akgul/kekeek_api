package com.kekeek.api.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "email")
@Data
@EqualsAndHashCode(callSuper = true)
public class Email extends BaseModel {
    @Length(max=255)
    @Column(name = "from_email")
    private String fromEmail;

    @Length(max=255)
    @Column(name = "from_name")
    private String fromName;

    @Length(max=255)
    @Column(name = "to_email")
    private String toEmail;

    @Length(max=255)
    @Column(name = "sender_email")
    private String senderEmail;

    @Length(max=255)
    @Column(name = "sender_name")
    private String senderName;

    @Length(max=40)
    @Column(name = "sender_ip")
    private String senderIp;

    @Length(max = 1023)
    private String subject;

    @Column(columnDefinition = "text")
    private String message;

    private Boolean success = Boolean.FALSE;
}
