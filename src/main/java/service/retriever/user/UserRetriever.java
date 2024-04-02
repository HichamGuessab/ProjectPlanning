package service.retriever.user;

import entity.User;

import java.util.List;
import java.util.Map;

public interface UserRetriever {
    User retrieveFromIdentifierAndPassword(String identifier, String password);

    /**
     * Retrieve all usernames and calendar urls
     * @return a map of usernames and calendar urls <username, url>
     */
    Map<String, String> retrieveUserNamesAndCalendarUrls();
}
