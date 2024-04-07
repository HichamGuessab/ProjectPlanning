package service.persister.courseEvent;

import entity.CourseEvent;

public interface CourseEventPersister {
    public boolean persist(CourseEvent courseEvent);
}
