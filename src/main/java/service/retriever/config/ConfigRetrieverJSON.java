package service.retriever.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import entity.Config;
import main.Main;
import service.FileReader;
import service.retriever.JSONRetriever;

import java.net.URL;
import java.util.List;

public class ConfigRetrieverJSON implements ConfigRetriever {
    private static String getConfigFilePath() {
        URL url = Main.class.getResource("/json/config.json");
        if(url == null) {
            throw new IllegalStateException("config.json resource not found");
        }
        return url.getPath();
    }

    @Override
    public List<Config> retrieveAll() {
        return JSONRetriever.retrieveAll(getConfigFilePath(), Config.class);
    }

    @Override
    public Config retrieveByUserIdentifier(String userIdentifier) {
        List<Config> configs = this.retrieveAll();
        for(Config config: configs) {
            if(config.getUserIdentifier().equals(userIdentifier)) {
                return config;
            }
        }
        return null;
    }
}
