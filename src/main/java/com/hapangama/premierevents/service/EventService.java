package com.hapangama.premierevents.service;

import com.hapangama.premierevents.entity.Event;
import com.hapangama.premierevents.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class EventService {

    private final EventRepository eventRepository;


    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    public Event updateEvent(Long id, Event updatedEvent) {
        Optional<Event> existing = eventRepository.findById(id);
        if (existing.isPresent()) {
            Event event = existing.get();
            event.setName(updatedEvent.getName());
            event.setDateTime(updatedEvent.getDateTime());
            event.setLocation(updatedEvent.getLocation());
            event.setDescription(updatedEvent.getDescription());
            return eventRepository.save(event);
        }
        return null;
    }

    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }

    public Page<Event> getAllEvents(Pageable pageable) {
        return eventRepository.findAll(pageable);
    }

    public Event getEventById(Long id) {
        return eventRepository.findById(id).orElse(null);
    }

    public Page<Event> searchEvents(String name, String location, LocalDateTime start, LocalDateTime end, Pageable pageable) {
        return eventRepository.findByNameContainingIgnoreCaseOrLocationContainingIgnoreCaseAndDateTimeBetween(
                name, location, start, end, pageable
        );
    }

    public void registerUserForEvent(Long eventId, String attendeeIdentifier) {
        Optional<Event> eventOptional = eventRepository.findById(eventId);
        if (eventOptional.isPresent()) {
            Event event = eventOptional.get();
            event.getAttendees().add(attendeeIdentifier);
            eventRepository.save(event);
        }else{
            throw new RuntimeException("Event not found");
        }

    }
}