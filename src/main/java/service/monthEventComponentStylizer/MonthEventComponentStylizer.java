package service.monthEventComponentStylizer;

import controller.MonthEventComponentController;
import entity.Event;

public class MonthEventComponentStylizer extends AbstractMonthEventComponentStylizer {
    public MonthEventComponentStylizer() {
        MonthEventComponentStylizerFiller.fillEventComponentStylizer(this);
    }

    @Override
    protected void applyStyleToEventComponentControllerWithoutChaining(Event event, MonthEventComponentController monthEventComponentController) {
        return;
    }

    @Override
    public boolean supportsEvent(Event event) {
        return false;
    }
}
