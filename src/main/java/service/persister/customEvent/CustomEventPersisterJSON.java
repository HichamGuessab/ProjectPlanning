package service.persister.customEvent;

import com.fasterxml.jackson.databind.ObjectMapper;
import entity.CustomEvent;
import main.Main;
import service.persister.JSONPersister;
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
        return JSONPersister.persist(this.pathToJSONCustomEvents, customEvent, CustomEvent.class);
    }
}
