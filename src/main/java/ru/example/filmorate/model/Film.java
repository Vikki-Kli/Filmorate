package ru.example.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

public class Film {

    private long id;
    @NotBlank
    private String name;
    @Size(max=200)
    private String description;
    @NotNull
    private LocalDate releaseDate;
    @Positive
    private int duration;
    private Collection<Long> likes = new HashSet<>();
    private Collection<Genre> genres = new HashSet<>();
    private Mpa mpa;

    public Film(){
    }

    public Film(long id, String name, String description, LocalDate releaseDate, int duration, Collection<Long> likes, Collection<Genre> genres, Mpa mpa) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.likes = likes;
        this.genres = genres;
        this.mpa = mpa;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public Mpa getMpa() {
        return mpa;
    }

    public Integer getDuration() {
        return duration;
    }

    public Collection<Genre> getGenres() {
        return genres;
    }

    public void addGenre(Genre genre) {
        genres.add(genre);
    }

    public void removeGenre(Genre genre) {
        genres.remove(genre);
    }

    public Collection<Long> getLikes() {
        return likes;
    }

    public void addLike(long id) {
        likes.add(id);
    }

    public void removeLike(long id) {
        likes.remove(id);
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public void setMpa(Mpa mpa) {
        this.mpa = mpa;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Film film = (Film) o;
        return id == film.id && name.equals(film.name) && Objects.equals(description, film.description) && releaseDate.equals(film.releaseDate) && duration==film.duration;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, releaseDate, duration);
    }

    @Override
    public String toString() {
        return "Film{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", releaseDate=" + releaseDate +
                ", duration=" + duration +
                '}';
    }
}

