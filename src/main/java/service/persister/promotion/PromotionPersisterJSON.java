package service.persister.promotion;

import com.fasterxml.jackson.databind.ObjectMapper;
import entity.Location;
import entity.Promotion;
import main.Main;
import service.persister.JSONPersister;
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
        return JSONPersister.persist(this.pathToJSONPromotions, promotion, Promotion.class);
    }
}
