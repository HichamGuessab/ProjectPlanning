package service;

public class UniqueIdentifierCreator {
    public static String create() {
        return UserManager.getInstance().getCurrentUser().getIdentifier() + System.currentTimeMillis();
    }
}
