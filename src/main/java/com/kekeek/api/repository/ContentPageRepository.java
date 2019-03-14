package com.kekeek.api.repository;

import com.kekeek.api.model.ContentPage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContentPageRepository extends JpaRepository<ContentPage, Long> {
//    List<ContentPage> findByContentId(Long contentId);

    List<ContentPage> findByContentIdentifier(String contentIdentifier);

    Optional<ContentPage> findByIdentifier(String identifier);
}
