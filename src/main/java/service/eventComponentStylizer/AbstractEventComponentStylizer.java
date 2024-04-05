package service.eventComponentStylizer;

import controller.EventComponentController;
import entity.Event;

public abstract class AbstractEventComponentStylizer implements EventComponentStylizerInterface {
    private EventComponentStylizerInterface nextEventComponentStylizer = null;

    @Override
    public void applyStyleToEventComponentController(Event event, EventComponentController eventComponentController) {
        if(!supportsEvent(event)) {
            if(getNextEventComponentStylizer() != null) {
                getNextEventComponentStylizer().applyStyleToEventComponentController(event, eventComponentController);
            } else {
                System.err.println("No event component stylizer found for event: " + event);
            }
            return;
        }
        applyStyleToEventComponentControllerWithoutChaining(event, eventComponentController);
    }

    protected abstract void applyStyleToEventComponentControllerWithoutChaining(
            Event event,
            EventComponentController eventComponentController
    );

    @Override
    public EventComponentStylizerInterface getNextEventComponentStylizer() {
        return nextEventComponentStylizer;
    }

    @Override
    public void setNextEventComponentStylizer(EventComponentStylizerInterface nextEventComponentStylizer) {
        this.nextEventComponentStylizer = nextEventComponentStylizer;
    }

    @Override
    public void addEventComponentStylizer(EventComponentStylizerInterface eventComponentStylizer) {
        EventComponentStylizerInterface currentEventComponentStylizer = this;
        while (currentEventComponentStylizer.getNextEventComponentStylizer() != null) {
            currentEventComponentStylizer = currentEventComponentStylizer.getNextEventComponentStylizer();
        }
        currentEventComponentStylizer.setNextEventComponentStylizer(eventComponentStylizer);
    }
}
