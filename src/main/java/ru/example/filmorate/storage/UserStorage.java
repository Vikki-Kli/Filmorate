package ru.example.filmorate.storage;

import ru.example.filmorate.exception.NoSuchUserException;
import ru.example.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {

    Collection<User> findAll();
    User create(User user);
    User edit(User user, long id);
    User getUser(long id);
    Collection<User> getUsers(Collection<Long> idCollection);
    void deleteUser(long id);
    void deleteFriend(long userId, long friendId);
    void addFriend(long userId, long friendId);
    Collection<User> getFriends(long id);
    Collection<User> getMutualFriends(long id1, long id2);
}
