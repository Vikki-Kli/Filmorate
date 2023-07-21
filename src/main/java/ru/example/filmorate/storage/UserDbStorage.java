package ru.example.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.example.filmorate.exception.NoSuchUserException;
import ru.example.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class UserDbStorage implements UserStorage {

    private JdbcTemplate jdbcTemplate;
    private final static Logger logger = LoggerFactory.getLogger(UserDbStorage.class);

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<User> findAll() {
        String sqlUsers = "select * from users";
        return jdbcTemplate.query(sqlUsers, (rs, rowNum) -> makeUser(rs));
    }

    private User makeUser(ResultSet rs) throws SQLException {
        String sqlFriends = "select * from friends where user_id = ?";
        SqlRowSet srs = jdbcTemplate.queryForRowSet(sqlFriends, rs.getLong("id"));
        Map<Long, Boolean> friends = new HashMap<>();
        while (srs.next()) {
            friends.put(srs.getLong("friend_id"), srs.getBoolean("status"));
        }
        return new User(rs.getLong("id"),
                rs.getString("email"),
                rs.getString("login"),
                rs.getString("name"),
                rs.getDate("birthday").toLocalDate(),
                friends);
    }

    @Override
    public User create(User user) {
        String sqlCreate = "insert into users(email, login, name, birthday) values(?, ?, ?, ?)";
        jdbcTemplate.update(sqlCreate, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday());

        String sqlGet = "select * from users where login = ?";
        User userToReturn = jdbcTemplate.query(sqlGet, (rs, rowNum) -> makeUser(rs), user.getLogin()).get(0);
        logger.info("User has been created: {}", userToReturn);
        return userToReturn;
    }

    @Override
    public User edit(User user, long id) {
        getUser(id);
        String sql = "update users set email = ?, login = ?, name = ?, birthday = ? where id = ?";
        jdbcTemplate.update(sql, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), id);
        logger.info("User has been edited: {}", user);
        return getUser(id);
    }

    public void deleteUser(long id) {
        getUser(id);
        String sql = "delete from users where id = ?";
        jdbcTemplate.update(sql, id);
        logger.info("User {} has been deleted", id);
    }

    @Override
    public User getUser(long id) throws NoSuchUserException {
        String sql = "select * from users where id = ?";
        Optional<User> optionalUser = jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs), id).stream().findFirst();
        if (optionalUser.isEmpty()) throw new NoSuchUserException("User " + id + " has not been found");
        else return optionalUser.get();
    }

    private Collection<User> getUsers(Collection<Long> idCollection) {
        String sql = "select * from users where id in (" + String.join(",", Collections.nCopies(idCollection.size(), "?")) + ")";
        return jdbcTemplate.query(sql, (rs, rn) -> makeUser(rs), idCollection.toArray());
    }

    public void deleteFriend(long userId, long friendId) {
        getUser(userId);
        getUser(friendId);
        String sqlDelete = "delete from friends where user_id = ? and friend_id = ?";
        jdbcTemplate.update(sqlDelete, userId, friendId);
        String sqlEdit = "update friends set status = false where user_id = ? and friend_id = ?";
        jdbcTemplate.update(sqlEdit, friendId, userId);
        logger.info("User {} removed {} from friends", userId, friendId);
    }

    @Override
    public void addFriend(long userId, long friendId) {
        getUser(userId);
        getUser(friendId);
        String sqlCheck = "select * from friends where user_id = ? and friend_id = ?";
        if (jdbcTemplate.queryForRowSet(sqlCheck, friendId, userId).isBeforeFirst()) {
            String sqlInsertTrue = "insert into friends (user_id, friend_id, status) values (?, ?, true)";
            String sqlUpdateTrue = "update friends set status = true where user_id = ? and friend_id = ?";
            jdbcTemplate.update(sqlInsertTrue, userId, friendId);
            jdbcTemplate.update(sqlUpdateTrue, friendId, userId);
            logger.info("Users {} and {} had become friends", userId, friendId);
        }
        else {
            String sqlInsertFalse = "insert into friends (user_id, friend_id, status) values (?, ?, false)";
            jdbcTemplate.update(sqlInsertFalse, userId, friendId);
            logger.info("User {} added {} as a friend", userId, friendId);
        }
    }

    @Override
    public Collection<User> getFriends(long id) {
        String sql = "select friend_id from friends where user_id = ? and status = true";
        return getUsers(jdbcTemplate.queryForList(sql, Long.class, id));
    }

    @Override
    public Collection<User> getMutualFriends(long id1, long id2) {
        String sql = "select friend_id from friends where user_id = ? and status = true " +
                "intersect select friend_id from friends where user_id = ? and status = true";
        return getUsers(jdbcTemplate.queryForList(sql, Long.class, id1, id2));
    }
}
