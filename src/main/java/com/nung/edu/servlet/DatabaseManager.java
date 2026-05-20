package com.nung.edu.servlet;

public class DatabaseManager {

    private static final String URL = "jdbc:mysql://localhost:3306/mydb?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASSWORD = "Root_Pass123!";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static String getAllMoviesAsJson() {
        String query = "SELECT * FROM movies";
        java.util.List<String> jsonItems = new java.util.ArrayList<>();

        try (java.sql.Connection conn = java.sql.DriverManager.getConnection(URL, USER, PASSWORD);
             java.sql.PreparedStatement ps = conn.prepareStatement(query);
             java.sql.ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String director = rs.getString("director");
                String genre = rs.getString("genre");
                double rating = rs.getDouble("rating");

                String jsonObject = String.format(
                        "{\"id\":%d, \"title\":\"%s\", \"director\":\"%s\", \"genre\":\"%s\", \"rating\":%.1f}",
                        id, title, director, genre, rating
                );
                jsonItems.add(jsonObject);
            }
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
            return "[]";
        }

        return "[" + String.join(",", jsonItems) + "]";
    }
    public static boolean addMovie(String title, String director, String genre, double rating) {
        String query = "INSERT INTO movies (title, director, genre, rating) VALUES (?, ?, ?, ?)";

        try (java.sql.Connection conn = java.sql.DriverManager.getConnection(URL, USER, PASSWORD);
             java.sql.PreparedStatement ps = conn.prepareStatement(query)) {


            ps.setString(1, title);
            ps.setString(2, director);
            ps.setString(3, genre);
            ps.setDouble(4, rating);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (java.sql.SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}