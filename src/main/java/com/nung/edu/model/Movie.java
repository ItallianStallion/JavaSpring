package com.nung.edu.model;

public class Movie {
    private int id;
    private String title;
    private String director;
    private String genre;
    private double rating;

    public Movie(int id, String title, String director, String genre, double rating) {
        this.id = id;
        this.title = title;
        this.director = director;
        this.genre = genre;
        this.rating = rating;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDirector() { return director; }
    public String getGenre() { return genre; }
    public double getRating() { return rating; }
}