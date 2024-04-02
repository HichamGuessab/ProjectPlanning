package service.persister.promotion;

import com.fasterxml.jackson.databind.ObjectMapper;
import entity.Location;
import entity.Promotion;
import main.Main;
import service.retriever.location.LocationRetrieverJSON;
import service.retriever.promotion.PromotionRetrieverJSON;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public class PromotionPersisterJSON implements PromotionPersister {
    private final String pathToJSONPromotions;

    public PromotionPersisterJSON() {
        URL url = Main.class.getResource("/json/promotions.json");
        if(url == null) {
            throw new IllegalStateException("promotions.json resource not found");
        }
        this.pathToJSONPromotions = url.getPath();
    }

    @Override
    public boolean persist(Promotion promotion) {
        PromotionRetrieverJSON promotionRetriever = new PromotionRetrieverJSON();
        List<Promotion> promotions = promotionRetriever.retrieveAll();
        promotions.add(promotion);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(new File(this.pathToJSONPromotions), promotions);
            return true;
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return false;
    }
}
