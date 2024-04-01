package service;

import entity.Event;
import net.fortuna.ical4j.filter.Filter;
import net.fortuna.ical4j.filter.predicate.PeriodRule;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.Period;

import java.util.Collection;
import java.util.Date;
import java.util.List;

public class CalendarFilterer {
    public static List<Event> getEventsForWeekOfYear(Calendar calendar, int weekOfYear) {
        if(calendar == null) {
            return List.of();
        }

        java.util.Calendar currentWeekStart = java.util.Calendar.getInstance();
        currentWeekStart.set(java.util.Calendar.WEEK_OF_YEAR, weekOfYear);
        currentWeekStart.set(java.util.Calendar.DAY_OF_WEEK, java.util.Calendar.MONDAY);
        currentWeekStart.set(java.util.Calendar.HOUR_OF_DAY, 0);
        currentWeekStart.set(java.util.Calendar.MINUTE, 0);
        currentWeekStart.set(java.util.Calendar.SECOND, 0);

        java.util.Calendar currentWeekEnd = (java.util.Calendar) currentWeekStart.clone();
        currentWeekEnd.set(java.util.Calendar.DAY_OF_WEEK, java.util.Calendar.SATURDAY);

        return getEvents(calendar, currentWeekStart, currentWeekEnd.getTime());
    }

    public static List<Event> getEventsForDayOfYear(Calendar calendar, int dayOfYear) {
        if(calendar == null) {
            return List.of();
        }

        java.util.Calendar currentDay = java.util.Calendar.getInstance();
        currentDay.set(java.util.Calendar.DAY_OF_YEAR, dayOfYear);
        currentDay.set(java.util.Calendar.HOUR_OF_DAY, 6);
        currentDay.set(java.util.Calendar.MINUTE, 0);
        currentDay.set(java.util.Calendar.SECOND, 0);
        currentDay.set(java.util.Calendar.MILLISECOND, 0);

        java.util.Calendar nextDay = (java.util.Calendar) currentDay.clone();
        nextDay.set(java.util.Calendar.DAY_OF_YEAR, dayOfYear+1);

        return getEvents(calendar, currentDay, nextDay.getTime());
    }

    public static List<Event> getEventsForMonthOfYear(Calendar calendar, int monthOfYear) {
        if(calendar == null) {
            return List.of();
        }

        java.util.Calendar currentMonth = java.util.Calendar.getInstance();
        currentMonth.set(java.util.Calendar.MONTH, monthOfYear);
        currentMonth.set(java.util.Calendar.DAY_OF_MONTH, 1);
        currentMonth.set(java.util.Calendar.HOUR_OF_DAY, 0);
        currentMonth.set(java.util.Calendar.MINUTE, 0);
        currentMonth.set(java.util.Calendar.SECOND, 0);
        currentMonth.set(java.util.Calendar.MILLISECOND, 0);

        java.util.Calendar nextMonth = (java.util.Calendar) currentMonth.clone();
        nextMonth.add(java.util.Calendar.MONTH, 1);

        return getEvents(calendar, currentMonth, nextMonth.getTime());
    }

    private static List<Event> getEvents(Calendar calendar, java.util.Calendar currentDay, Date time) {
        Period period = new Period(new DateTime(currentDay.getTime()), new DateTime(time));
        Filter filter = new Filter(new PeriodRule(period));

        Collection<Component> eventsOfTheDayCollection = filter.filter(calendar.getComponents(Component.VEVENT));
        return componentsToEvents(eventsOfTheDayCollection);
    }

    private static List<Event> componentsToEvents(Collection<Component> components) {
        return components.stream().map(ComponentTransformer::componentToEvent).toList();
    }
 }
