package ru.example.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;
import java.util.*;

public class User {

    private long id;
    @Email
    private String email;
    @NotBlank
    private String login;
    @NotBlank
    private String name;
    @Past
    private LocalDate birthday;
    private Map<Long, Boolean> friendship = new HashMap<>();

    public User(){}

    public User(long id, String email, String login, String name, LocalDate birthday, Map<Long, Boolean> friendship) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
        this.friendship = friendship;
    }

    public Collection<Long> getFriends() {
        return friendship.entrySet().stream().filter(Map.Entry::getValue).map(Map.Entry::getKey).toList();
    }

    public long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getLogin() {
        return login;
    }

    public String getName() {
        return name;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void addFriend(long id) {
        friendship.put(id, false);
    }

    public void editFriendshipStatus(long id) {
        if (!friendship.get(id)) friendship.put(id, true);
        else friendship.put(id, false);
    }

    public void deleteFriend(long id) {
        friendship.remove(id);
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && email.equals(user.email) && login.equals(user.login) && Objects.equals(name, user.name) && birthday.equals(user.birthday);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", login='" + login + '\'' +
                ", name='" + name + '\'' +
                ", birthday=" + birthday +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, login, name, birthday);
    }
}
