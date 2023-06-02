package ru.example.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.example.filmorate.controller.UserController;
import ru.example.filmorate.exception.NoSuchUserException;
import ru.example.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();
    private static long count = 1;
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public User create(User user) {
        if (user.getName().isBlank()) user.setName(user.getLogin());
        user.setId(count++);
        users.put(user.getId(), user);
        log.info("Has been created: " + user);
        return user;
    }

    @Override
    public User edit(User user, long id) throws Exception {
        if (user.getName().isBlank()) user.setName(user.getLogin());
        if (users.containsKey(id) && (user.getId() == 0 || user.getId() == id)) {
            users.put(id, user);
            log.info("Has been edited: " + user);
        }
        else if (user.getId() != 0 && user.getId() != id) {
            log.info("The ID in request body doesn't match the ID in the path");
            throw new Exception("The ID in request body must match the ID in the path");
        }
        else {
            log.info("NoSuchUserException, id " + id);
            throw new NoSuchUserException("User with required id is not found");
        }
        return user;
    }

    public User getUser(long id) throws NoSuchUserException {
        if (!users.containsKey(id)) throw new NoSuchUserException("User with required id is not found");
        return users.get(id);
    }

    @Override
    public Collection<User> getUsers(Collection<Long> friendsId) {
        return friendsId.stream().map(users::get).toList();
    }
}
