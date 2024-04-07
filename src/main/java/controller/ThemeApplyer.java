package controller;

import javafx.scene.paint.Color;
import model.Themes;

public interface ThemeApplyer {
    void applyTheme(Color[] colors, Themes theme);
}
