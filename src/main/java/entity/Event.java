package entity;

import java.util.Date;

public class Event {
    private String category;
    private Date stamp;
    private Date lastModified;
    private String uid;
    private Date start;
    private Date end;
    private String summary;
    private String location;
    private String description;

    public Event(String category, Date stamp, Date lastModified, String uid, Date start, Date end, String summary, String location, String description) {
        this.category = category;
        this.stamp = stamp;
        this.lastModified = lastModified;
        this.uid = uid;
        this.start = start;
        this.end = end;
        this.summary = summary;
        this.location = location;
        this.description = description;
    }

    @Override
    public String toString() {
        return "Event{" + "\n" +
                "  category='" + category + "\n" +
                "  stamp=" + stamp + "\n" +
                "  lastModified=" + lastModified + "\n" +
                "  uid='" + uid + "\n" +
                "  start=" + start + "\n" +
                "  end=" + end + '\'' + "\n" +
                "  summary='" + summary + '\'' + "\n" +
                "  location='" + location + '\'' + "\n" +
                "  description=' \n" + description + '\'' + "\n"
                + '}';
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Date getStamp() {
        return stamp;
    }

    public void setStamp(Date stamp) {
        this.stamp = stamp;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNameBySummary() {
        String[] parts = this.summary.split(" - ");
        String namePart = parts.length > 1 ? parts[0] : this.summary;
        String cleanName = namePart.replaceAll("UCE \\d+", "").trim();
        return cleanName.substring(0, 1).toUpperCase() + cleanName.substring(1);
    }
}
