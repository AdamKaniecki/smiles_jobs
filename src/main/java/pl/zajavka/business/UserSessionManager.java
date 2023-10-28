package pl.zajavka.business;

import org.springframework.stereotype.Component;
import pl.zajavka.domain.User;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserSessionManager {
    private final Map<String, User> loggedInUsers = new HashMap<>();

    public void registerLoggedInUser(String sessionId, User user) {
        loggedInUsers.put(sessionId, user);
    }

    public void unregisterLoggedInUser(String sessionId) {
        loggedInUsers.remove(sessionId);
    }

    public User getLoggedInUser(String sessionId) {
        return loggedInUsers.get(sessionId);
    }
}