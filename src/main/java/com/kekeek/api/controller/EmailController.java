package com.kekeek.api.controller;

import com.kekeek.api.model.Email;
import com.kekeek.api.repository.EmailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/email")
public class EmailController {

    private EmailRepository emailRepository;

    @Autowired
    public EmailController(EmailRepository emailRepository) {
        this.emailRepository = emailRepository;
    }

    @GetMapping
    public Page getEmails(Pageable pageable) {
        return emailRepository.findAll(pageable);
    }

    @PostMapping
    public Email postEmail(@Valid @RequestBody Email email) {
        return emailRepository.save(email);
    }
}
