package ru.example.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.example.filmorate.exception.NoSuchUserException;
import ru.example.filmorate.model.User;
import ru.example.filmorate.storage.UserStorage;

import java.util.Collection;

@Service
public class UserService {

    private UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public User getUser(long id) throws NoSuchUserException {
        return userStorage.getUser(id);
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User edit(User user, long id) throws Exception {
        return userStorage.edit(user, id);
    }

    public Collection<User> getFriends(long id) throws NoSuchUserException {
        return userStorage.getUsers(userStorage.getUser(id).getFriends());
    }

    public Collection<User> getMutualFriends(long id1, long id2) throws NoSuchUserException {
        Collection<User> friends1 = getFriends(id1);
        Collection<User> friends2 = getFriends(id2);
        return friends1.stream().filter(friends2::contains).toList();
    }

    public void deleteFriend(long userId, long friendId) throws NoSuchUserException {
        userStorage.getUser(userId).deleteFriend(friendId);
        userStorage.getUser(friendId).deleteFriend(userId);
    }

    public void addFriend(long userId, long friendId) throws NoSuchUserException {
        userStorage.getUser(userId);
        userStorage.getUser(friendId);
        userStorage.getUser(userId).addFriend(friendId);
        userStorage.getUser(friendId).addFriend(userId);
    }
}
