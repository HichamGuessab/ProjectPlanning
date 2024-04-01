package controller;

import entity.Event;

import java.util.List;

public interface CalendarController {
    void displayEvents() throws Exception;
    void setEvents(List<Event> events);
}
