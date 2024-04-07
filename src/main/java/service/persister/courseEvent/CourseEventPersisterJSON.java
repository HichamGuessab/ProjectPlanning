package service.persister.courseEvent;

import com.fasterxml.jackson.databind.ObjectMapper;
import entity.CourseEvent;
import main.Main;
import service.retriever.courseEvent.CourseEventRetrieverJSON;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public class CourseEventPersisterJSON implements CourseEventPersister {
    private final String pathToJSONCourseEvents;

    public CourseEventPersisterJSON() {
        URL url = Main.class.getResource("/json/course-events.json");
        if(url == null) {
            throw new IllegalStateException("course-events.json resource not found");
        }
        this.pathToJSONCourseEvents = url.getPath();
    }

    @Override
    public boolean persist(CourseEvent courseEvent) {
        System.out.println("Persisting course event");

        CourseEventRetrieverJSON courseEventRetrieverJSON = new CourseEventRetrieverJSON();
        List<CourseEvent> courseEvents = courseEventRetrieverJSON.retrieveAll();
        courseEvents.add(courseEvent);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(new File(this.pathToJSONCourseEvents), courseEvents);
            return true;
        } catch (IOException e) {
            System.err.println("Error while persisting : "+e.getMessage());
        }
        return false;
    }
}
