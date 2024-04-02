package entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Location {
    private String name;
    private String calendarUrl;

    @JsonCreator
    public Location(
            @JsonProperty("name") String name,
            @JsonProperty("calendarUrl") String calendarUrl) {
        this.name = name;
        this.calendarUrl = calendarUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCalendarUrl() {
        return calendarUrl;
    }

    public void setCalendarUrl(String calendarUrl) {
        this.calendarUrl = calendarUrl;
    }

    @Override
    public String toString() {
        return "Room{" +
                "name='" + name + '\'' +
                ", calendarUrl='" + calendarUrl + '\'' +
                '}';
    }
}
