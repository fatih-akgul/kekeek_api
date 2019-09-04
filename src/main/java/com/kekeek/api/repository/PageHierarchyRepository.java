package com.kekeek.api.repository;

import com.kekeek.api.model.PageHierarchy;
import com.kekeek.api.model.PageHierarchyKey;
import com.kekeek.api.model.SitePage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PageHierarchyRepository extends JpaRepository<PageHierarchy, PageHierarchyKey> {

}
