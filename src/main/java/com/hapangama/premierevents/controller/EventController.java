package com.hapangama.premierevents.controller;

import com.hapangama.premierevents.entity.Event;
import com.hapangama.premierevents.service.EventService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    @PostMapping
    public ResponseEntity<?> createEvent(@Valid @RequestBody Event event) {

        if (event.getDateTime().isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().body("Event date cannot be in the past");
        }

        return ResponseEntity.ok(eventService.createEvent(event));
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateEvent(@PathVariable Long id,@Valid @RequestBody Event event) {
        Event event1 = eventService.updateEvent(id, event);
        if (event1 != null) {
            return ResponseEntity.ok(event1);
        }else{
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/{id}")
    public void deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
    }


    @GetMapping
    public Page<Event> getAllEvents(Pageable pageable) {
        return eventService.getAllEvents(pageable);
    }


    @GetMapping("/{id}")
    public Event getEventById(@PathVariable Long id) {
        return eventService.getEventById(id);
    }


    @GetMapping("/{id}/attendees")
    public Set<String> getAttendees(@PathVariable Long id) {
        Event event = eventService.getEventById(id);
        if (event != null) {
            return event.getAttendees();
        }
        return Set.of();
    }

    @GetMapping("/search")
    public Page<Event> searchEvents(
            @RequestParam(required = false, defaultValue = "") String name,
            @RequestParam(required = false, defaultValue = "") String location,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            Pageable pageable
    ) {

        LocalDateTime start = (startDate != null) ? startDate : LocalDateTime.of(1970, 1, 1, 0, 0);
        LocalDateTime end = (endDate != null) ? endDate : LocalDateTime.now().plusYears(50);
        return eventService.searchEvents(name, location, start, end, pageable);
    }


    @PostMapping("/{id}/register")
    public void registerForEvent(@PathVariable Long id, @NotEmpty @RequestBody String attendeeIdentifier) {
        eventService.registerUserForEvent(id, attendeeIdentifier);
    }
}