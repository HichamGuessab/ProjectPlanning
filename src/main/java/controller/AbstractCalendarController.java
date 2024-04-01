package controller;

import entity.Event;

import java.util.List;

abstract class AbstractCalendarController implements CalendarController {
    protected List<Event> events;
    protected int timePeriod;

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public void setTimePeriod(int timePeriod) {
        this.timePeriod = timePeriod;
    }
}
