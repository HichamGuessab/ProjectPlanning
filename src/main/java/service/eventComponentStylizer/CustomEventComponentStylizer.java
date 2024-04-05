package service.eventComponentStylizer;

import controller.EventComponentController;
import entity.CustomEvent;
import entity.Event;

public class CustomEventComponentStylizer extends AbstractEventComponentStylizer {
    @Override
    public boolean supportsEvent(Event event) {
        return event instanceof CustomEvent;
    }

    @Override
    protected void applyStyleToEventComponentControllerWithoutChaining(
            Event event,
            EventComponentController eventComponentController
    ) {
        CustomEvent customEvent = (CustomEvent) event;
        eventComponentController.setName(customEvent.getSummary());
        eventComponentController.setType("Personnel");
        eventComponentController.setLocation(customEvent.getLocation());
        eventComponentController.setBackGroundColor(customEvent.getColor());
    }
}