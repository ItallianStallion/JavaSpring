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

        String requestParam = request.getParameter("param");

        String pathInfo = request.getPathInfo();
        String pathVar = "Не вказано";
        if (pathInfo != null && pathInfo.length() > 1) {
            pathVar = pathInfo.substring(1);
        }

        HttpSession session = request.getSession(false);
        String sessionUser = (session != null) ? (String) session.getAttribute("username") : "Гість";

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        String jsonResponse = String.format(
                "{\"requestParam\": \"%s\", \"pathVariable\": \"%s\", \"sessionUser\": \"%s\"}",
                requestParam != null ? requestParam : "null", pathVar, sessionUser
        );

        out.print(jsonResponse);
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        setCorsHeaders(response);

        HttpSession session = request.getSession();
        session.setAttribute("username", "JohnDoe");

        Cookie userCookie = new Cookie("Orange", "orange");
        userCookie.setMaxAge(60 * 60);
        userCookie.setPath("/");
        response.addCookie(userCookie);

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        PrintWriter out = response.getWriter();
        out.print("{\"status\": \"Success\", \"message\": \"Сесію успішно створено, кукі додано!\"}");
        out.flush();
    }
}
