package controller;

import entity.Event;

import java.util.List;

abstract class AbstractCalendarController implements CalendarController {
    protected List<Event> events;
    protected int timePeriod;

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    /**
     * Set the time period for the calendar
     * Day of year for daily calendar, week of year for weekly calendar, month of year for monthly calendar
     * @param timePeriod
     */
    public void setTimePeriod(int timePeriod) {
        this.timePeriod = timePeriod;
    }
}
