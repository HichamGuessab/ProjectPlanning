package entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import model.Themes;

public class Config {
    private Themes theme;
    private String userIdentifier;

    @JsonCreator
    public Config(
            @JsonProperty("theme") Themes theme,
            @JsonProperty("userIdentifier") String userIdentifier
    ) {
        this.theme = theme;
        this.userIdentifier = userIdentifier;
    }

    public Themes getTheme() {
        return theme;
    }

    public void setTheme(Themes theme) {
        this.theme = theme;
    }

    public String getUserIdentifier() {
        return userIdentifier;
    }

    public void setUserIdentifier(String userIdentifier) {
        this.userIdentifier = userIdentifier;
    }
}
