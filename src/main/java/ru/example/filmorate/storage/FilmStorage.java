package ru.example.filmorate.storage;

import ru.example.filmorate.exception.NoSuchFilmException;
import ru.example.filmorate.exception.ValidationFilmException;
import ru.example.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {

    Collection<Film> findAll();
    Film create(Film film) throws ValidationFilmException;
    Film edit(Film film, long id) throws Exception;
    Film getFilm(long id) throws NoSuchFilmException;
    Collection<Film> getFilms(Collection<Long> idCollection);
}
