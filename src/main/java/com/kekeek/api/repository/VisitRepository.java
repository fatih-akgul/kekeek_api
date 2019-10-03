package com.kekeek.api.repository;

import com.kekeek.api.model.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface VisitRepository extends JpaRepository<Visit, Long> {

    @Modifying
    @Query("DELETE FROM Visit v WHERE v.createdAt < :date")
    void deleteVisitsOlderThan(@Param("date") LocalDateTime date);
}
