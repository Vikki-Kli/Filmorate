package ru.example.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.example.filmorate.exception.NoSuchFilmException;
import ru.example.filmorate.exception.ValidationFilmException;
import ru.example.filmorate.model.Film;
import ru.example.filmorate.model.Genre;
import ru.example.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class FilmDbStorage implements FilmStorage {

    private JdbcTemplate jdbcTemplate;
    private UserStorage userStorage;
    private final static Logger logger = LoggerFactory.getLogger(FilmDbStorage.class);


    public FilmDbStorage(JdbcTemplate jdbcTemplate, @Qualifier("userDbStorage") UserStorage userStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.userStorage = userStorage;
    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        String sqlLikes = "select * from likes where film_id = ?";
        Collection<Long> likes = jdbcTemplate.query(sqlLikes, (rs1, rn) -> rs1.getLong("user_id"), rs.getLong("id"));

        String sqlGenres = "select * from genres join film_genre on genres.id = film_genre.genre_id where film_id = ?";
        Collection<Genre> genres = jdbcTemplate.query(sqlGenres, (rs2, rn) ->
                Genre.valueOf(rs2.getString("name")), rs.getLong("id"));

        String sqlMpa = "select * from mpa where id = ?";
        Mpa mpa = jdbcTemplate.query(sqlMpa, (rs2, rn) ->
                Mpa.valueOf(rs2.getString("name")), rs.getLong("mpa_id")).get(0);

        return new Film(rs.getLong("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getDate("release_date").toLocalDate(),
                rs.getInt("duration"),
                likes,
                genres,
                mpa);
    }

    @Override
    public Collection<Film> findAll() {
        String sql = "select * from films";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));
    }

    @Override
    public Film getFilm(long id) throws NoSuchFilmException {
        String sql = "select * from films where id = ?";
        Optional<Film> optionalFilm = jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs), id).stream().findFirst();
        if (optionalFilm.isEmpty()) throw new NoSuchFilmException("Film with id " + id + " has not been found");
        else return optionalFilm.get();
    }

    private byte mpaToByte(Mpa mpa) {
        String sql = "select * from mpa where name = ?";
        SqlRowSet srs = jdbcTemplate.queryForRowSet(sql, mpa.toString());
        if (srs.next()) return srs.getByte("id");
        else throw new ValidationFilmException("This mpa is not exist");
    }

    private byte genreToByte(Genre genre) {
        String sql = "select * from genres where name = ?";
        SqlRowSet srs = jdbcTemplate.queryForRowSet(sql, genre.toString());
        if (srs.next()) return srs.getByte("id");
        else throw new ValidationFilmException("This genre is not exist");
    }

    @Override
    public Film create(Film film) {
        byte mpaId = mpaToByte(film.getMpa());

        String sqlCreate = "insert into films(name, description, release_date, duration, mpa_id) values(?, ?, ?, ?, ?)";
        jdbcTemplate.update(sqlCreate, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), mpaId);

        String sqlGet = "select * from films where name = ?";
        Film filmToReturn = jdbcTemplate.query(sqlGet, (rs, rn) -> makeFilm(rs), film.getName()).get(0);

        String sqlGenres = "insert into film_genre(film_id, genre_id) values(?, ?)";
        for (Genre i : film.getGenres()) {
            jdbcTemplate.update(sqlGenres, filmToReturn.getId(), genreToByte(i));
        }

        logger.info("Film has been created: {}", filmToReturn);
        return filmToReturn;
    }

    @Override
    public Film edit(Film film, long id) {
        getFilm(id);

        String sql = "update films set name = ?, description = ?, release_date = ?, duration = ?, mpa_id = ? where id = ?";
        jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), mpaToByte(film.getMpa()), id);

        String sqlGenres = "insert into film_genre(film_id, genre_id) values(?, ?)";
        for (Genre i : film.getGenres()) {
            jdbcTemplate.update(sqlGenres, id, genreToByte(i));
        }

        logger.info("Film has been edited: {}", film);
        return getFilm(id);
    }

    @Override
    public void delete(long id) {
        getFilm(id);
        String sql = "delete from films where id = ?";
        jdbcTemplate.update(sql, id);
        logger.info("Film {} has been deleted", id);
    }

    @Override
    public Collection<Film> getMostPopularFilms(int count) {
        String sql = "select * from films " +
                "join (select film_id, count(*) as count from likes group by film_id) as likes_count on films.id = likes_count.film_id " +
                "order by count desc limit ?";
        return jdbcTemplate.query(sql, (rs, rn) -> makeFilm(rs), count);
    }

    @Override
    public void addLike(long userId, long filmId) {
        getFilm(filmId);
        userStorage.getUser(userId);
        String sql = "insert into likes (user_id, film_id) values (?, ?)";
        jdbcTemplate.update(sql, userId, filmId);
        logger.info("User {} liked film {}", userId, filmId);
    }

    @Override
    public void removeLike(long userId, long filmId) {
        getFilm(filmId);
        userStorage.getUser(userId);
        String sql = "delete from likes where user_id = ? and film_id = ?";
        jdbcTemplate.update(sql, userId, filmId);
        logger.info("User {} unliked film {}", userId, filmId);
    }

    public Collection<Mpa> getMpa() {
        String sql = "select * from mpa";
        return jdbcTemplate.query(sql, (rs, rn) -> Mpa.valueOf(rs.getString("name")));
    }

    public Collection<Genre> getGenres() {
        String sql = "select * from genres";
        return jdbcTemplate.query(sql, (rs, rn) -> Genre.valueOf(rs.getString("name")));
    }
}
