package com.smcinema.admin;

import com.google.gson.JsonObject;
import com.smcinema.classes.DatabaseManager;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UpdateStatusOfUserInReservation extends HttpServlet {
    Connection con;
    PreparedStatement pstmt;
    ResultSet rs;
    Statement stmt;
    JsonObject statusOfUpdateStatusOfUserInReservation = new JsonObject();
    
    String query,reservation_referenceNumber,status,user_emailAddress,userType,
            blockedUntil,reservedBy,new_dateAndTimeReserved;
    int count;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            
            con = DatabaseManager.getConnection();
            
            reservation_referenceNumber = (String) request.getSession().getAttribute("referenceNumber");
            status = request.getParameter("txtStatusOption");
            user_emailAddress = (String) request.getSession().getAttribute("COOKIE_USER_EMAILADDRESS");
            reservedBy = (String) request.getSession().getAttribute("reservedBy");
            
            SimpleDateFormat sdf_dateAndTimeCreated = new SimpleDateFormat("MMMMM dd, yyyy | hh:mm a");
            Calendar dateAndTimeCreated = Calendar.getInstance();
                    
            new_dateAndTimeReserved = sdf_dateAndTimeCreated.format(dateAndTimeCreated.getTime());
            
            pstmt = con.prepareStatement("SELECT * FROM TRISTANROSALES.TBLUSERSANDADMINS "
                    + "WHERE USER_EMAILADDRESS=?");
            pstmt.setString(1, user_emailAddress);
            
            rs = pstmt.executeQuery();
            
            if(rs.next()){
                userType = rs.getString("USER_TYPE");
                
                if(userType.equals("IT Administrator")){
                    statusOfUpdateStatusOfUserInReservation.addProperty("isUpdatingStatusOfUserInReservationSuccess", "accessDenied");
                    response.getWriter().write(statusOfUpdateStatusOfUserInReservation.toString());
                }else{
                    updateStatusOfUserInReservation(response);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(UpdateStatusOfUserInReservation.class.getName()).log(Level.SEVERE, null, ex);
            response.getWriter().write(ex.getMessage());
        }
    }
    
    public void updateStatusOfUserInReservation(HttpServletResponse response) throws IOException{
        try {
            pstmt = con.prepareStatement("UPDATE TRISTANROSALES.TBLRESERVATIONSREPORT "
                    + "SET RESERVATION_STATUS=?,RESERVATION_DATEANDTIMERESERVED=? WHERE RESERVATION_REFERENCENUMBER=?");
            pstmt.setString(1, status);
            pstmt.setString(2, new_dateAndTimeReserved);
            pstmt.setString(3, reservation_referenceNumber);
            
            count = pstmt.executeUpdate();
            
            if(count > 0){
                statusOfUpdateStatusOfUserInReservation.addProperty("isUpdatingStatusOfUserInReservationSuccess", true);
                response.getWriter().write(statusOfUpdateStatusOfUserInReservation.toString()); 
            }else{
                response.getWriter().write("Error!");
            }
            
            con.close(); 
        } catch (SQLException ex) {
            Logger.getLogger(UpdateStatusOfUserInReservation.class.getName()).log(Level.SEVERE, null, ex);
            response.getWriter().write(ex.getMessage());
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
