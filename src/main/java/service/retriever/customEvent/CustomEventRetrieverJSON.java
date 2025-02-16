package service.retriever.customEvent;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import entity.CustomEvent;
import entity.Event;
import entity.Location;
import main.Main;
import service.FileReader;
import service.retriever.JSONRetriever;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CustomEventRetrieverJSON implements CustomEventRetriever {
    private final String pathToJSONCustomEvents;

    public CustomEventRetrieverJSON() {
        URL url = Main.class.getResource("/json/custom-events.json");
        if(url == null) {
            throw new IllegalStateException("custom-events.json resource not found");
        }
        this.pathToJSONCustomEvents = url.getPath();
    }
    @Override
    public List<CustomEvent> retrieveAll() {
        return JSONRetriever.retrieveAll(this.pathToJSONCustomEvents, CustomEvent.class);
    }

    @Override
    public List<CustomEvent> retrieveFromUserIdentifier(String userIdentifier) {
        List<CustomEvent> allEvents = retrieveAll();
        List<CustomEvent> userEvents = new ArrayList<>();
        for (CustomEvent event : allEvents) {
            if (event == null) {
                continue;
            }

            if (event.getCreatorIdentifier().equals(userIdentifier)) {
                userEvents.add(event);
            }
        }
        return userEvents;
    }
}
