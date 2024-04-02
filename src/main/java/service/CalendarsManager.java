package service;

import entity.Location;
import entity.Promotion;
import model.CalendarType;
import model.CalendarUrl;
import service.retriever.location.LocationRetriever;
import service.retriever.promotion.PromotionRetriever;
import service.retriever.user.UserRetriever;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CalendarsManager {
    private final Map<String, String> userCalendars = new HashMap<>(); // <name, url>
    private final Map<String, String> locationCalendars = new HashMap<>(); // <name, url>
    private final Map<String, String> promotionCalendars = new HashMap<>(); // <name, url>

    private final UserRetriever userRetriever;
    private final LocationRetriever locationRetriever;
    private final PromotionRetriever promotionRetriever;

    public CalendarsManager(
            UserRetriever userRetriever,
            LocationRetriever locationRetriever,
            PromotionRetriever promotionRetriever
    ) {
        this.userRetriever = userRetriever;
        this.locationRetriever = locationRetriever;
        this.promotionRetriever = promotionRetriever;
        updateCalendarsMaps();
    }

    public List<String> getAllCalendarNames() {
        List<String> names = new ArrayList<>();
        names.addAll(userCalendars.keySet());
        names.addAll(locationCalendars.keySet());
        names.addAll(promotionCalendars.keySet());

        return names;
    }

    public CalendarUrl getCalendarUrlAndTypeFromName(String name) {
        if(userCalendars.containsKey(name)) {
            return new CalendarUrl(userCalendars.get(name), CalendarType.USER);
        } else if(locationCalendars.containsKey(name)) {
            return new CalendarUrl(locationCalendars.get(name), CalendarType.LOCATION);
        } else if (promotionCalendars.containsKey(name)) {
            return new CalendarUrl(promotionCalendars.get(name), CalendarType.PROMOTION);
        }
        return null;
    }

    public void updateCalendarsMaps() {
        buildLocationsMap();
        buildUsersMap();
        buildPromotionsMap();
    }

    private void buildLocationsMap() {
        List<Location> locations = locationRetriever.retrieveAll();
            for(Location location : locations) {
                locationCalendars.put(location.getName(), location.getCalendarUrl());
            }
    }

    private void buildPromotionsMap() {
        List<Promotion> promotions = promotionRetriever.retrieveAll();
        for(Promotion promotion : promotions) {
            promotionCalendars.put(promotion.getName(), promotion.getCalendarUrl());
        }
    }

    private void buildUsersMap() {
        userCalendars.putAll(userRetriever.retrieveUserNamesAndCalendarUrls());
    }
}
