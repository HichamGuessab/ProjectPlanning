package service;

import entity.CourseEvent;
import entity.Event;

import java.util.ArrayList;
import java.util.List;

public class FiltersManager {
    /**
     * Get all the course names from the list of events
     * @param events
     * @return List of course names
     */
    public static List<String> getCourseNames(List<CourseEvent> events) {
        List<String> courseNames = new ArrayList<>();
        for (CourseEvent event : events) {
            if(event == null || event.getName() == null) {
                continue;
            }
            if(courseNames.contains(event.getName())) {
                continue;
            }
            courseNames.add(event.getName());
        }
        courseNames.sort(String::compareTo);
        return courseNames;
    }

    /**
     * Get all the promotions from the list of events
     * @param events
     * @return List of promotions
     */
    public static List<String> getPromotions(List<CourseEvent> events) {
        List<String> groups = new ArrayList<>();
        for (CourseEvent event : events) {
            if(event == null || event.getPromotions() == null) {
                continue;
            }
            String[] promotions = event.getPromotions();
            for (String promotion : promotions) {
                if (groups.contains(promotion)) {
                    continue;
                }
                groups.add(promotion);
            }
        }
        groups.sort(String::compareTo);
        return groups;
    }

    /**
     * Get all the locations from the list of events
     * @param events
     * @return List of locations
     */
    public static List<String> getLocations(List<CourseEvent> events) {
        List<String> locations = new ArrayList<>();
        for (CourseEvent event : events) {
            if(event == null || event.getLocation() == null) {
                continue;
            }
            if(locations.contains(event.getLocation())) {
                continue;
            }
            locations.add(event.getLocation());
        }
        locations.sort(String::compareTo);
        return locations;
    }

    /**
     * Get all the course types from the list of events
     * @param events
     * @return List of course types
     */
    public static List<String> getCourseTypes(List<CourseEvent> events) {
        List<String> courseTypes = new ArrayList<>();
        for (CourseEvent event : events) {
            if(event == null || event.getCourseType() == null) {
                continue;
            }
            if(courseTypes.contains(event.getCourseType().toString())) {
                continue;
            }
            courseTypes.add(event.getCourseType().toString());
        }
        courseTypes.sort(String::compareTo);
        return courseTypes;
    }
}
