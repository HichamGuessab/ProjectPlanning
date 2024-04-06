package service.monthEventComponentStylizer;

public class MonthEventComponentStylizerFiller {
    public static void fillEventComponentStylizer(MonthEventComponentStylizer eventComponentStylizer) {
        eventComponentStylizer.addEventComponentStylizer(new CustomMonthEventComponentStylizer());
        eventComponentStylizer.addEventComponentStylizer(new CourseMonthEventComponentStylizer());
        eventComponentStylizer.addEventComponentStylizer(new DefaultMonthEventComponentStylizer());
    }
}
