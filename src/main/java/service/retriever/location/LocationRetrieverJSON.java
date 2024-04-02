package service.retriever.location;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import entity.Location;
import main.Main;
import service.FileReader;

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
        String json = FileReader.readFile(this.pathToJSONLocations);
        ObjectMapper objectMapper = new ObjectMapper();
        try {

            return objectMapper.readValue(json, new TypeReference<List<Location>>() {});
        } catch (JsonProcessingException e) {
            System.err.println(e.getMessage());
        }
        return new ArrayList<>();
    }
}
