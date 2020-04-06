package com.smcinema.user;

import com.google.gson.JsonObject;
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

public class DetectIfUserIsBlocked extends HttpServlet {
    Connection con;
    PreparedStatement pstmt;
    ResultSet rs;
    PrintWriter print;
    JsonObject status = new JsonObject();
    
    String blockedUntil,user_emailAddress;
    boolean isDateTodayEqualToBlockedUntilDate,isDateTodayBeforeTheBlockedUntilDate,
            isDateTodayAfterTheBlockedUntilDate;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
                
        con = DatabaseManager.getConnection();
        
        user_emailAddress = request.getParameter("user_emailAddress");
        
        fetchAllRecordsOfBlockedUsers(request,response);
    }
    
    public void fetchAllRecordsOfBlockedUsers(HttpServletRequest request,HttpServletResponse response) throws ParseException, IOException{
        try {
            pstmt = con.prepareStatement("SELECT * FROM TRISTANROSALES.TBLBLOCKEDUSERS WHERE USER_EMAILADDRESS=?");
            pstmt.setString(1, user_emailAddress);
            
            rs = pstmt.executeQuery();

            if(rs.next()){
                blockedUntil = rs.getString("USER_BLOCKEDUNTIL");
                status.addProperty("USER_BLOCKEDUNTIL", blockedUntil);
                //request.getSession().setAttribute("USER_BLOCKEDUNTIL", blockedUntil);

                detectIfUserIsBlocked(response);
            }else{
                status.addProperty("isUserBlocked", false);
                response.getWriter().write(status.toString());
            }
        } catch (SQLException ex) {
            Logger.getLogger(DetectIfUserIsBlocked.class.getName()).log(Level.SEVERE, null, ex);
            status.addProperty("isUserBlocked", false);
            response.getWriter().write(status.toString());
        }
    }
    
    public void detectIfUserIsBlocked(HttpServletResponse response) throws ParseException, IOException{
        SimpleDateFormat sdf = new SimpleDateFormat("E, MMMMM dd, yyyy");
        Calendar dateToday_calendar = Calendar.getInstance();
        String dateToday_string = sdf.format(dateToday_calendar.getTime());
        Date newDateToday_date = sdf.parse(dateToday_string);
        Date newBlockedUntil_date = sdf.parse(blockedUntil);

        Calendar newDateToday_calendar = Calendar.getInstance();
        newDateToday_calendar.setTime(newDateToday_date);

        Calendar newBlockedUntil_calendar = Calendar.getInstance();
        newBlockedUntil_calendar.setTime(newBlockedUntil_date);

        isDateTodayEqualToBlockedUntilDate = newDateToday_calendar.equals(newBlockedUntil_calendar);
        isDateTodayBeforeTheBlockedUntilDate = newDateToday_calendar.before(newBlockedUntil_calendar);
        isDateTodayAfterTheBlockedUntilDate = newDateToday_calendar.after(newBlockedUntil_calendar);

        if(isDateTodayEqualToBlockedUntilDate || isDateTodayAfterTheBlockedUntilDate){
            status.addProperty("isUserBlocked", false);
            response.getWriter().write(status.toString());
        }else{
            status.addProperty("isUserBlocked", true);
            response.getWriter().write(status.toString());
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
            Logger.getLogger(DetectIfUserIsBlocked.class.getName()).log(Level.SEVERE, null, ex);
            print.println(ex.getMessage() + "<br>");
            print.flush();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            print = response.getWriter();
            processRequest(request, response);
        } catch (ParseException ex) {
            Logger.getLogger(DetectIfUserIsBlocked.class.getName()).log(Level.SEVERE, null, ex);
            print.println(ex.getMessage() + "<br>");
            print.flush();
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
