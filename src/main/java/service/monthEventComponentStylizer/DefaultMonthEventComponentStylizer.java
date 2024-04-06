package service.monthEventComponentStylizer;

import controller.MonthEventComponentController;
import entity.Event;
import javafx.scene.paint.Color;

public class DefaultMonthEventComponentStylizer extends AbstractMonthEventComponentStylizer {
    @Override
    public boolean supportsEvent(Event event) {
        return true;
    }

    @Override
    protected void applyStyleToEventComponentControllerWithoutChaining(
            Event event,
            MonthEventComponentController monthEventComponentController
    ) {
        monthEventComponentController.setName(event.getSummary());
        monthEventComponentController.setType("");
        monthEventComponentController.setLocation(event.getLocation());
        monthEventComponentController.setBackGroundColor(Color.rgb(133, 133, 133, 0.5));
    }
}
