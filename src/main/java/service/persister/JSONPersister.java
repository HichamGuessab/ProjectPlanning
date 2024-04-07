package service.persister;

import com.fasterxml.jackson.databind.ObjectMapper;
import entity.Promotion;
import service.retriever.JSONRetriever;
import service.retriever.promotion.PromotionRetrieverJSON;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class JSONPersister {
    public static <T> boolean persist(String filePath, T object, Class<T> clazz) {
        List<T> objects = JSONRetriever.retrieveAll(filePath, clazz);
        objects.add(object);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(new File(filePath), objects);
            return true;
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return false;
    }
}
