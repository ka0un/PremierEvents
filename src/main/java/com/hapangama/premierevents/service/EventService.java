package com.hapangama.premierevents.service;

import com.hapangama.premierevents.entity.Event;
import com.hapangama.premierevents.repository.EventRepository;
import com.hapangama.premierevents.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.security.SecurityUtil;
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
    private final SecurityUtils securityUtils;

    public Event createEvent(Event event) {

        event.setUserId(securityUtils.getUser().getId());
        return eventRepository.save(event);
    }

    public Event updateEvent(Long id, Event updatedEvent) {

        Event old = getEventById(id);
        Integer userid = securityUtils.getUser().getId();

        if (old != null && userid != null && old.getUserId() != userid) {
            throw new RuntimeException("You are not allowed to update this event");
        }

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

        Event old = getEventById(id);
        Integer userid = securityUtils.getUser().getId();

        if (old != null && userid != null && old.getUserId() != userid) {
            throw new RuntimeException("You are not allowed to delete this event");
        }

        eventRepository.deleteById(id);
    }

    public Page<Event> getAllEvents(Pageable pageable) {
        return eventRepository.findAll(pageable);
    }

    public Event getEventById(Long id) {
        return eventRepository.findById(id).orElse(null);
    }

    public Page<Event> searchEvents(String name, String location, LocalDateTime start, LocalDateTime end, Pageable pageable) {
        return eventRepository.searchEvents(
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