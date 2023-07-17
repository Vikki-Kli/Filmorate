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
    public User edit(User user, long id) {
        if (user.getName().isBlank()) user.setName(user.getLogin());
        if (users.containsKey(id) && (user.getId() == 0 || user.getId() == id)) {
            users.put(id, user);
            log.info("Has been edited: " + user);
        }
        else {
            log.info("NoSuchUserException, id " + id);
            throw new NoSuchUserException("User with required id is not found");
        }
        return user;
    }

    public User getUser(long id) {
        if (!users.containsKey(id)) throw new NoSuchUserException("User with required id is not found");
        return users.get(id);
    }

    @Override
    public Collection<User> getUsers(Collection<Long> friendsId) {
        return friendsId.stream().map(users::get).toList();
    }

    @Override
    public void deleteUser(long id) {
        users.remove(id);
    }

    @Override
    public void deleteFriend(long userId, long friendId) {
        User user = getUser(userId);
        User friend = getUser(friendId);
        user.deleteFriend(friendId);
        friend.editFriendshipStatus(userId);
        edit(user, userId);
        edit(friend, friendId);
    }

    @Override
    public void addFriend(long userId, long friendId) {
        User user = getUser(userId);
        User friend = getUser(friendId);
        user.addFriend(friendId);
        if (friend.getFriends().contains(userId)) {
            friend.editFriendshipStatus(userId);
            user.editFriendshipStatus(friendId);
        }
    }

    @Override
    public Collection<User> getFriends(long id) {
        return getUsers(getUser(id).getFriends());
    }

    @Override
    public Collection<User> getMutualFriends(long id1, long id2) {
        Collection<User> friends1 = getFriends(id1);
        Collection<User> friends2 = getFriends(id2);
        return friends1.stream().filter(friends2::contains).toList();
    }
}
