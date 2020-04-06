package com.smcinema.classes;

public class MovieBranchAndAuditoriumBean {
    String movie_branchName,movie_auditoriumName,movie_auditorium_totalNumberOfSeats,dateAndTimeCreated;

    public String getMovie_branchName() {
        return movie_branchName;
    }

    public String getDateAndTimeCreated() {
        return dateAndTimeCreated;
    }

    public void setDateAndTimeCreated(String dateAndTimeCreated) {
        this.dateAndTimeCreated = dateAndTimeCreated;
    }

    public void setMovie_branchName(String movie_branchName) {
        this.movie_branchName = movie_branchName;
    }

    public String getMovie_auditoriumName() {
        return movie_auditoriumName;
    }

    public void setMovie_auditoriumName(String movie_auditoriumName) {
        this.movie_auditoriumName = movie_auditoriumName;
    }

    public String getMovie_auditorium_totalNumberOfSeats() {
        return movie_auditorium_totalNumberOfSeats;
    }

    public void setMovie_auditorium_totalNumberOfSeats(String movie_auditorium_totalNumberOfSeats) {
        this.movie_auditorium_totalNumberOfSeats = movie_auditorium_totalNumberOfSeats;
    }
}
