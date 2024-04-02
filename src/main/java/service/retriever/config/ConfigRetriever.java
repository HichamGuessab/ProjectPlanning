package service.retriever.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import entity.Config;
import main.Main;
import service.FileReader;

import java.net.URL;

public class ConfigRetriever {
    private static String getConfigFilePath() {
        URL url = Main.class.getResource("/json/config.json");
        if(url == null) {
            throw new IllegalStateException("config.json resource not found");
        }
        return url.getPath();
    }
    public static Config retrieve() {
        String json = FileReader.readFile(getConfigFilePath());
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(json, Config.class);
        } catch (JsonProcessingException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }
}
