package service.monthEventComponentStylizer;

import controller.MonthEventComponentController;
import entity.Event;

public interface MonthEventComponentStylizerInterface {
    public void applyStyleToEventComponentController(Event event, MonthEventComponentController monthEventComponentController);
    public boolean supportsEvent(Event event);
    public MonthEventComponentStylizerInterface getNextEventComponentStylizer();
    public void setNextEventComponentStylizer(MonthEventComponentStylizerInterface nextEventComponentStylizer);
    public void addEventComponentStylizer(MonthEventComponentStylizerInterface eventComponentStylizer);
}
