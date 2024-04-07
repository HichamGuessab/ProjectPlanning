package service.eventComponentStylizer;

import controller.EventComponentController;
import entity.Event;

public interface EventComponentStylizerInterface {
    /**
     * Apply the style to the event component controller. If the event component stylizer does not support the event, it will call the next event component stylizer
     * @param event
     * @param eventComponentController
     */
    public void applyStyleToEventComponentController(Event event, EventComponentController eventComponentController);
    public boolean supportsEvent(Event event);
    public EventComponentStylizerInterface getNextEventComponentStylizer();
    public void setNextEventComponentStylizer(EventComponentStylizerInterface nextEventComponentStylizer);
    public void addEventComponentStylizer(EventComponentStylizerInterface eventComponentStylizer);
}
