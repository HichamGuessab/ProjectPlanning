package service.eventComponentStylizer;

public class EventComponentStylizerFiller {
    public static void fillEventComponentStylizer(EventComponentStylizer eventComponentStylizer) {
        eventComponentStylizer.addEventComponentStylizer(new CustomEventComponentStylizer());
        eventComponentStylizer.addEventComponentStylizer(new CourseEventComponentStylizer());
        eventComponentStylizer.addEventComponentStylizer(new DefaultEventComponentStylizer());
    }
}
