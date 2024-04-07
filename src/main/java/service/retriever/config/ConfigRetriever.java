package service.retriever.config;

import entity.Config;

import java.util.List;

public interface ConfigRetriever {
    public List<Config> retrieveAll();
    public Config retrieveByUserIdentifier(String userIdentifier);
}
