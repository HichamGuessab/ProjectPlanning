package service.persister.customEvent;

import com.fasterxml.jackson.databind.ObjectMapper;
import entity.CustomEvent;
import main.Main;
import service.retriever.customEvent.CustomEventRetrieverJSON;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public class CustomEventPersisterJSON implements CustomEventPersister {
    private final String pathToJSONCustomEvents;

    public CustomEventPersisterJSON() {
        URL url = Main.class.getResource("/json/custom-events.json");
        if(url == null) {
            throw new IllegalStateException("custom-events.json resource not found");
        }
        this.pathToJSONCustomEvents = url.getPath();
    }
    @Override
    public boolean persist(CustomEvent customEvent) {
        System.out.println("Persisting custom event");

        CustomEventRetrieverJSON customEventRetrieverJSON = new CustomEventRetrieverJSON();
        List<CustomEvent> customEvents = customEventRetrieverJSON.retrieveAll();
        customEvents.add(customEvent);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(new File(this.pathToJSONCustomEvents), customEvents);
            return true;
        } catch (IOException e) {
            System.err.println("Error while persisting : "+e.getMessage());
        }
        return false;
    }
}
