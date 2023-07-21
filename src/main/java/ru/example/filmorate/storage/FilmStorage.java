package ru.example.filmorate.storage;

import ru.example.filmorate.exception.NoSuchFilmException;
import ru.example.filmorate.model.Film;
import ru.example.filmorate.model.Genre;
import ru.example.filmorate.model.Mpa;

import java.util.Collection;

public interface FilmStorage {

    Collection<Film> findAll();
    Film create(Film film);
    Film edit(Film film, long id);
    Film getFilm(long id) throws NoSuchFilmException;
    void delete(long id);
    Collection<Film> getMostPopularFilms(int count);
    void addLike(long userId, long filmId);
    void removeLike(long userId, long filmId);
    Collection<Mpa> getMpa();
    Collection<Genre> getGenres();
}
