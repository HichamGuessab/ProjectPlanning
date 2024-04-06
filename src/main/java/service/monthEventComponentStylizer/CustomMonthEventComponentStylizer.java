package service.monthEventComponentStylizer;

import controller.MonthEventComponentController;
import entity.CustomEvent;
import entity.Event;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Map;

public class CustomMonthEventComponentStylizer extends AbstractMonthEventComponentStylizer {
    @Override
    public boolean supportsEvent(Event event) {
        return event instanceof CustomEvent;
    }

    @Override
    protected void applyStyleToEventComponentControllerWithoutChaining(
            Event event,
            MonthEventComponentController monthEventComponentController
    ) {
        CustomEvent customEvent = (CustomEvent) event;
        monthEventComponentController.setName(customEvent.getSummary());
        monthEventComponentController.setType("Personnel");
        monthEventComponentController.setLocation(customEvent.getLocation());

        monthEventComponentController.addAdditionalInformation(new HashMap<>(Map.of("Description", customEvent.getDescription())));
        monthEventComponentController.setBackGroundColor(Color.web(customEvent.getColorHex()));
    }
}