package ru.example.filmorate.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ru.example.filmorate.model.Film;
import ru.example.filmorate.model.Genre;
import ru.example.filmorate.model.Mpa;
import ru.example.filmorate.service.FilmService;

import java.util.Collection;

@RestController
@RequestMapping("/films")
public class FilmController {

    private FilmService filmService;

    FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping()
    public Collection<Film> findAll() {
        return filmService.findAll();
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable long id) {
        return filmService.getFilm(id);
    }

    @GetMapping("/popular")
    public Collection<Film> getMostPopularFilms(@RequestParam(required = false) Integer count) {
        if (count == null || count < 1) count = 10;
        return filmService.getMostPopularFilms(count);
    }

    @PostMapping()
    public Film create(@Valid @RequestBody Film film) {
        return filmService.create(film);
    }

    @PutMapping("/{id}")
    public Film edit(@Valid @RequestBody Film film, @PathVariable long id) {
        return filmService.edit(film, id);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable long id, @PathVariable long userId) {
        filmService.addLike(userId, id);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable long id, @PathVariable long userId) {
        filmService.removeLike(userId, id);
    }

    @DeleteMapping("/{id}")
    public void deleteFilm(@PathVariable long id) {
        filmService.delete(id);
    }

    @GetMapping("/mpa")
    public Collection<Mpa> getMpa() {
        return filmService.getMpa();
    }

    @GetMapping("/genres")
    public Collection<Genre> getGenres() {
        return filmService.getGenres();
    }

}
