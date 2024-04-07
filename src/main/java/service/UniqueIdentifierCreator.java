package service;

public class UniqueIdentifierCreator {
    /**
     * Create a unique identifier from the current user identifier and the current time in milliseconds
     * @return the unique identifier
     */
    public static String create() {
        return UserManager.getInstance().getCurrentUser().getIdentifier() + System.currentTimeMillis();
    }
}
