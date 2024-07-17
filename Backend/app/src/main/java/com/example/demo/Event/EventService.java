package com.example.demo.Event;
import com.example.demo.user.User;
import java.util.ArrayList;

public class EventService {
    int currAmountOfEvents = 0;

    private ArrayList<Event> events = new ArrayList<>();
    public void addEvent(Event event) {
        currAmountOfEvents++;
        events.add(event);
    }
    public ArrayList<Event> getEvents() {
        return events;
    }
    public Event getEventById(int id) {
        return events.get(id);
    }
    public void removeEvent(int id) {
        events.remove(id);
    }
    public boolean addParticipantToEvent(User participant, Event event) {
        if (event.getCurrentParticipants() < event.getMaxParticipants()) {
            event.addParticipant(participant);
            event.setCurrentParticipants(event.getCurrentParticipants() + 1);
            return true;
        }
        return false;
    }
    public boolean removeParticipantFromEvent(User participant, Event event) {
        if (event.removeParticipant(participant)) {
            event.setCurrentParticipants(event.getCurrentParticipants() - 1);
            return true;
        }
        return false;
    }
    public boolean isParticipantInEvent(User participant, Event event) {
        return event.isParticipant(participant);
    }



}
