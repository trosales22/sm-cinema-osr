package com.smcinema.reservation;

import com.google.gson.Gson;
import com.smcinema.classes.DatabaseManager;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetMovieAvailableDay extends HttpServlet {
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs;
    
    List<String> listOfMovieAvailableDay = new ArrayList<>();
    String JSON_movieAvailableDay = null;
    String movieID,movieName,movieBranchName,movieAuditorium;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException {
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
                listOfMovieAvailableDay.clear();
                
                SimpleDateFormat sdf = new SimpleDateFormat("E, MMMMM dd, yyyy");
                Calendar cal = Calendar.getInstance();
                // get starting date
                //cal.add(Calendar.DAY_OF_WEEK, -1);
                
                //String movieCloseDate = rs.getString("MOVIE_CLOSEDATE");
                
                // loop adding one day in each iteration
                for(int i = 0; i< 3; i++){
                    cal.add(Calendar.DAY_OF_YEAR, 1);                
                    listOfMovieAvailableDay.add(sdf.format(cal.getTime()));
                }  
                
            }
            
            JSON_movieAvailableDay = new Gson().toJson(listOfMovieAvailableDay);  
            response.getWriter().write(JSON_movieAvailableDay);
            response.setContentType("application/json");
        } catch (SQLException ex) {
            Logger.getLogger(GetMovieAvailableDay.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            if(pstmt != null){
                try {
                    pstmt.close();
                } catch (SQLException ex) {
                    Logger.getLogger(GetMovieAvailableDay.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            if(con != null){
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(GetMovieAvailableDay.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (ParseException ex) {
            Logger.getLogger(GetMovieAvailableDay.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (ParseException ex) {
            Logger.getLogger(GetMovieAvailableDay.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
