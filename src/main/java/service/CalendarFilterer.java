package service;

import entity.Event;
import net.fortuna.ical4j.filter.Filter;
import net.fortuna.ical4j.filter.predicate.PeriodRule;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.Period;

import java.net.URL;
import java.util.Collection;
import java.util.List;

public class CalendarFilterer {
    public static List<Event> getCurrentWeekEvents(Calendar calendar) {
        if(calendar == null) {
            return List.of();
        }

        java.util.Calendar currentWeekStart = java.util.Calendar.getInstance();
        currentWeekStart.set(java.util.Calendar.DAY_OF_WEEK, java.util.Calendar.MONDAY);
        currentWeekStart.set(java.util.Calendar.HOUR_OF_DAY, 0);
        currentWeekStart.set(java.util.Calendar.MINUTE, 0);
        currentWeekStart.set(java.util.Calendar.SECOND, 0);
        currentWeekStart.set(java.util.Calendar.MILLISECOND, 0);

        java.util.Calendar currentWeekEnd = (java.util.Calendar) currentWeekStart.clone();
        currentWeekEnd.set(java.util.Calendar.DAY_OF_WEEK, 7);

        Period period = new Period(new DateTime(currentWeekStart.getTime()), new DateTime(currentWeekEnd.getTime()));
        Filter filter = new Filter(new PeriodRule(period));

        Collection<Component> eventsOfTheWeekCollection = filter.filter(calendar.getComponents(Component.VEVENT));
        return componentsToEvents(eventsOfTheWeekCollection);
    }

    private static List<Event> componentsToEvents(Collection<Component> components) {
        return components.stream().map(ComponentTransformer::componentToEvent).toList();
    }
 }
