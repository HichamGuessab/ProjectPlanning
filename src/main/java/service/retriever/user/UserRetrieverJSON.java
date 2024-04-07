package service.retriever.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import entity.User;
import main.Main;
import service.FileReader;
import service.retriever.JSONRetriever;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserRetrieverJSON implements UserRetriever{
    private final String pathToJSONUsers;

    public UserRetrieverJSON() {
        URL url = Main.class.getResource("/json/users.json");
        if(url == null) {
            throw new IllegalStateException("users.json resource not found");
        }
        this.pathToJSONUsers = url.getPath();
    }

    @Override
    public User retrieveFromIdentifierAndPassword(String identifier, String password) {
        List<User> users = JSONRetriever.retrieveAll(this.pathToJSONUsers, User.class);

        for (User user : users) {
            if (user == null) {
                continue;
            }

            if (user.getPassword().equals(password) && user.getIdentifier().equals(identifier)) {

                return user;
            }
        }
        return null;
    }

    @Override
    public Map<String, String> retrieveUserNamesAndCalendarUrls() {
        List<User> users = JSONRetriever.retrieveAll(this.pathToJSONUsers, User.class);
        Map<String, String> calendarUrls = new HashMap<>();

        for (User user : users) {
            if (user == null) {
                continue;
            }
            calendarUrls.put(user.getName(), user.getCalendarUrl());
        }
        return calendarUrls;
    }
}
