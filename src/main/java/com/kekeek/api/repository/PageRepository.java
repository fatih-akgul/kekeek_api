package com.kekeek.api.repository;

import com.kekeek.api.model.SitePage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface PageRepository extends JpaRepository<SitePage, Long> {
    Optional<SitePage> findByIdentifier(String identifier);

    boolean existsByIdentifier(String identifier);

    @Query("SELECT c FROM SitePage p JOIN p.parents pc JOIN pc.child c WHERE p.identifier = :identifier AND c.contentType = :contentType ORDER BY pc.sequence")
    Collection<SitePage> findChildren(@Param("identifier") String identifier, @Param("contentType") String contentType);

    @Query("SELECT p FROM SitePage c JOIN c.children pc JOIN pc.parent p WHERE c.identifier = :identifier ORDER BY pc.sequence")
    Collection<SitePage> findParents(@Param("identifier") String identifier);

    @Query("SELECT p FROM SitePage c JOIN c.children pc JOIN pc.parent p WHERE c.identifier = :identifier AND pc.primary = true ORDER BY pc.sequence")
    Optional<SitePage> findPrimaryParent(String identifier);
}
