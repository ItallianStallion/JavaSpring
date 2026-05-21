package com.nung.edu.servlet;

import com.nung.edu.dao.MovieDAO;
import com.nung.edu.model.Movie;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/user/*")
public class UserServlet extends HttpServlet {

    private final MovieDAO movieDAO = new MovieDAO();

    private void setCorsHeaders(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setHeader("Access-Control-Allow-Credentials", "true");
    }

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) {
        setCorsHeaders(response);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        setCorsHeaders(response);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        String searchId = request.getParameter("id");
        List<String> jsonItems = new ArrayList<>();

        if (searchId != null && !searchId.trim().isEmpty()) {
            try {
                int id = Integer.parseInt(searchId);
                Movie movie = movieDAO.getMovieById(id);
                if (movie != null) {
                    String jsonObject = String.format(
                            "{\"id\":%d, \"title\":\"%s\", \"director\":\"%s\", \"genre\":\"%s\", \"rating\":%.1f}",
                            movie.getId(), movie.getTitle(), movie.getDirector(), movie.getGenre(), movie.getRating()
                    );
                    jsonItems.add(jsonObject);
                }
            } catch (NumberFormatException e) {

            }
        } else {
            for (int i = 1; i <= 10; i++) {
                Movie movie = movieDAO.getMovieById(i);
                if (movie != null) {
                    String jsonObject = String.format(
                            "{\"id\":%d, \"title\":\"%s\", \"director\":\"%s\", \"genre\":\"%s\", \"rating\":%.1f}",
                            movie.getId(), movie.getTitle(), movie.getDirector(), movie.getGenre(), movie.getRating()
                    );
                    jsonItems.add(jsonObject);
                }
            }
        }

        String jsonArray = "[" + String.join(",", jsonItems) + "]";
        out.print(jsonArray);
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        setCorsHeaders(response);

        // Витягуємо дані, які надіслав React
        String title = request.getParameter("title");
        String director = request.getParameter("director");
        String genre = request.getParameter("genre");
        String ratingStr = request.getParameter("rating");

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        java.io.PrintWriter out = response.getWriter();

        if (title != null && director != null && genre != null && ratingStr != null) {
            try {
                double rating = Double.parseDouble(ratingStr);

                boolean success = movieDAO.createMovie(title, director, genre, rating);

                if (success) {
                    out.print("{\"status\":\"success\", \"message\":\"Фільм успішно додано!\"}");
                } else {
                    out.print("{\"status\":\"error\", \"message\":\"Помилка збереження в базу\"}");
                }
            } catch (NumberFormatException e) {
                out.print("{\"status\":\"error\", \"message\":\"Рейтинг має бути числом!\"}");
            }
        } else {
            out.print("{\"status\":\"error\", \"message\":\"Заповніть всі поля!\"}");
        }
        out.flush();
    }
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        setCorsHeaders(response);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        // Для PUT запитів параметри зручніше передавати через URL (Query String)
        String idStr = request.getParameter("id");
        String title = request.getParameter("title");
        String director = request.getParameter("director");
        String genre = request.getParameter("genre");
        String ratingStr = request.getParameter("rating");

        if (idStr != null && title != null && director != null && genre != null && ratingStr != null) {
            try {
                int id = Integer.parseInt(idStr);
                double rating = Double.parseDouble(ratingStr);

                boolean success = movieDAO.updateMovie(id, title, director, genre, rating);
                if (success) {
                    out.print("{\"status\":\"success\", \"message\":\"Фільм оновлено!\"}");
                } else {
                    out.print("{\"status\":\"error\", \"message\":\"Помилка оновлення в базі\"}");
                }
            } catch (NumberFormatException e) {
                out.print("{\"status\":\"error\", \"message\":\"Некоректні числові дані!\"}");
            }
        } else {
            out.print("{\"status\":\"error\", \"message\":\"Заповніть всі поля!\"}");
        }
        out.flush();
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        setCorsHeaders(response);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        String idStr = request.getParameter("id");
        if (idStr != null) {
            try {
                int id = Integer.parseInt(idStr);
                boolean success = movieDAO.deleteMovie(id);
                if (success) {
                    out.print("{\"status\":\"success\", \"message\":\"Фільм видалено!\"}");
                } else {
                    out.print("{\"status\":\"error\", \"message\":\"Помилка видалення в базі\"}");
                }
            } catch (NumberFormatException e) {
                out.print("{\"status\":\"error\", \"message\":\"Некоректний ID!\"}");
            }
        }
        out.flush();
    }
}