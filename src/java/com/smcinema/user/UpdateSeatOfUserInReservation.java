package com.smcinema.user;

import com.google.gson.JsonObject;
import com.smcinema.classes.DatabaseManager;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UpdateSeatOfUserInReservation extends HttpServlet {
    Connection con;
    PreparedStatement pstmt;
    ResultSet rs;
    JsonObject status = new JsonObject();
    
    String selectedSeats,selectedSeats_improvedName,reservation_referenceNumber,reservation_seatQuantity;
    int counter;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        
        con = DatabaseManager.getConnection();
        
        counter = Integer.parseInt(request.getParameter("txtCounter"));
        selectedSeats = request.getParameter("txtBlockedSeats");
        selectedSeats_improvedName = request.getParameter("txtBlockedSeats_improvedName");
        reservation_referenceNumber = (String)request.getSession().getAttribute("reservation_movieReferenceNumber");
        reservation_seatQuantity = (String)request.getSession().getAttribute("reservation_movieQuantity");
        
        detectIfSelectedSeatsExceedsTheReservationSeatQuantity(response);
    }
    
    public void detectIfSelectedSeatsExceedsTheReservationSeatQuantity(HttpServletResponse response) throws IOException{
        if(counter > Integer.parseInt(reservation_seatQuantity)){
            status.addProperty("isUpdateSeatOfUserInReservationSuccess", "exceeds");
            response.getWriter().write(status.toString());
        }else{
            updateSeatOfUserInReservation(response);
        }
    }
    
    public void updateSeatOfUserInReservation(HttpServletResponse response) throws IOException{
        try {
            pstmt = con.prepareStatement("UPDATE TRISTANROSALES.TBLRESERVATIONSREPORT "
                    + "SET RESERVATION_OCCUPIEDSEATS=?,RESERVATION_OCCUPIEDSEATS_IMPROVEDNAME=? "
                    + "WHERE RESERVATION_REFERENCENUMBER=?");
            pstmt.setString(1, selectedSeats);
            pstmt.setString(2, selectedSeats_improvedName);
            pstmt.setString(3, reservation_referenceNumber);
            
            pstmt.executeUpdate();
            
            status.addProperty("isUpdateSeatOfUserInReservationSuccess", true);
            response.getWriter().write(status.toString());
            
            con.close(); 
        } catch (SQLException ex) {
            Logger.getLogger(UpdateSeatOfUserInReservation.class.getName()).log(Level.SEVERE, null, ex);
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
