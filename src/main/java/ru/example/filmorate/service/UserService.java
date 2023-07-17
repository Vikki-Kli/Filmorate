package ru.example.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.example.filmorate.exception.NoSuchUserException;
import ru.example.filmorate.model.User;
import ru.example.filmorate.storage.UserStorage;

import java.util.Collection;

@Service
public class UserService {

    private UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Collection<User> findAll() throws NoSuchUserException {
        return userStorage.findAll();
    }

    public User getUser(long id) throws NoSuchUserException {
        return userStorage.getUser(id);
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User edit(User user, long id) throws NoSuchUserException {
        return userStorage.edit(user, id);
    }

    public Collection<User> getFriends(long id) throws NoSuchUserException {
        return userStorage.getFriends(id);
    }

    public Collection<User> getMutualFriends(long id1, long id2) throws NoSuchUserException {
        return userStorage.getMutualFriends(id1, id2);
    }

    public void deleteFriend(long userId, long friendId) throws NoSuchUserException {
        userStorage.deleteFriend(userId, friendId);
    }

    public void addFriend(long userId, long friendId) throws NoSuchUserException {
        userStorage.addFriend(userId, friendId);
    }

    public void delete(long id) throws NoSuchUserException {
        userStorage.deleteUser(id);
    }
}
