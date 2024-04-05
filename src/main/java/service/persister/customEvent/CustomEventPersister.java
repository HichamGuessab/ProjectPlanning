package service.persister.customEvent;

import entity.CustomEvent;

public interface CustomEventPersister {
    boolean persist(CustomEvent customEvent);
}
