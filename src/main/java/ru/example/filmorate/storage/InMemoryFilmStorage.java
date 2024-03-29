package ru.example.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.example.filmorate.controller.FilmController;
import ru.example.filmorate.exception.NoSuchFilmException;
import ru.example.filmorate.exception.ValidationFilmException;
import ru.example.filmorate.model.Film;
import ru.example.filmorate.model.FilmComparatorForLikes;
import ru.example.filmorate.model.Genre;
import ru.example.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();
    private static int count = 1;
    private final static Logger log = LoggerFactory.getLogger(FilmController.class);

    @Override
    public Collection<Film> findAll() {
        return films.values();
    }

    @Override
    public Film create(Film film) {
        hasValidationException(film);
        film.setId(count++);
        films.put(film.getId(), film);
        log.info("Has been created: " + film);
        return film;
    }

    @Override
    public Film edit(Film film, long id) {
        hasValidationException(film);
        if (films.containsKey(id) && (film.getId() == 0 || film.getId() == id)) {
            films.put(id, film);
            log.info("Has been edited: " + film);
        }
        else if (film.getId() != 0 && film.getId() != id) {
            log.info("The ID in request body doesn't match the ID in the path");
            throw new RuntimeException("The ID in request body must match the ID in the path");
        }
        else {
            log.info("NoSuchFilmException, id " + id);
            throw new NoSuchFilmException("Film with required id is not found");
        }
        return film;
    }

    @Override
    public Film getFilm(long id) throws NoSuchFilmException {
        if (!films.containsKey(id)) throw new NoSuchFilmException("Film with required id is not found");
        return films.get(id);
    }

//    private Collection<Film> getFilms(Collection<Long> idCollection) {
//        return idCollection.stream().map(films::get).toList();
//    }

    @Override
    public void delete(long id) {
        films.remove(id);
    }

    @Override
    public Collection<Film> getMostPopularFilms(int count) {
        return findAll().stream().sorted(new FilmComparatorForLikes()).limit(count).toList();
    }

    @Override
    public void addLike(long userId, long filmId) {
        Film film = getFilm(filmId);
        film.addLike(userId);
        edit(film, filmId);
    }

    @Override
    public void removeLike(long userId, long filmId) {
        Film film = getFilm(filmId);
        film.removeLike(userId);
        edit(film, filmId);
    }

    private void hasValidationException(Film film) throws ValidationFilmException {
        if (film.getReleaseDate().isBefore(LocalDate.parse("1895-12-28"))) {
            log.info("ValidationFilmException: " + film);
            throw new ValidationFilmException("Film can't be created earlier than 1895-12-28");
        }
    }

    public Collection<Mpa> getMpa() {
        return List.of(Mpa.values());
    }

    public Collection<Genre> getGenres() {
        return List.of(Genre.values());
    }
}
