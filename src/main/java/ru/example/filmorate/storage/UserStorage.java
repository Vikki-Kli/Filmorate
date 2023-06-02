package ru.example.filmorate.storage;

import ru.example.filmorate.exception.NoSuchUserException;
import ru.example.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {

    Collection<User> findAll();
    User create(User user);
    User edit(User user, long id) throws Exception;
    User getUser(long id) throws NoSuchUserException;
    Collection<User> getUsers(Collection<Long> idCollection);
}
