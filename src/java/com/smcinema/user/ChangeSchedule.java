package com.smcinema.user;

import com.google.gson.JsonObject;
import com.smcinema.classes.DatabaseManager;
import java.io.IOException;
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

public class ChangeSchedule extends HttpServlet {
    Connection con;
    PreparedStatement pstmt;
    ResultSet rs;
    JsonObject status = new JsonObject();
    
    String user_emailAddress,selected_availableDay,selected_scheduleTime,paymentDue,
            selected_movieTypeOfSeating,selected_movieStatus;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
        
            con = DatabaseManager.getConnection();
            
            user_emailAddress = request.getParameter("user_emailAddress");
            selected_availableDay = request.getParameter("txtSelectMovieAvailableDay");
            selected_scheduleTime = (String) request.getParameter("txtSelectMovieScheduleTime");
            selected_movieTypeOfSeating = (String) request.getSession().getAttribute("reservation_typeOfSeating");
            selected_movieStatus = (String) request.getSession().getAttribute("reservation_status"); 
            
            //create a java timestamp object that represents the current time (i.e., a "current timestamp")
            SimpleDateFormat sdf_startTime = new SimpleDateFormat("hh:mma");
            Date date = sdf_startTime.parse(selected_scheduleTime);
            Calendar startTime = Calendar.getInstance();
            
            startTime.setTime(date);
            startTime.add(Calendar.MINUTE, -45);
            
            String paymentDue_time = sdf_startTime.format(startTime.getTime());
            
            paymentDue = selected_availableDay + " | " + paymentDue_time;
            
            if(selected_movieStatus.equals("Paid")){
                status.addProperty("isChangeScheduleSuccess", "accessDenied");
                response.getWriter().write(status.toString());
            }else{
                updateOrChangeSchedule(response);
            }
        } catch (ParseException ex) {
            Logger.getLogger(ChangeSchedule.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void updateOrChangeSchedule(HttpServletResponse response) throws IOException{
        try {
            pstmt = con.prepareStatement("UPDATE TRISTANROSALES.TBLRESERVATIONSREPORT "
                    + "SET RESERVATION_MOVIESCREENINGDATE=?,RESERVATION_MOVIESTARTTIME=?,"
                    + "RESERVATION_PAYMENTDUE=? WHERE RESERVATION_MOVIERESERVEDBY=?");
            pstmt.setString(1, selected_availableDay);
            pstmt.setString(2, selected_scheduleTime);
            pstmt.setString(3, paymentDue);
            pstmt.setString(4, user_emailAddress);
            
            pstmt.executeUpdate();
            
            status.addProperty("isChangeScheduleSuccess", true);
            response.getWriter().write(status.toString());
                
            con.close(); 
        } catch (SQLException ex) {
            Logger.getLogger(ChangeSchedule.class.getName()).log(Level.SEVERE, null, ex);
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
