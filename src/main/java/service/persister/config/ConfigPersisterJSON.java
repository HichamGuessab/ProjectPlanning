package service.persister.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import entity.Config;
import main.Main;
import service.persister.JSONPersister;
import service.retriever.config.ConfigRetrieverJSON;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public class ConfigPersisterJSON implements ConfigPersister {
    private static String getConfigFilePath() {
        URL url = Main.class.getResource("/json/config.json");
        if(url == null) {
            throw new IllegalStateException("config.json resource not found");
        }
        return url.getPath();
    }

    @Override
    public boolean persist(Config config) {
        ConfigRetrieverJSON configRetriever = new ConfigRetrieverJSON();
        List<Config> configs = configRetriever.retrieveAll();

        configs.removeIf(c -> c.getUserIdentifier().equals(config.getUserIdentifier()));
        configs.add(config);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(new File(getConfigFilePath()), configs);
            return true;
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return false;
    }
}
