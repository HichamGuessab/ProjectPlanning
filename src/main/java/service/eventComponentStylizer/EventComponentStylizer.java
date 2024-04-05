package service.eventComponentStylizer;

import controller.EventComponentController;
import entity.Event;

public class EventComponentStylizer extends AbstractEventComponentStylizer {
    public EventComponentStylizer() {
        EventComponentStylizerFiller.fillEventComponentStylizer(this);
    }

    @Override
    protected void applyStyleToEventComponentControllerWithoutChaining(Event event, EventComponentController eventComponentController) {
        return;
    }

    @Override
    public boolean supportsEvent(Event event) {
        return false;
    }
}
