package service;

import controller.EventComponentController;
import entity.Event;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import model.ViewAndController;
import service.eventComponentStylizer.EventComponentStylizer;

import java.util.*;

public class DayEventComponentBuilder {
    private int yStartCoordinate;
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
        for(Event event : events) {
            int eventYStartCoordinate = calculateYStartCoordinate(event.getStart());
            int eventYEndCoordinate = calculateYEndCoordinate(event.getEnd());
            int depth = eventDepths.get(event);

            try {
                ViewAndController viewAndController = ViewLoader.getViewAndController("eventComponent");
                EventComponentController eventComponentController = (EventComponentController) viewAndController.controller;
                EventComponentStylizer eventComponentStylizer = new EventComponentStylizer();
                eventComponentStylizer.applyStyleToEventComponentController(event, eventComponentController);

                dayGridPane.add(viewAndController.node, depth, eventYStartCoordinate, 1, eventYEndCoordinate - eventYStartCoordinate);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        gridPane.add(dayGridPane, xCoordinate, yStartCoordinate, 1, yEndCoordinate - yStartCoordinate);
    }

    private int getYStartCoordinatesFromEventList(Event event, List<List<Event>> eventDepths) {
        for(int i=yStartCoordinate; i<eventDepths.size(); i++) {
            if(eventDepths.get(i).contains(event)) {
                return i;
            }
        }
        return -1;
    }

    private int getYEndCoordinatesFromEventList(Event event, List<List<Event>> eventDepths) {
        for(int i=eventDepths.size()-1; i>=yStartCoordinate; i--) {
            if(eventDepths.get(i).contains(event)) {
                return i;
            }
        }
        return -1;
    }

    private int getMaxDepth(Map<Event, Integer> eventDepths) {
        int maxDepth = 0;
        for (Map.Entry<Event, Integer> entry : eventDepths.entrySet()) {
            if(entry.getValue() > maxDepth) {
                maxDepth = entry.getValue();
            }
        }
        return maxDepth;
    }

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

    private int calculateYStartCoordinate(Date date) {
        int startHour = date.getHours();
        if(startHour < 8) {
            startHour = 8;
        }
        return ((startHour - 8) * 2) + calculateAdjustment(date.getMinutes());
    }

    private int calculateYEndCoordinate(Date date) {
        int endHour = date.getHours();
        if(endHour > 20 || endHour < 8) {
            endHour = 19;
        }
        return ((endHour - 8) * 2) + calculateAdjustment(date.getMinutes());
    }

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
