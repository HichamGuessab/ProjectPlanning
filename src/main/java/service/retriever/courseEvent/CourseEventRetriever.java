package service.retriever.courseEvent;

import entity.CourseEvent;

import java.util.List;

public interface CourseEventRetriever {
    public List<CourseEvent> retrieveAll();
    public List<CourseEvent> retrieveFromTeacherName(String teacherName);
    public List<CourseEvent> retrieveFromLocationName(String locationName);
}
