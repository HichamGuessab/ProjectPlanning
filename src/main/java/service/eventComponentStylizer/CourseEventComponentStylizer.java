package service.eventComponentStylizer;

import controller.EventComponentController;
import entity.Event;
import entity.CourseEvent;
import javafx.scene.paint.Color;
import model.EventType;

import java.util.HashMap;
import java.util.Map;

public class CourseEventComponentStylizer extends AbstractEventComponentStylizer {
    @Override
    public boolean supportsEvent(Event event) {
        return event instanceof CourseEvent;
    }

    @Override
    protected void applyStyleToEventComponentControllerWithoutChaining(
            Event event,
            EventComponentController eventComponentController
    ) {
        CourseEvent courseEvent = (CourseEvent) event;
        eventComponentController.setName(courseEvent.getSummary());
        eventComponentController.setType(courseEvent.getCourseType().name());
        eventComponentController.setLocation(courseEvent.getLocation());

        eventComponentController.setPopupName("Matière : "+courseEvent.getSummary());
        eventComponentController.setPopupType("Type : "+courseEvent.getCourseType().name());
        eventComponentController.setPopupLocation("Salle : "+courseEvent.getLocation());

        eventComponentController.addAdditionalInformation(new HashMap<>(Map.of("Enseignant", courseEvent.getTeacher()+"["+courseEvent.getTeacherEmail()+"]")));
        eventComponentController.addAdditionalInformation(new HashMap<>(Map.of("Promotions", String.join(", ", courseEvent.getPromotions()))));
        eventComponentController.addAdditionalInformation(new HashMap<>(Map.of("Formations", String.join(", ", courseEvent.getFormations()))));

        eventComponentController.setBackGroundColor(getColorByCourseType(courseEvent.getCourseType()));
    }

    private Color getColorByCourseType(EventType courseType) {
        switch (courseType) {
            case CM -> {
                return Color.rgb(255, 0, 0, 0.5);
            }
            case TD -> {
                return Color.rgb(0, 255, 0, 0.5);
            }
            case TP -> {
                return Color.rgb(0, 0, 255, 0.5);
            }
            case EVALUATION -> {
                return Color.rgb(255, 255, 0, 0.5);
            }
            default -> {
                return Color.rgb(133, 133, 133, 0.5);
            }
        }
    }
}