package service.retriever.promotion;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import entity.Promotion;
import main.Main;
import service.FileReader;
import service.retriever.JSONRetriever;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PromotionRetrieverJSON implements PromotionRetriever {
    private final String pathToJSONPromotions;

    public PromotionRetrieverJSON() {
        URL url = Main.class.getResource("/json/promotions.json");
        if(url == null) {
            throw new IllegalStateException("promotions.json resource not found");
        }
        this.pathToJSONPromotions = url.getPath();
    }

    @Override
    public Promotion retrieveFromName(String name) {
        List<Promotion> promotions = retrieveAll();
        for (Promotion promotion : promotions) {
            if (promotion == null) {
                continue;
            }

            if (promotion.getName().equals(name)) {
                return promotion;
            }
        }
        return null;
    }

    @Override
    public List<Promotion> retrieveAll() {
        return JSONRetriever.retrieveAll(this.pathToJSONPromotions, Promotion.class);
    }
}
