package com.hapangama.premierevents.repository;

import com.hapangama.premierevents.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface EventRepository extends JpaRepository<Event, Long> {
    Page<Event> findByNameContainingIgnoreCaseOrLocationContainingIgnoreCaseAndDateTimeBetween(
            String name,
            String location,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Pageable pageable
    );
}