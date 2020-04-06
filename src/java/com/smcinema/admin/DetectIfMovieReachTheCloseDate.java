package com.smcinema.admin;

import com.smcinema.classes.DatabaseManager;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DetectIfMovieReachTheCloseDate extends HttpServlet {
    Connection con;
    PreparedStatement pstmt;
    ResultSet rs;
    
    PrintWriter print;
    String movieID,movieName,movieCloseDate,movieStatus;
    boolean isDateTodayEqualToMovieCloseDate,isDateTodayAfterTheMovieCloseDate,isDateTodayBeforeTheMovieCloseDate;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException {
        response.setContentType("text/html;charset=UTF-8");
        con = DatabaseManager.getConnection();
        
        fetchMovieInformation();
    }
    
    public void fetchMovieInformation() throws ParseException{
        try {
            pstmt = con.prepareStatement("SELECT * FROM TRISTANROSALES.TBLMOVIECINEMA");
            rs = pstmt.executeQuery();
            
            while(rs.next()){
                movieID = rs.getString("MOVIE_ID");
                movieName = rs.getString("MOVIE_NAME");
                movieCloseDate = rs.getString("MOVIE_CLOSEDATE");
                movieStatus = rs.getString("MOVIE_STATUS");
                
                SimpleDateFormat sdf = new SimpleDateFormat("E, MMMMM dd, yyyy");
                Calendar dateToday_calendar = Calendar.getInstance();
                String dateToday_string = sdf.format(dateToday_calendar.getTime());
                Date newDateToday_date = sdf.parse(dateToday_string);
                Date newMovieCloseDate_date = sdf.parse(movieCloseDate);
                
                Calendar newDateToday_calendar = Calendar.getInstance();
                newDateToday_calendar.setTime(newDateToday_date);
                
                Calendar newMovieCloseDate_calendar = Calendar.getInstance();
                newMovieCloseDate_calendar.setTime(newMovieCloseDate_date);

                isDateTodayEqualToMovieCloseDate = newDateToday_calendar.equals(newMovieCloseDate_calendar);
                isDateTodayAfterTheMovieCloseDate = newDateToday_calendar.after(newMovieCloseDate_calendar);
                isDateTodayBeforeTheMovieCloseDate = newDateToday_calendar.before(newMovieCloseDate_calendar);

                if (isDateTodayEqualToMovieCloseDate || isDateTodayAfterTheMovieCloseDate) {
                    if(!movieStatus.equals("Deactivated")){
                        changeMovieStatus();
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DetectIfMovieReachTheCloseDate.class.getName()).log(Level.SEVERE, null, ex);
            print.println(ex.getMessage() + "<br>");
            print.flush();
        }       
    }
    
    public void changeMovieStatus(){
        try {
            pstmt = con.prepareStatement("UPDATE TRISTANROSALES.TBLMOVIECINEMA "
                    + "SET MOVIE_STATUS=? WHERE MOVIE_ID=? AND MOVIE_NAME=?");
            pstmt.setString(1, "Deactivated");
            pstmt.setString(2, movieID);
            pstmt.setString(3, movieName);
            
            pstmt.executeUpdate();
            
            print.println("Successfully deactivated " + movieName + "<br>");
            print.flush();
        } catch (SQLException ex) {
            Logger.getLogger(DetectIfMovieReachTheCloseDate.class.getName()).log(Level.SEVERE, null, ex);
            print.println(ex.getMessage() + "<br>");
            print.flush();
        }
    }
    
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            print = response.getWriter();
            processRequest(request, response);
        } catch (ParseException ex) {
            Logger.getLogger(DetectIfMovieReachTheCloseDate.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            print = response.getWriter();
            processRequest(request, response);
        } catch (ParseException ex) {
            Logger.getLogger(DetectIfMovieReachTheCloseDate.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
