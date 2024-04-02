package entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import model.UserType;

public class User {
    private String name;
    private String identifier;
    private String password;
    private UserType type;
    private String calendarUrl;

    @JsonCreator
    public User(
            @JsonProperty("id") String name,
            @JsonProperty("identifier") String identifier,
            @JsonProperty("password") String password,
            @JsonProperty("type") UserType type,
            @JsonProperty("calendarUrl") String calendarUrl) {
        this.name = name;
        this.identifier = identifier;
        this.password = password;
        this.type = type;
        this.calendarUrl = calendarUrl;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", identifier='" + identifier + '\'' +
                ", password='" + password + '\'' +
                ", type=" + type + '\'' +
                ", calendarUrl=" + calendarUrl +
                '}';
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }

    public String getCalendarUrl() {
        return calendarUrl;
    }

    public void setCalendarUrl(String calendarUrl) {
        this.calendarUrl = calendarUrl;
    }
}
