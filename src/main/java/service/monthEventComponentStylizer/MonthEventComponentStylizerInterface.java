package service.monthEventComponentStylizer;

import controller.MonthEventComponentController;
import entity.Event;

public interface MonthEventComponentStylizerInterface {
    /**
     * Apply the style to the event component controller. If the event component stylizer does not support the event, it will call the next event component stylizer
     * @param event
     * @param monthEventComponentController
     */
    public void applyStyleToEventComponentController(Event event, MonthEventComponentController monthEventComponentController);
    public boolean supportsEvent(Event event);
    public MonthEventComponentStylizerInterface getNextEventComponentStylizer();
    public void setNextEventComponentStylizer(MonthEventComponentStylizerInterface nextEventComponentStylizer);
    public void addEventComponentStylizer(MonthEventComponentStylizerInterface eventComponentStylizer);
}
