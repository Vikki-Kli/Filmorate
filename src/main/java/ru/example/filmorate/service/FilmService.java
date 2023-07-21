package ru.example.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.example.filmorate.model.Film;
import ru.example.filmorate.model.Genre;
import ru.example.filmorate.model.Mpa;
import ru.example.filmorate.storage.FilmStorage;

import java.util.Collection;

@Service
public class FilmService {

    private FilmStorage filmStorage;

    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }
    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }
    public Film getFilm(long id) {
        return filmStorage.getFilm(id);
    }
    public Film create(Film film) {
        return filmStorage.create(film);
    }
    public Film edit(Film film, long id) {
        return filmStorage.edit(film, id);
    }
    public void addLike(long userId, long filmId) {
        filmStorage.addLike(userId, filmId);
    }
    public void removeLike(long userId, long filmId) {
        filmStorage.removeLike(userId, filmId);
    }
    public Collection<Film> getMostPopularFilms(int count) {
        return filmStorage.getMostPopularFilms(count);
    }
    public void delete(long id) {
        filmStorage.delete(id);
    }
    public Collection<Mpa> getMpa() {
        return filmStorage.getMpa();
    }
    public Collection<Genre> getGenres() {
        return filmStorage.getGenres();
    }
}
