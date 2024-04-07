package service.retriever.courseEvent;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import entity.CourseEvent;
import entity.CustomEvent;
import main.Main;
import service.FileReader;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CourseEventRetrieverJSON implements CourseEventRetriever {
    private final String pathToJSONCourseEvents;

    public CourseEventRetrieverJSON() {
        URL url = Main.class.getResource("/json/course-events.json");
        if(url == null) {
            throw new IllegalStateException("course-events.json resource not found");
        }
        this.pathToJSONCourseEvents = url.getPath();
    }

    @Override
    public List<CourseEvent> retrieveAll() {
        String json = FileReader.readFile(this.pathToJSONCourseEvents);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(json, new TypeReference<List<CourseEvent>>() {});
        } catch (JsonProcessingException e) {
            System.err.println(e.getMessage());
        }
        return new ArrayList<>();
    }

    @Override
    public List<CourseEvent> retrieveFromTeacherName(String teacherName) {
        List<CourseEvent> allEvents = retrieveAll();
        List<CourseEvent> userEvents = new ArrayList<>();
        for (CourseEvent event : allEvents) {
            if (event == null) {
                continue;
            }

            if (event.getTeacher().equals(teacherName)) {
                userEvents.add(event);
            }
        }
        return userEvents;
    }

    @Override
    public List<CourseEvent> retrieveFromLocationName(String locationName) {
        List<CourseEvent> allEvents = retrieveAll();
        List<CourseEvent> locationEvents = new ArrayList<>();
        for (CourseEvent event : allEvents) {
            if (event == null) {
                continue;
            }

            if (event.getLocation().equals(locationName)) {
                locationEvents.add(event);
            }
        }
        return locationEvents;
    }
}
