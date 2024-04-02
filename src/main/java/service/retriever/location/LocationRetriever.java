package service.retriever.location;

import entity.Location;

import java.util.List;

public interface LocationRetriever {
    Location retrieveFromName(String name);
    List<Location> retrieveAll();
}
