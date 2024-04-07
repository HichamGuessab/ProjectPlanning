package service.persister.courseEvent;

import com.fasterxml.jackson.databind.ObjectMapper;
import entity.CourseEvent;
import main.Main;
import service.persister.JSONPersister;
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
        return JSONPersister.persist(this.pathToJSONCourseEvents, courseEvent, CourseEvent.class);
    }
}
