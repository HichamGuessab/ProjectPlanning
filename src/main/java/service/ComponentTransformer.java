package service;

import entity.CourseEvent;
import entity.Event;
import model.EventProperties;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.Property;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ComponentTransformer {
    /**
     * Transform a component to an event
     * @param component
     * @return Event
     */
    public static Event componentToEvent(Component component) {
        Date dtstamp = parseDatePropertyFromName(EventProperties.DTSTAMP.name(), component);
        Date lastModified = parseDatePropertyFromName(EventProperties.LAST_MODIFIED.name(), component);
        Date dtstart = parseDatePropertyFromName(EventProperties.DTSTART.name(), component);
        Date dtend = parseDatePropertyFromName(EventProperties.DTEND.name(), component);

        dtstamp = changeDateToCurrentTimeZone(dtstamp);
        lastModified = changeDateToCurrentTimeZone(lastModified);
        dtstart = changeDateToCurrentTimeZone(dtstart);
        dtend = changeDateToCurrentTimeZone(dtend);

        String categories = getPropertyValueFromName(EventProperties.CATEGORIES.name(), component);
        String uid = getPropertyValueFromName(EventProperties.UID.name(), component);
        String summary = getPropertyValueFromName(EventProperties.SUMMARY.name(), component);
        String location = getPropertyValueFromName(EventProperties.LOCATION.name(), component);
        String description = getPropertyValueFromName(EventProperties.DESCRIPTION.name(), component);

        try {
            return new CourseEvent(categories, dtstamp, lastModified, uid, dtstart, dtend, summary, location, description);
        } catch (Exception e) {
            //System.err.println("Error: " + e.getMessage());
            return new Event(categories, dtstamp, lastModified, uid, dtstart, dtend, summary, location, description);
        }
    }

    private static Date changeDateToCurrentTimeZone(Date date) {
        if(date == null) {
            return null;
        }
        date.setHours(date.getHours() + 2);
        return date;
    }

    private static String getPropertyValueFromName(String name, Component component) {
        Property property = component.getProperty(name);
        if(property == null) {
            return null;
        }
        return property.getValue();
    }

    private static Date parseDatePropertyFromName(String name, Component component) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
        String dateStr = getPropertyValueFromName(name, component);
        if(dateStr == null) {
            return null;
        }
        try {
            if(dateStr.length() == 8) {
                dateStr += "T000000Z";
            }
            return dateFormat.parse(dateStr);
        } catch (ParseException e) {
            System.err.println("Error for "+name+" date parsing to format "+dateFormat.toPattern()+" : "+e.getMessage());
        }

        dateFormat = new SimpleDateFormat("yyyyMMdd");
        try {
            System.out.println("Parsing "+name+" to format "+dateFormat.toPattern());
            return dateFormat.parse(dateStr);
        } catch (ParseException e) {
            System.err.println("Error for "+name+" date parsing to format "+dateFormat.toPattern()+" : "+e.getMessage());
        }

        return null;
    }
}
