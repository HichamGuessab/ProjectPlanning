package service.retriever.customEvent;

import entity.CustomEvent;

import java.util.List;

public interface CustomEventRetriever {
    List<CustomEvent> retrieveAll();
    List<CustomEvent> retrieveFromUserIdentifier(String userIdentifier);
}
