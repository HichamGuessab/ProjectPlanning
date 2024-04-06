package entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.scene.paint.Color;

import java.util.Date;

public class CustomEvent extends Event {
    private String creatorIdentifier;
    private String colorHex;

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
            @JsonProperty("colorHex") String colorHex) {
        super(category, stamp, lastModified, uid, start, end, summary, location, description);
        this.creatorIdentifier = creatorIdentifier;
        this.colorHex = colorHex;
    }

    public String toString() {
        return "CustomEvent{" + "\n" +
                "  category='" + getCategory() + "\n" +
                "  stamp=" + getStamp() + "\n" +
                "  lastModified=" + getLastModified() + "\n" +
                "  uid='" + getUid() + "\n" +
                "  start=" + getStart() + "\n" +
                "  end=" + getEnd() + '\'' + "\n" +
                "  summary='" + getSummary() + '\'' + "\n" +
                "  location='" + getLocation() + '\'' + "\n" +
                "  description=' \n" + getDescription() + '\'' + "\n" +
                "  creatorIdentifier='" + creatorIdentifier + '\'' + "\n" +
                "  colorHex='" + colorHex + '\'' + "\n"
                + '}';
    }

    public String getCreatorIdentifier() {
        return creatorIdentifier;
    }

    public void setCreatorIdentifier(String creatorIdentifier) {
        this.creatorIdentifier = creatorIdentifier;
    }

    public String getColorHex() {
        return colorHex;
    }

    public void setColorHex(String colorHex) {
        this.colorHex = colorHex;
    }
}
