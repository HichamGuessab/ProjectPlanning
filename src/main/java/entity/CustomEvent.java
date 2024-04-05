package entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.scene.paint.Color;

import java.util.Date;

public class CustomEvent extends Event {
    private String creatorIdentifier;
    private Color color;

    @JsonCreator
    public CustomEvent(
            @JsonProperty("category") String category,
            @JsonProperty("stamp") Date stamp,
            @JsonProperty("lastModified") Date lastModified,
            @JsonProperty("uid") String uid,
            @JsonProperty("start") Date start,
            @JsonProperty("end") Date end,
            @JsonProperty("summary") String summary,
            @JsonProperty("location") String location,
            @JsonProperty("description") String description,
            @JsonProperty("creatorIdentifier") String creatorIdentifier,
            @JsonProperty("color") String color) {
        super(category, stamp, lastModified, uid, start, end, summary, location, description);
        this.creatorIdentifier = creatorIdentifier;
        this.color = Color.web(color);
    }

    public String getCreatorIdentifier() {
        return creatorIdentifier;
    }

    public void setCreatorIdentifier(String creatorIdentifier) {
        this.creatorIdentifier = creatorIdentifier;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
