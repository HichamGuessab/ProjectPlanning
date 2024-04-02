package service.retriever.promotion;

import entity.Promotion;

import java.util.List;

public interface PromotionRetriever {
    Promotion retrieveFromName(String name);
    List<Promotion> retrieveAll();
}
