package service.persister.config;

import entity.Config;

public interface ConfigPersister {
    public boolean persist(Config config);
}
