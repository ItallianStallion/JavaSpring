package com.nung.edu.servlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/user/*")
public class UserServlet extends HttpServlet {

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

        String jsonArray = DatabaseManager.getAllMoviesAsJson();

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        PrintWriter out = response.getWriter();
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

        // Перевіряємо, чи всі поля заповнені
        if (title != null && director != null && genre != null && ratingStr != null) {
            try {
                double rating = Double.parseDouble(ratingStr);
                boolean success = DatabaseManager.addMovie(title, director, genre, rating);

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
}
