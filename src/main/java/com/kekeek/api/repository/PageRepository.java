package com.kekeek.api.repository;

import com.kekeek.api.model.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PageRepository extends JpaRepository<Page, Long> {
    Optional<Page> findByIdentifier(String identifier);

    boolean existsByIdentifier(String identifier);
}
