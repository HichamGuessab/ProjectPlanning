package service;

import entity.User;
import service.retriever.user.UserRetriever;
import service.retriever.user.UserRetrieverJSON;

public class UserManager {
    private static UserManager INSTANCE;
    private User currentUser;
    private UserRetriever userRetriever = new UserRetrieverJSON();

    private UserManager() {

    }

    public static UserManager getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new UserManager();
        }
        return INSTANCE;
    }

    /**
     * Set the user retriever that will be used to retrieve the user. The default user retriever is UserRetrieverJSON
     * @param userRetriever
     */
    public void setUserRetriever(UserRetriever userRetriever) {
        if(userRetriever == null) {
            throw new IllegalArgumentException("UserRetriever cannot be null");
        }
        this.userRetriever = userRetriever;
    }

    /**
     * Authenticate the user with the given identifier and password
     * @param identifier the identifier of the user
     * @param password the password of the user
     * @return true if the user is authenticated, false otherwise
     */
    public boolean authenticate(String identifier, String password) {
        User user = userRetriever.retrieveFromIdentifierAndPassword(identifier, password);
        this.currentUser = user;
        return user != null;
    }

    /**
     * Get the current user
     * @return the current user
     */
    public User getCurrentUser() {
        return this.currentUser;
    }

    public void disconnect() {
        this.currentUser = null;
    }
}
