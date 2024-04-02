package service.persister.location;

import entity.Location;

public interface LocationPersister {
    public boolean persist(Location location);
}
