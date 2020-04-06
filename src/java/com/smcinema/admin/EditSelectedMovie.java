package com.smcinema.admin;

import com.google.gson.JsonObject;
import com.smcinema.classes.DatabaseManager;
import com.smcinema.classes.MovieBean;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EditSelectedMovie extends HttpServlet {
    Connection con;
    PreparedStatement pstmt;
    Statement stmt;
    ResultSet rs;
    PrintWriter print;
    MovieBean movie = new MovieBean();
    JsonObject status = new JsonObject();
    
    String movieID,query,user_emailAddress,userType;
    int count;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            
            con = DatabaseManager.getConnection();
            
            user_emailAddress = (String) request.getSession().getAttribute("COOKIE_USER_EMAILADDRESS");
            movieID = (String) request.getSession().getAttribute("MOVIE_ID");
            
            movie.setMovieReference(request.getParameter("txtMovieReference"));
            movie.setMovieName(request.getParameter("txtMovieName"));
            movie.setMovieYear(request.getParameter("txtMovieYear"));
            movie.setMovieTrailerLink(request.getParameter("txtMovieTrailerLink"));
            movie.setMTRCB_rating(request.getParameter("txtMTRCB_Rating"));
            movie.setMovieDirector(request.getParameter("txtMovieDirector"));
            movie.setReleasedBy(request.getParameter("txtMovieReleasedBy"));
            movie.setMovieCast(request.getParameter("txtMovieCast"));
            movie.setMovieGenre(request.getParameter("txtMovieGenre"));
            movie.setMovieSynopsis(request.getParameter("txtMovieSynopsis"));
            movie.setMovieDuration(request.getParameter("txtMovieDuration"));
            movie.setMoviePrice(request.getParameter("txtMoviePrice"));
            movie.setTypeOfSeating(request.getParameter("txtMovieTypeOfSeating"));
            movie.setMovieStatus(request.getParameter("txtMovieStatus"));
            
            stmt = con.createStatement();
            
            rs = stmt.executeQuery("SELECT RESERVATION_MOVIENAME FROM TRISTANROSALES.TBLRESERVATIONSREPORT "
                    + "WHERE RESERVATION_MOVIENAME LIKE '%" + movie.getMovieName() + "%'");
            
            if(rs.next()){    
                status.addProperty("isEditMovieSuccess", "reservationIsInProgress");
                response.getWriter().write(status.toString());
            }else{
                detectIfUserIsAnITSupervisor(response);
            }
        } catch (SQLException ex) {
            Logger.getLogger(EditSelectedMovie.class.getName()).log(Level.SEVERE, null, ex);
            detectIfUserIsAnITSupervisor(response);
        }
    }
    
    public void detectIfUserIsAnITSupervisor(HttpServletResponse response) throws IOException{
        try {
            pstmt = con.prepareStatement("SELECT * FROM TRISTANROSALES.TBLUSERSANDADMINS "
                    + "WHERE USER_EMAILADDRESS=?");
            pstmt.setString(1, user_emailAddress);
            
            rs = pstmt.executeQuery();
            
            if(rs.next()){
                userType = rs.getString("USER_TYPE");
                
                if(userType.equals("IT Supervisor")){
                    updateMovie(response);
                }else{
                    status.addProperty("isEditMovieSuccess", "accessDenied");
                    response.getWriter().write(status.toString());
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(EditSelectedMovie.class.getName()).log(Level.SEVERE, null, ex);
            response.getWriter().write(ex.getMessage());
        }
    }
    
    public void updateMovie(HttpServletResponse response) throws IOException{
        try {
            rs = stmt.executeQuery("SELECT MOVIE_NAME FROM TRISTANROSALES.TBLMOVIECINEMA "
                    + "WHERE MOVIE_NAME LIKE '%" + movie.getMovieName() + "%'");
            
            if(rs.next()){    
                status.addProperty("isEditMovieSuccess", "existingMovieName");
                response.getWriter().write(status.toString());
            }else{
                pstmt = con.prepareStatement("UPDATE TRISTANROSALES.TBLMOVIECINEMA "
                    + "SET MOVIE_REFERENCE=?,MOVIE_NAME=?,MOVIE_YEAR=?,MOVIE_DIRECTOR=?,"
                    + "MOVIE_RELEASEDBY=?,MOVIE_TRAILERLINK=?,MOVIE_DURATION=?,MOVIE_PRICE=?,"
                    + "MOVIE_TYPEOFSEATING=?,MOVIE_CAST=?,MOVIE_SYNOPSIS=?,MTRCB_RATING=?,"
                    + "MOVIE_GENRE=?,MOVIE_STATUS=? WHERE MOVIE_ID=?");
                pstmt.setString(1, movie.getMovieReference());
                pstmt.setString(2, movie.getMovieName());
                pstmt.setString(3, movie.getMovieYear());
                pstmt.setString(4, movie.getMovieDirector());
                pstmt.setString(5, movie.getReleasedBy());
                pstmt.setString(6, movie.getMovieTrailerLink());
                pstmt.setString(7, movie.getMovieDuration());
                pstmt.setString(8, movie.getMoviePrice());
                pstmt.setString(9, movie.getTypeOfSeating());
                pstmt.setString(10, movie.getMovieCast());
                pstmt.setString(11, movie.getMovieSynopsis());
                pstmt.setString(12, movie.getMTRCB_rating());
                pstmt.setString(13, movie.getMovieGenre());
                pstmt.setString(14, movie.getMovieStatus());
                pstmt.setString(15, movieID);

                pstmt.executeUpdate();

                status.addProperty("isEditMovieSuccess", true);
                response.getWriter().write(status.toString());    
            }
            
            con.close(); 
        } catch (SQLException ex) {
            Logger.getLogger(EditSelectedMovie.class.getName()).log(Level.SEVERE, null, ex);
            response.getWriter().write(ex.getMessage());
        }
    }
    
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        print = response.getWriter();
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        print = response.getWriter();
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
