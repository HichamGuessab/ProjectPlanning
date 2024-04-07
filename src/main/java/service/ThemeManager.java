package service;

import controller.ThemeApplyer;
import entity.Config;
import javafx.scene.paint.Color;
import model.DarkModeColors;
import model.LightModeColors;
import model.Themes;
import service.persister.config.ConfigPersister;
import service.persister.config.ConfigPersisterJSON;
import service.retriever.config.ConfigRetriever;
import service.retriever.config.ConfigRetrieverJSON;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThemeManager {
    private static ThemeManager INSTANCE = null;
    private List<ThemeApplyer> themeApplyers = new ArrayList<>();

    private final Map<DarkModeColors, Color> darkModeColorsMap = new HashMap<>();
    private final Map<LightModeColors, Color> lightModeColorsMap = new HashMap<>();
    private Themes currentTheme = Themes.LIGHT;
    private UserManager userManager = UserManager.getInstance();

    private ThemeManager() {
        darkModeColorsMap.put(DarkModeColors.COLOR1, Color.rgb(133, 133, 133));
        darkModeColorsMap.put(DarkModeColors.COLOR2, Color.rgb(100, 100, 100));
        darkModeColorsMap.put(DarkModeColors.COLOR3, Color.rgb(66, 66, 66));
        darkModeColorsMap.put(DarkModeColors.COLOR4, Color.rgb(33, 33, 33));
        darkModeColorsMap.put(DarkModeColors.COLOR5, Color.rgb(0, 0, 0));

        lightModeColorsMap.put(LightModeColors.COLOR1, Color.rgb(255, 255, 255));
        lightModeColorsMap.put(LightModeColors.COLOR2, Color.rgb(220, 220, 220));
        lightModeColorsMap.put(LightModeColors.COLOR3, Color.rgb(200, 200, 200));
        lightModeColorsMap.put(LightModeColors.COLOR4, Color.rgb(180, 180, 180));
        lightModeColorsMap.put(LightModeColors.COLOR5, Color.rgb(160, 160, 160));

        updateCurrentThemeByCurrentUser();
    }

    public static ThemeManager getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new ThemeManager();
        }
        return INSTANCE;
    }

    public void updateCurrentThemeByCurrentUser() {
        if(userManager.getCurrentUser() != null) {
            ConfigRetriever configRetriever = new ConfigRetrieverJSON();
            Config config = configRetriever.retrieveByUserIdentifier(userManager.getCurrentUser().getIdentifier());
            if(config != null) {
                currentTheme = config.getTheme();
            }
        }
        applyTheme(currentTheme);
    }

    public Color getColorForDarkMode(DarkModeColors darkModeColor) {
        return darkModeColorsMap.get(darkModeColor);
    }

    public Color getColorForLightMode(LightModeColors lightModeColor) {
        return lightModeColorsMap.get(lightModeColor);
    }

    public void registerThemeApplyer(ThemeApplyer themeApplyer) {
        themeApplyers.add(themeApplyer);
        themeApplyer.applyTheme(getcurrentColors(), currentTheme);
    }

    public void removeThemeApplyer(ThemeApplyer themeApplyer) {
        themeApplyers.remove(themeApplyer);
    }

    public void applyTheme(Themes theme) {
        currentTheme = theme;

        if(userManager.getCurrentUser() != null) {
            ConfigRetriever configRetriever = new ConfigRetrieverJSON();
            Config config = configRetriever.retrieveByUserIdentifier(userManager.getCurrentUser().getIdentifier());
            if(config == null) {
                config = new Config(currentTheme, userManager.getCurrentUser().getIdentifier());
            }
            config.setTheme(theme);
            ConfigPersister configPersister = new ConfigPersisterJSON();
            configPersister.persist(config);
        }

        Color[] colors = getcurrentColors();
        for(ThemeApplyer themeApplyer : themeApplyers) {
            themeApplyer.applyTheme(colors, currentTheme);
        }
    }

    public Color[] getcurrentColors() {
        Color color1;
        Color color2;
        Color color3;
        Color color4;
        Color color5;
        switch (currentTheme) {
            case LIGHT:
                color1 = getColorForLightMode(LightModeColors.COLOR1);
                color2 = getColorForLightMode(LightModeColors.COLOR2);
                color3 = getColorForLightMode(LightModeColors.COLOR3);
                color4 = getColorForLightMode(LightModeColors.COLOR4);
                color5 = getColorForLightMode(LightModeColors.COLOR5);
                break;
            case DARK:
                color1 = getColorForDarkMode(DarkModeColors.COLOR1);
                color2 = getColorForDarkMode(DarkModeColors.COLOR2);
                color3 = getColorForDarkMode(DarkModeColors.COLOR3);
                color4 = getColorForDarkMode(DarkModeColors.COLOR4);
                color5 = getColorForDarkMode(DarkModeColors.COLOR5);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + currentTheme);
        }
        return new Color[]{color1, color2, color3, color4, color5};
    }

    public String colorToStyleString(Color color) {
        return String.format("rgb(%d, %d, %d)", (int)(color.getRed() * 255), (int)(color.getGreen() * 255), (int)(color.getBlue() * 255));
    }

    public Themes getCurrentTheme() {
        return currentTheme;
    }
}
