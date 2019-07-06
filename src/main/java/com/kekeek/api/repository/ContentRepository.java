package com.kekeek.api.repository;

import com.kekeek.api.model.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface ContentRepository extends JpaRepository<Content, Long> {
    Collection<Content> findByPageIdentifier(String sitePageIdentifier);

    Optional<Content> findByPageIdentifierAndIdentifier(String pageIdentifier, String identifier);
}
