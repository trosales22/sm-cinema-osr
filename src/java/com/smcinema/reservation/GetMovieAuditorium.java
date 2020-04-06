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

public class GetMovieAuditorium extends HttpServlet {
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs;
    
    List<String> listOfMovieAuditorium = new ArrayList<>();
    String JSON_movieAuditorium = null;
    String movieID,movieName,movieBranchName;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            movieID = request.getParameter("movieID");
            movieName = request.getParameter("movieName");
            movieBranchName = request.getParameter("movieBranchName");
            con = DatabaseManager.getConnection();
            
            pstmt = con.prepareStatement("SELECT * FROM TRISTANROSALES.TBLMOVIECINEMA WHERE MOVIE_NAME=? "
                    + "AND MOVIE_BRANCHES=?");
            pstmt.setString(1, movieName);
            pstmt.setString(2, movieBranchName);
            
            rs = pstmt.executeQuery();
            
            while(rs.next()){
                listOfMovieAuditorium.clear();
                listOfMovieAuditorium.add(rs.getString("MOVIE_AUDITORIUM"));
            }
            
            JSON_movieAuditorium = new Gson().toJson(listOfMovieAuditorium);  
            response.getWriter().write(JSON_movieAuditorium);
            response.setContentType("application/json");
        } catch (SQLException ex) {
            Logger.getLogger(GetMovieAuditorium.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            if(pstmt != null){
                try {
                    pstmt.close();
                } catch (SQLException ex) {
                    Logger.getLogger(GetMovieAuditorium.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            if(con != null){
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(GetMovieAuditorium.class.getName()).log(Level.SEVERE, null, ex);
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

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
