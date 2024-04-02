package service.persister.promotion;

import entity.Promotion;

public interface PromotionPersister {
    public boolean persist(Promotion promotion);
}
