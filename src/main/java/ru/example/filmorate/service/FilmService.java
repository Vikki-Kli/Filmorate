package ru.example.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.example.filmorate.exception.NoSuchFilmException;
import ru.example.filmorate.exception.ValidationFilmException;
import ru.example.filmorate.model.Film;
import ru.example.filmorate.model.FilmComparatorForLikes;
import ru.example.filmorate.storage.FilmStorage;

import java.util.Collection;

@Service
public class FilmService {

    private FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film getFilm(long id) throws NoSuchFilmException {
        return filmStorage.getFilm(id);
    }

    public Film create(Film film) throws ValidationFilmException {
        return filmStorage.create(film);
    }

    public Film edit(Film film, long id) throws Exception {
        return filmStorage.edit(film, id);
    }

    public void addLike(long userId, long filmId) throws NoSuchFilmException {
        filmStorage.getFilm(filmId).addLike(userId);
    }

    public void removeLike(long userId, long filmId) throws NoSuchFilmException {
        filmStorage.getFilm(filmId).removeLike(userId);
    }

    public Collection<Film> getMostPopularFilms(Integer count) {
        if (count == null || count < 1) count = 10;
        return filmStorage.findAll().stream().sorted(new FilmComparatorForLikes()).limit(count).toList();
    }
}
