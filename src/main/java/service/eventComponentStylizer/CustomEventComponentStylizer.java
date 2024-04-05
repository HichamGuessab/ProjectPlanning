package service.eventComponentStylizer;

import controller.EventComponentController;
import entity.CustomEvent;
import entity.Event;

import java.util.HashMap;
import java.util.Map;

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

        eventComponentController.addAdditionalInformation(new HashMap<>(Map.of("Description", customEvent.getDescription())));
        eventComponentController.setBackGroundColor(customEvent.getColor());
    }
}