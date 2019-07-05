package com.kekeek.api.repository;

import com.kekeek.api.model.SitePage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SitePageRepository extends JpaRepository<SitePage, Long> {
    Optional<SitePage> findByIdentifier(String identifier);

    boolean existsByIdentifier(String identifier);

    Long findIdByIdentifier(String identifier);
}
