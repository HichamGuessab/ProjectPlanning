package service.eventComponentStylizer;

import controller.EventComponentController;
import entity.Event;
import javafx.scene.paint.Color;

public class DefaultEventComponentStylizer extends AbstractEventComponentStylizer {
    @Override
    public boolean supportsEvent(Event event) {
        return true;
    }

    @Override
    protected void applyStyleToEventComponentControllerWithoutChaining(
            Event event,
            EventComponentController eventComponentController
    ) {
        eventComponentController.setName(event.getSummary());
        eventComponentController.setType("");
        eventComponentController.setLocation(event.getLocation());
        eventComponentController.setBackGroundColor(Color.rgb(133, 133, 133, 0.5));
    }
}
