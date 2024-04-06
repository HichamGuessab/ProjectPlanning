package service.monthEventComponentStylizer;

import controller.MonthEventComponentController;
import entity.Event;

public abstract class AbstractMonthEventComponentStylizer implements MonthEventComponentStylizerInterface {
    private MonthEventComponentStylizerInterface nextEventComponentStylizer = null;

    @Override
    public void applyStyleToEventComponentController(Event event, MonthEventComponentController monthEventComponentController) {
        if(!supportsEvent(event)) {
            if(getNextEventComponentStylizer() != null) {
                getNextEventComponentStylizer().applyStyleToEventComponentController(event, monthEventComponentController);
            } else {
                System.err.println("No event component stylizer found for event: " + event);
            }
            return;
        }
        applyStyleToEventComponentControllerWithoutChaining(event, monthEventComponentController);
    }

    protected abstract void applyStyleToEventComponentControllerWithoutChaining(
            Event event,
            MonthEventComponentController monthEventComponentController
    );

    @Override
    public MonthEventComponentStylizerInterface getNextEventComponentStylizer() {
        return nextEventComponentStylizer;
    }

    @Override
    public void setNextEventComponentStylizer(MonthEventComponentStylizerInterface nextEventComponentStylizer) {
        this.nextEventComponentStylizer = nextEventComponentStylizer;
    }

    @Override
    public void addEventComponentStylizer(MonthEventComponentStylizerInterface eventComponentStylizer) {
        MonthEventComponentStylizerInterface currentEventComponentStylizer = this;
        while (currentEventComponentStylizer.getNextEventComponentStylizer() != null) {
            currentEventComponentStylizer = currentEventComponentStylizer.getNextEventComponentStylizer();
        }
        currentEventComponentStylizer.setNextEventComponentStylizer(eventComponentStylizer);
    }
}
