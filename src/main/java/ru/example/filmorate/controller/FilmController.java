package ru.example.filmorate.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.example.filmorate.exception.NoSuchFilmException;
import ru.example.filmorate.exception.ValidationFilmException;
import ru.example.filmorate.model.Film;
import ru.example.filmorate.service.FilmService;

import java.util.Collection;

@RestController
@RequestMapping("/films")
public class FilmController {

    private FilmService filmService;

    @Autowired
    FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping()
    public Collection<Film> findAll() {
        return filmService.findAll();
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable long id) throws NoSuchFilmException {
        return filmService.getFilm(id);
    }

    @GetMapping("/popular?count={count}")
    public Collection<Film> getMostPopularFilms(@RequestParam(required = false) Integer count) {
        return filmService.getMostPopularFilms(count);
    }

    @PostMapping()
    public Film create(@Valid @RequestBody Film film) throws ValidationFilmException {
        return filmService.create(film);
    }

    @PutMapping("/{id}")
    public Film edit(@Valid @RequestBody Film film, @PathVariable long id) throws Exception {
        return filmService.edit(film, id);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable long id, @PathVariable long userId) throws NoSuchFilmException {
        filmService.addLike(userId, id);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable long id, @PathVariable long userId) throws NoSuchFilmException {
        filmService.removeLike(userId, id);
    }

}
