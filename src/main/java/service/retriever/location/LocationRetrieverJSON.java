package service.retriever.location;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import entity.Location;
import entity.User;
import main.Main;
import service.JsonReader;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class LocationRetrieverJSON implements LocationRetriever {
    private final String pathToJSONLocations;

    public LocationRetrieverJSON() {
        URL url = Main.class.getResource("/json/locations.json");
        if(url == null) {
            throw new IllegalStateException("locations.json resource not found");
        }
        this.pathToJSONLocations = url.getPath();
    }
    @Override
    public Location retrieveFromName(String name) {
        List<Location> locations = retrieveAll();
        for (Location location : locations) {
            if (location == null) {
                continue;
            }

            if (location.getName().equals(name)) {
                return location;
            }
        }
        return null;
    }

    @Override
    public List<Location> retrieveAll() {
        String json = JsonReader.readJsonFile(this.pathToJSONLocations);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<Location> locations = objectMapper.readValue(json, new TypeReference<List<Location>>() {});

            return locations;
        } catch (JsonProcessingException e) {
            System.err.println(e.getMessage());
        }
        return new ArrayList<>();
    }
}
