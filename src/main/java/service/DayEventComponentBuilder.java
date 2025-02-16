package service;

import controller.EventComponentController;
import entity.Event;
import javafx.animation.*;
import javafx.scene.Node;
import javafx.scene.layout.*;
import javafx.util.Duration;
import model.ViewAndController;
import service.eventComponentStylizer.EventComponentStylizer;

import java.util.*;

public class DayEventComponentBuilder {
    private int yStartCoordinate;

    /**
     * Build a gridpane will all the events of the day and add it to the given gridpane at the given coordinates
     * @param gridPane
     * @param xCoordinate
     * @param yStartCoordinate
     * @param yEndCoordinate
     * @param givenEvents
     */
    public void buildDay(GridPane gridPane, int xCoordinate, int yStartCoordinate, int yEndCoordinate, List<Event> givenEvents) {
        this.yStartCoordinate = yStartCoordinate;

        List<Event> events = new ArrayList<>();
        for(Event event : givenEvents) {
            if(!events.contains(event)) {
                events.add(event);
            }
        }

        Map<Event, Integer> eventDepths = new HashMap<>();
        for (Event event : events) {
            if(eventDepths.containsKey(event)) {
                continue;
            }
            eventDepths.put(event, -1);
        }

        for(int i=yStartCoordinate; i<=yEndCoordinate; i++) {
            setEventsHorizontalDepth(eventDepths, events, i);
        }

        int maxDepth = getMaxDepth(eventDepths);

        // Build a gridpane with as many rows as yEndCoordinate - yStartCoordinate and as many columns as maxDepth
        GridPane dayGridPane = new GridPane();
        for(int i=0; i<=maxDepth; i++) {
            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setHgrow(Priority.SOMETIMES);
            columnConstraints.setMinWidth(10);
            columnConstraints.setPrefWidth(53);
            dayGridPane.getColumnConstraints().add(columnConstraints);
        }
        for(int i=0; i<yEndCoordinate - yStartCoordinate; i++) {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setVgrow(Priority.SOMETIMES);
            rowConstraints.setMinHeight(10);
            rowConstraints.setPrefHeight(30);
            dayGridPane.getRowConstraints().add(rowConstraints);
        }

        List<AnchorPane> eventComponents = new ArrayList<>();
        for (Event event : events) {
            int eventYStartCoordinate = calculateYStartCoordinate(event.getStart());
            int eventYEndCoordinate = calculateYEndCoordinate(event.getEnd());
            int depth = eventDepths.get(event);

            try {
                ViewAndController viewAndController = ViewLoader.getViewAndController("eventComponent");
                EventComponentController eventComponentController = (EventComponentController) viewAndController.controller;
                EventComponentStylizer eventComponentStylizer = new EventComponentStylizer();
                eventComponentStylizer.applyStyleToEventComponentController(event, eventComponentController);

                AnchorPane node = (AnchorPane) viewAndController.node;
                dayGridPane.add(node, depth, eventYStartCoordinate, 1, eventYEndCoordinate - eventYStartCoordinate);
                eventComponents.add(node);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        animateEventComponents(eventComponents);
        gridPane.add(dayGridPane, xCoordinate, yStartCoordinate, 1, yEndCoordinate - yStartCoordinate);
    }

    /**
     * Animate the event components
     * @param eventComponents
     */
    private void animateEventComponents(List<AnchorPane> eventComponents) {
        for (AnchorPane eventComponent : eventComponents) {
            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), eventComponent);
            scaleTransition.setFromX(0);
            scaleTransition.setToX(1);
            scaleTransition.play();
        }
    }

    /**
     * Get the y start coordinates of the event in the eventDepths list
     * @param event
     * @param eventDepths
     * @return y start coordinates
     */
    private int getYStartCoordinatesFromEventList(Event event, List<List<Event>> eventDepths) {
        for(int i=yStartCoordinate; i<eventDepths.size(); i++) {
            if(eventDepths.get(i).contains(event)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Get the y end coordinates of the event in the eventDepths list
     * @param event
     * @param eventDepths
     * @return y end coordinates
     */
    private int getYEndCoordinatesFromEventList(Event event, List<List<Event>> eventDepths) {
        for(int i=eventDepths.size()-1; i>=yStartCoordinate; i--) {
            if(eventDepths.get(i).contains(event)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Get the maximum depth of the events
     * @param eventDepths
     * @return the maximum depth
     */
    private int getMaxDepth(Map<Event, Integer> eventDepths) {
        int maxDepth = 0;
        for (Map.Entry<Event, Integer> entry : eventDepths.entrySet()) {
            if(entry.getValue() > maxDepth) {
                maxDepth = entry.getValue();
            }
        }
        return maxDepth;
    }

    /**
     * Set the horizontal depth of the events
     * @param eventDepths
     * @param events
     * @param yCoordinate
     */
    private void setEventsHorizontalDepth(Map<Event, Integer> eventDepths, List<Event> events, int yCoordinate) {
        for (Event event : events) {
            if(calculateYStartCoordinate(event.getStart()) <= yCoordinate && calculateYEndCoordinate(event.getEnd()) > yCoordinate) {
                if(eventDepths.get(event) == -1) {
                    int depth = 0;
                    while(!depthAvailable(event, eventDepths, yCoordinate, depth)) {
                        depth++;
                    }
                    eventDepths.put(event, depth);
                }
            }
        }
    }

    /**
     * Check if the depth is available for the event
     * @param event
     * @param eventDepths
     * @param yCoordinate
     * @param depth
     * @return true if the depth is available, false otherwise
     */
    private boolean depthAvailable(Event event, Map<Event, Integer> eventDepths, int yCoordinate, int depth) {
        if(eventDepths.get(event) != -1) {
            return false;
        }

        for (Map.Entry<Event, Integer> eventEntry : eventDepths.entrySet()) {
            if(eventEntry.getKey() == event) {
                continue;
            }
            if(calculateYStartCoordinate(eventEntry.getKey().getStart()) <= yCoordinate && calculateYEndCoordinate(eventEntry.getKey().getEnd()) > yCoordinate) {
                if(eventEntry.getValue() == depth) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Calculate the y start coordinate of the event
     * @param date
     * @return the y start coordinate
     */
    private int calculateYStartCoordinate(Date date) {
        int startHour = date.getHours();
        if(startHour < 8) {
            startHour = 8;
        }
        return ((startHour - 8) * 2 + 2) + calculateAdjustment(date.getMinutes());
    }

    /**
     * Calculate the y end coordinate of the event
     * @param date
     * @return the y end coordinate
     */
    private int calculateYEndCoordinate(Date date) {
        int endHour = date.getHours();
        if(endHour > 20 || endHour < 8) {
            endHour = 19;
        }
        return ((endHour - 8) * 2 + 2) + calculateAdjustment(date.getMinutes());
    }

    /**
     * Calculate the adjustment of the event depending on the minutes
     * @param minutes
     * @return the adjustment
     */
    private int calculateAdjustment(int minutes) {
        int adjustment = 0;
        if(minutes > 15 && minutes < 45) {
            adjustment = 1;
        }
        if(minutes >= 45) {
            adjustment = 2;
        }
        return adjustment;
    }
}
