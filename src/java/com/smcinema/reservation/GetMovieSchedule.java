package com.smcinema.reservation;

import com.google.gson.Gson;
import com.smcinema.classes.DatabaseManager;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetMovieSchedule extends HttpServlet {
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs;
    
    List<String> listOfMovieSchedule = new ArrayList<>();
    String JSON_movieSchedule = null;
    String movieID,movieName,movieBranchName,movieAuditorium;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            movieID = request.getParameter("movieID");
            movieName = request.getParameter("movieName");
            movieBranchName = request.getParameter("movieBranchName");
            movieAuditorium = request.getParameter("movieAuditorium");
            
            con = DatabaseManager.getConnection();
            
            pstmt = con.prepareStatement("SELECT * FROM TRISTANROSALES.TBLMOVIECINEMA WHERE MOVIE_NAME=? "
                    + "AND MOVIE_BRANCHES=? AND MOVIE_AUDITORIUM=?");
            pstmt.setString(1, movieName);
            pstmt.setString(2, movieBranchName);
            pstmt.setString(3, movieAuditorium);
            
            rs = pstmt.executeQuery();
            
            while(rs.next()){   
                listOfMovieSchedule.clear();
                
                String movieSchedule = rs.getString("MOVIE_SCHEDULE");
                String[] splitMovieSchedule = null;
                splitMovieSchedule = movieSchedule.split(",");
                for (String splitMovieSchedule1 : splitMovieSchedule) {               
                    listOfMovieSchedule.add(splitMovieSchedule1);                       
                }
            }
            
            JSON_movieSchedule = new Gson().toJson(listOfMovieSchedule);  
            response.getWriter().write(JSON_movieSchedule);
            response.setContentType("application/json");
        } catch (SQLException ex) {
            Logger.getLogger(GetMovieSchedule.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            if(pstmt != null){
                try {
                    pstmt.close();
                } catch (SQLException ex) {
                    Logger.getLogger(GetMovieSchedule.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            if(con != null){
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(GetMovieSchedule.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
