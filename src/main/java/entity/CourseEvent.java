package entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import model.EventType;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CourseEvent extends Event {
    private String name;
    private EventType courseType;
    private String teacher;
    private String[] formations;
    private String[] promotions;

    @JsonCreator
    public CourseEvent(
            @JsonProperty("category") String category,
            @JsonProperty("stamp") Date stamp,
            @JsonProperty("lastModified") Date lastModified,
            @JsonProperty("uid") String uid,
            @JsonProperty("start") Date start,
            @JsonProperty("end") Date end,
            @JsonProperty("summary") String summary,
            @JsonProperty("location") String location,
            @JsonProperty("description") String description
    ) {
        super(category, stamp, lastModified, uid, start, end, summary, location, description);
        setName();
        setType();
        setTeacher();
        setPromotions();
        setFormations(); // Se base sur les promotions pour déduire les formations
    }

    @Override
    public String toString() {
        return super.toString() + "\n" +
                "CourseEvent { \n" +
                "  courseType='" + courseType + "'\n" +
                "  teacher='" + teacher + "'\n" +
                "  formations=" + Arrays.toString(formations) + "\n" +
                "  promotions=" + Arrays.toString(promotions) + "\n" +
                "}";
    }

    private void setName() throws RuntimeException {
        String text = super.getDescription();
        Pattern pattern = Pattern.compile("Matière : (.+)");
        Matcher matcher = pattern.matcher(text);

        if (matcher.find()) {
            this.name = matcher.group(1);
            if (this.name.matches("^UCE \\d+ .*")) {
                this.name = this.name.replaceFirst("^UCE \\d+ ", "");
            }
        } else {
            this.name = "";
            throw new RuntimeException("Name not found");
        }
    }

    private void setType() {
        String text = super.getDescription();
        Pattern pattern = Pattern.compile("Type : (\\w+)");
        Matcher matcher = pattern.matcher(text);

        if (matcher.find()) {
            switch (matcher.group(1)) {
                case "TD":
                    this.courseType = EventType.TD;
                    break;
                case "TP":
                    this.courseType = EventType.TP;
                    break;
                case "Evaluation":
                    this.courseType = EventType.EVALUATION;
                    break;
                default:
                    this.courseType = EventType.CM;
                    break;
            }
        } else {
            this.courseType = EventType.OTHER;
        }
    }

    private void setTeacher() {
        String text = super.getDescription();
        Pattern pattern = Pattern.compile("Enseignant : (.*)");
        Matcher matcher = pattern.matcher(text);

        if (matcher.find()) {
            this.teacher = matcher.group(1);
        } else {
            this.teacher = "";
        }
    }

    private void setPromotions() {
        String text = super.getDescription();
        Pattern pattern = Pattern.compile("TD : (.+)");
        Matcher matcher = pattern.matcher(text);

        if (matcher.find()) {
            this.promotions = matcher.group(1).split(", ");
        } else {
            pattern = Pattern.compile("Promotions : (.+)");
            matcher = pattern.matcher(text);
            if (matcher.find()) {
                this.promotions = matcher.group(1).split(", ");
            } else {
                this.promotions = new String[0];
            }
        }
    }

    private void setFormations() {
        HashSet<String> formationsSet = new HashSet<>();
        for (String promotion : this.promotions) {
            String formation;
            if (promotion.contains("-")) {
                formation = promotion.split("-")[0] + "-" + promotion.split("-")[1];
            } else {
                formation = promotion.split(" ")[0] + "-" + promotion.split("\\(")[1].replace(")", "");
            }
            formationsSet.add(formation);
        }
        this.formations = formationsSet.toArray(new String[0]);
    }

    public String getName() {
        return name;
    }
    public EventType getCourseType() {
        return courseType;
    }

    public String getTeacher() {
        return teacher;
    }

    public String[] getFormations() {
        return formations;
    }

    public String[] getPromotions() {
        return promotions;
    }

    @JsonIgnore
    public String getTeacherEmail() {
        return "mailto:" + teacher.replaceAll(" ", ".") + "@univ-avignon.fr";
    }
}
