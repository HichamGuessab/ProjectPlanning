package service;

import entity.CourseEvent;
import entity.Event;
import model.EventType;
import model.ViewModes;
import net.fortuna.ical4j.filter.Filter;
import net.fortuna.ical4j.filter.predicate.PeriodRule;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.Period;

import java.lang.reflect.Method;
import java.util.*;

public class CalendarFilterer {
    /**
     * Get all the events for the week of the year
     * @param calendar
     * @param weekOfYear
     * @return List of events
     */
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

    /**
     * Get all the events for the day of the year
     * @param calendar
     * @param dayOfYear
     * @return List of events
     */
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

    /**
     * Get all the events for the month of the year
     * @param calendar
     * @param monthOfYear
     * @return List of events
     */
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

    /**
     * Get all the course events
     * @param calendar
     * @return List of course events
     */
    public static List<CourseEvent> getAllCourseEvents(Calendar calendar) {
        Filter filter = new Filter();

        List<Event> events = componentsToEvents(filter.filter(calendar.getComponents(Component.VEVENT)));
        List<CourseEvent> courseEvents = new ArrayList<>();
        for (Event event : events) {
            if(event instanceof CourseEvent) {
                courseEvents.add((CourseEvent) event);
            }
        }
        return courseEvents;
    }

    private static List<Event> getEvents(Calendar calendar, java.util.Calendar currentDay, Date time) {
        Period period = new Period(new DateTime(currentDay.getTime()), new DateTime(time));
        Filter filter = new Filter(new PeriodRule(period));

        Collection<Component> eventsOfTheDayCollection = filter.filter(calendar.getComponents(Component.VEVENT));
        return componentsToEvents(eventsOfTheDayCollection);
    }

    /**
     * Filter the events by the view mode and the time period
     * @param viewMode
     * @param timePeriod
     * @param events
     * @return List of events
     */
    public static List<Event> filterEventsByPeriod(ViewModes viewMode, int timePeriod, List<Event> events) {
        List<Event> filteredEvents = new ArrayList<>();
        Date startDate = new Date();
        Date endDate = new Date();
        switch (viewMode) {
            case DAILY -> {
                java.util.Calendar start = java.util.Calendar.getInstance();
                start.set(java.util.Calendar.DAY_OF_YEAR, timePeriod);
                start.set(java.util.Calendar.HOUR_OF_DAY, 1);
                start.set(java.util.Calendar.MINUTE, 0);
                start.set(java.util.Calendar.SECOND, 0);
                start.set(java.util.Calendar.MILLISECOND, 0);

                startDate = start.getTime();

                java.util.Calendar end = (java.util.Calendar) start.clone();
                end.set(java.util.Calendar.HOUR_OF_DAY, 23);
                end.set(java.util.Calendar.MINUTE, 59);
                end.set(java.util.Calendar.SECOND, 59);

                endDate = end.getTime();
            }
            case WEEKLY -> {
                java.util.Calendar start = java.util.Calendar.getInstance();
                start.set(java.util.Calendar.WEEK_OF_YEAR, timePeriod);
                start.set(java.util.Calendar.DAY_OF_WEEK, java.util.Calendar.MONDAY);
                start.set(java.util.Calendar.HOUR_OF_DAY, 1);
                start.set(java.util.Calendar.MINUTE, 0);
                start.set(java.util.Calendar.SECOND, 0);
                start.set(java.util.Calendar.MILLISECOND, 0);

                startDate = start.getTime();

                java.util.Calendar end = (java.util.Calendar) start.clone();
                end.set(java.util.Calendar.DAY_OF_WEEK, java.util.Calendar.SATURDAY);
                end.set(java.util.Calendar.HOUR_OF_DAY, 23);
                end.set(java.util.Calendar.MINUTE, 59);
                end.set(java.util.Calendar.SECOND, 59);

                endDate = end.getTime();
            }
            case MONTHLY -> {
                java.util.Calendar start = java.util.Calendar.getInstance();
                start.set(java.util.Calendar.MONTH, timePeriod);
                start.set(java.util.Calendar.DAY_OF_MONTH, 1);
                start.set(java.util.Calendar.HOUR_OF_DAY, 1);
                start.set(java.util.Calendar.MINUTE, 0);
                start.set(java.util.Calendar.SECOND, 0);
                start.set(java.util.Calendar.MILLISECOND, 0);

                startDate = start.getTime();

                java.util.Calendar end = (java.util.Calendar) start.clone();
                end.add(java.util.Calendar.MONTH, 1);
                end.set(java.util.Calendar.DAY_OF_MONTH, 1);
                end.add(java.util.Calendar.DAY_OF_MONTH, -1);
                end.set(java.util.Calendar.HOUR_OF_DAY, 23);
                end.set(java.util.Calendar.MINUTE, 59);
                end.set(java.util.Calendar.SECOND, 59);

                endDate = end.getTime();
            }
        }

        for (Event event : events) {
            if(event.getStart().after(startDate) && event.getEnd().before(endDate)) {
                filteredEvents.add(event);
            }
        }
        return filteredEvents;
    }

