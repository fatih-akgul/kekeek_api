package com.kekeek.api.repository;

import com.kekeek.api.model.ContentPage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface ContentPageRepository extends JpaRepository<ContentPage, Long> {
    Collection<ContentPage> findByContentIdentifier(String contentIdentifier);

    Optional<ContentPage> findByIdentifier(String identifier);
}
