package com.example.demo.Event;
import com.example.demo.user.User;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class Event {
    private String location;
    private LocalDate date; // Corrected type
    private LocalDate endDate;
    private List<User> participants = new ArrayList<>();
    private String time;
    private SportDiscipline sportDiscipline;
    private int maxParticipants;
    private int currentParticipants;
    private String description;
    private String creator;
    enum SportDiscipline {
        FOOTBALL,
        TENNIS,
        BASKETBALL,
        VOLLEYBALL,
        CYCLING,
        SWIMMING,
        RUNNING,
        GOLF,
        YOGA,
        BILLIARD,
        BADMINTON,
        GYM
    }

    public Event(String location, String date, String time, SportDiscipline sportDiscipline, int maxParticipants,
                 int currentParticipants, String description, String creator, String endDate) {
        this.location = location;
        this.date = LocalDate.parse(date); // Correct parsing
        this.time = time;
        this.sportDiscipline = sportDiscipline;
        this.maxParticipants = maxParticipants;
        this.currentParticipants = currentParticipants;
        this.description = description;
        this.creator = creator;
        this.endDate = LocalDate.parse(endDate);
        long daysBetween = ChronoUnit.DAYS.between(this.date, this.endDate);
        if (daysBetween > 7) {
            throw new IllegalArgumentException("Twoje wydarzenie musi zakończyć się w ciągu tygodnia od rozpoczęcia");
        }
    }
    public void setDate(String date) {
        this.date = LocalDate.parse(date);
    }
    public LocalDate getDate() {
        return date;
    }
    public void setSportDiscipline(SportDiscipline sportDiscipline) {
        this.sportDiscipline = sportDiscipline;
    }
    public SportDiscipline getSportDiscipline() {
        return sportDiscipline;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public String getLocation() {
        return location;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public String getTime() {
        return time;
    }
    public void setMaxParticipants(int maxParticipants){
        this.maxParticipants = maxParticipants;
    }
    public int getMaxParticipants(){
        return maxParticipants;
    }
    public void setCurrentParticipants(int currentParticipants){
        this.currentParticipants = currentParticipants;
    }
    public int getCurrentParticipants(){
        return currentParticipants;
    }
    public void setDescription(String description){
        this.description = description;
    }
    public String getDescription(){
        return description;
    }
    public boolean addParticipant(User participant) {
        if (currentParticipants < maxParticipants && !participants.contains(participant)) {
            participants.add(participant);
            currentParticipants++;
            return true;
        }
        return false;
    }
    public boolean removeParticipant(User participant) {
        if (participants.remove(participant)) {
            currentParticipants--;
            return true;
        }
        return false;
    }
    public boolean isParticipant(User participant) {
        return participants.contains(participant);
    }
    public int getParticipantCount() {
        return participants.size();
    }
}