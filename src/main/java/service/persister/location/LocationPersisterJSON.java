package service.persister.location;

import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import entity.Location;
import main.Main;
import service.retriever.location.LocationRetrieverJSON;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public class LocationPersisterJSON implements LocationPersister {
    private final String pathToJSONLocations;

    public LocationPersisterJSON() {
        URL url = Main.class.getResource("/json/locations.json");
        if(url == null) {
            throw new IllegalStateException("locations.json resource not found");
        }
        this.pathToJSONLocations = url.getPath();
    }

    @Override
    public boolean persist(Location location) {
        LocationRetrieverJSON locationRetriever = new LocationRetrieverJSON();
        List<Location> locations = locationRetriever.retrieveAll();
        locations.add(location);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(new File(this.pathToJSONLocations), locations);
            return true;
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return false;
    }
}
