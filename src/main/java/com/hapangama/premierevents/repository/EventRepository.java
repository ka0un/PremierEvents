package com.hapangama.premierevents.repository;

import com.hapangama.premierevents.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface EventRepository extends JpaRepository<Event, Long> {
    @Query(value = "SELECT * FROM EVENTS e WHERE " +
            "(:name IS NULL OR :name = '' OR LOWER(e.NAME) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
            "(:location IS NULL OR :location = '' OR LOWER(e.LOCATION) LIKE LOWER(CONCAT('%', :location, '%'))) AND " +
            "(:startDate IS NULL OR e.DATE_TIME >= :startDate) AND " +
            "(:endDate IS NULL OR e.DATE_TIME <= :endDate)",
            nativeQuery = true)
    Page<Event> searchEvents(
            @Param("name") String name,
            @Param("location") String location,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable
    );

}