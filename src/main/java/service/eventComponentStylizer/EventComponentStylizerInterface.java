package service.eventComponentStylizer;

import controller.EventComponentController;
import entity.Event;

public interface EventComponentStylizerInterface {
    public void applyStyleToEventComponentController(Event event, EventComponentController eventComponentController);
    public boolean supportsEvent(Event event);
    public EventComponentStylizerInterface getNextEventComponentStylizer();
    public void setNextEventComponentStylizer(EventComponentStylizerInterface nextEventComponentStylizer);
    public void addEventComponentStylizer(EventComponentStylizerInterface eventComponentStylizer);
}