    /**
     * Filter the events by the filters. Filters key is the name of the attribute and the value is the value of the attribute.
     * Attribute must have a getter in the CourseEvent class.
     * @param filters
     * @param events
     * @return List of events
     */
    public static List<Event> filterEvents(Map<String, String> filters, List<Event> events) {
        Class<CourseEvent> cls = CourseEvent.class;
        List<Method> methods = new ArrayList<>();
        for (Map.Entry<String, String> filter : filters.entrySet()) {
            try {
                methods.add(cls.getMethod("get" + filter.getKey().substring(0, 1).toUpperCase() + filter.getKey().substring(1)));
            } catch (NoSuchMethodException e) {
                System.err.println("No such method: " + e.getMessage());
            }
        }

        List<Event> filteredEvents = new ArrayList<>(events);

        for (Event event : events) {
            for (Method method : methods) {
                if(!methodReturnedValueCorrespondToFilter(method, event, filters)) {
                    filteredEvents.remove(event);
                }
            }
        }
        return filteredEvents;
    }

    private static boolean methodReturnedValueCorrespondToFilter(Method method, Event event, Map<String, String> filters) {
        System.out.println(method.getReturnType());
        if (method.getReturnType().equals(String.class)) {
            return stringMethodReturnedValueCorrespondToFilter(method, event, filters);
        } else if (method.getReturnType().equals(String[].class)) {
            return stringArrayMethodReturnedValueCorrespondToFilter(method, event, filters);
        } else if (method.getReturnType().equals(EventType.class)) {
            return eventTypeMethodReturnedValueCorrespondToFilter(method, event, filters);
        }
        return false;
    }

    private static boolean stringMethodReturnedValueCorrespondToFilter(Method method, Event event, Map<String, String> filters) {
        try {
            return method.invoke(event).equals(filters.get(getValueNameFromMethodName(method.getName())));
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean stringArrayMethodReturnedValueCorrespondToFilter(Method method, Event event, Map<String, String> filters) {
        try {
            return Arrays.asList((String[]) method.invoke(event)).contains(filters.get(getValueNameFromMethodName(method.getName())));
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean eventTypeMethodReturnedValueCorrespondToFilter(Method method, Event event, Map<String, String> filters) {
        try {
            return method.invoke(event).equals(EventType.valueOf(filters.get(getValueNameFromMethodName(method.getName()))));
        } catch (Exception e) {
            return false;
        }
    }

    private static String getValueNameFromMethodName(String methodName) {
        String valueName = methodName.substring(3);
        return valueName.substring(0, 1).toLowerCase() + valueName.substring(1);
    }

    private static List<Event> componentsToEvents(Collection<Component> components) {
        return components.stream().map(ComponentTransformer::componentToEvent).toList();
    }
 }
