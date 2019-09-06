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

    @Query("SELECT c FROM SitePage p JOIN p.parents pc JOIN pc.child c WHERE p.identifier = :identifier ORDER BY pc.sequence")
    Collection<SitePage> findChildren(@Param("identifier") String identifier);
}
