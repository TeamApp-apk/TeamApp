package com.example.demo.Event;
import com.example.demo.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/event")
public class EventController {
    private final EventService eventService = new EventService();

    @GetMapping("")
    List<Event> getEvents() {
        return eventService.getEvents();
    }

    @GetMapping("/{id}")
    Event getEventById(@PathVariable int id) {
        return eventService.getEventById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/addEvent")
    public void addEvent(@RequestBody Event event) {
        eventService.addEvent(event);
    }

    @DeleteMapping("/removeEvent/{id}")
    public void removeEvent(@PathVariable int id) {
        eventService.removeEvent(id);
    }

    @PostMapping("/addParticipant/{eventId}")
    public boolean addParticipantToEvent(@PathVariable int eventId, @RequestBody User participant) {
        Event event = eventService.getEventById(eventId);
        return eventService.addParticipantToEvent(participant, event);
    }

    @DeleteMapping("/removeParticipant/{eventId}")
    public boolean removeParticipantFromEvent(@PathVariable int eventId, @RequestBody User participant) {
        Event event = eventService.getEventById(eventId);
        return eventService.removeParticipantFromEvent(participant, event);
    }

    @GetMapping("/isParticipant/{eventId}")
    public boolean isParticipantInEvent(@PathVariable int eventId, @RequestBody User participant) {
        Event event = eventService.getEventById(eventId);
        return eventService.isParticipantInEvent(participant, event);
    }
}