package service.monthEventComponentStylizer;

import controller.EventComponentController;
import controller.MonthEventComponentController;
import entity.CourseEvent;
import entity.Event;
import javafx.scene.paint.Color;
import model.EventType;

import java.util.HashMap;
import java.util.Map;

public class CourseMonthEventComponentStylizer extends AbstractMonthEventComponentStylizer {
    @Override
    public boolean supportsEvent(Event event) {
        return event instanceof CourseEvent;
    }

    @Override
    protected void applyStyleToEventComponentControllerWithoutChaining(
            Event event,
            MonthEventComponentController monthEventComponentController
    ) {
        CourseEvent courseEvent = (CourseEvent) event;
        monthEventComponentController.setName(courseEvent.getSummary());
        monthEventComponentController.setType(courseEvent.getCourseType().name());
        monthEventComponentController.setLocation(courseEvent.getLocation());

        monthEventComponentController.setPopupName("Matière : "+courseEvent.getSummary());
        monthEventComponentController.setPopupType("Type : "+courseEvent.getCourseType().name());
        monthEventComponentController.setPopupLocation("Salle : "+courseEvent.getLocation());

        monthEventComponentController.addAdditionalInformation(new HashMap<>(Map.of("Enseignant", courseEvent.getTeacher()+"["+courseEvent.getTeacherEmail()+"]")));
        monthEventComponentController.addAdditionalInformation(new HashMap<>(Map.of("Promotions", String.join(", ", courseEvent.getPromotions()))));
        monthEventComponentController.addAdditionalInformation(new HashMap<>(Map.of("Formations", String.join(", ", courseEvent.getFormations()))));

        monthEventComponentController.setBackGroundColor(getColorByCourseType(courseEvent.getCourseType()));
    }

    private Color getColorByCourseType(EventType courseType) {
        switch (courseType) {
            case CM -> {
                return Color.rgb(186, 255, 162, 0.5);
            }
            case TD -> {
                return Color.rgb(142, 255, 220, 0.5);
            }
            case TP -> {
                return Color.rgb(0, 0, 255, 0.5);
            }
            case EVALUATION -> {
                return Color.rgb(255, 0, 0, 0.5);
            }
            default -> {
                return Color.rgb(133, 133, 133, 0.4);
            }
        }
    }
}