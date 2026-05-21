package com.nung.edu.dao;

import com.nung.edu.model.Movie;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class MovieDAO {

    private DataSource getDataSource() throws NamingException {
        InitialContext ctx = new InitialContext();
        return (DataSource) ctx.lookup("java:comp/env/jdbc/UserDB");
    }

    public boolean createMovie(String title, String director, String genre, double rating) {
        String query = "INSERT INTO movies (title, director, genre, rating) VALUES (?, ?, ?, ?)";
        try (Connection conn = getDataSource().getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setString(1, title);
                ps.setString(2, director);
                ps.setString(3, genre);
                ps.setDouble(4, rating);
                int rowsAffected = ps.executeUpdate();
                conn.commit();
                return rowsAffected > 0;
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
            }
        } catch (SQLException | NamingException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Movie getMovieById(int id) {
        String query = "SELECT * FROM movies WHERE id = ?";
        try (Connection conn = getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Movie(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("director"),
                            rs.getString("genre"),
                            rs.getDouble("rating")
                    );
                }
            }
        } catch (SQLException | NamingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateMovie(int id, String newTitle, String newDirector, String newGenre, double newRating) {
        String query = "UPDATE movies SET title = ?, director = ?, genre = ?, rating = ? WHERE id = ?";
        try (Connection conn = getDataSource().getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setString(1, newTitle);
                ps.setString(2, newDirector);
                ps.setString(3, newGenre);
                ps.setDouble(4, newRating);
                ps.setInt(5, id);
                int rowsAffected = ps.executeUpdate();
                conn.commit();
                return rowsAffected > 0;
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
            }
        } catch (SQLException | NamingException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteMovie(int id) {
        String query = "DELETE FROM movies WHERE id = ?";
        try (Connection conn = getDataSource().getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setInt(1, id);
                int rowsAffected = ps.executeUpdate();
                conn.commit();
                return rowsAffected > 0;
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
            }
        } catch (SQLException | NamingException e) {
            e.printStackTrace();
        }
        return false;
    }
}