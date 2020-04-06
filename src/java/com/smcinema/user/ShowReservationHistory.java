package com.smcinema.user;

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
import org.json.JSONArray;
import org.json.JSONObject;

public class ShowReservationHistory extends HttpServlet {
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs;
    
    String user_emailAddress,reservation_movieName,reservation_movieBranchName,reservation_movieAuditoriumName,
            reservation_movieReferenceNumber,reservation_movieQuantity,reservation_movieAmount,
            reservation_movieSchedule,reservation_typeOfSeating,reservation_status,reservation_modeOfPayment,
            reservation_transactionDate,reservation_paymentDue,reservation_occupiedSeats;

    JSONObject JSONReservationsReport = new JSONObject();
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            
            con = DatabaseManager.getConnection();
            
            user_emailAddress = request.getParameter("user_emailAddress");
 
            pstmt = con.prepareStatement("SELECT * FROM TRISTANROSALES.TBLRESERVATIONSREPORT "
                    + "WHERE RESERVATION_MOVIERESERVEDBY=?");
            pstmt.setString(1, user_emailAddress);
            
            rs = pstmt.executeQuery();
            
            JSONArray jsonArray = new JSONArray();
            
            while(rs.next()){
                JSONObject jsonObject = new JSONObject();
                 
                //get resultset value
                reservation_movieName = rs.getString("RESERVATION_MOVIENAME");
                reservation_movieBranchName = rs.getString("RESERVATION_MOVIEBRANCHNAME");
                reservation_movieAuditoriumName = rs.getString("RESERVATION_MOVIEAUDITORIUMNAME");
                reservation_movieReferenceNumber = rs.getString("RESERVATION_REFERENCENUMBER");
                reservation_movieQuantity = rs.getString("RESERVATION_MOVIESEATQUANTITY");
                reservation_movieAmount = rs.getString("RESERVATION_MOVIEPRICE");
                reservation_movieSchedule = rs.getString("RESERVATION_MOVIESCREENINGDATE") + "-" + rs.getString("RESERVATION_MOVIESTARTTIME");
                reservation_typeOfSeating = rs.getString("RESERVATION_MOVIETYPEOFSEATING");
                reservation_occupiedSeats = rs.getString("RESERVATION_OCCUPIEDSEATS");
                reservation_modeOfPayment = rs.getString("RESERVATION_MODEOFPAYMENT");
                reservation_paymentDue = rs.getString("RESERVATION_PAYMENTDUE"); 
                reservation_status = rs.getString("RESERVATION_STATUS");
                reservation_transactionDate = rs.getString("RESERVATION_DATEANDTIMERESERVED");
                        
                //JSON
                jsonObject.put("reservation_movieName", reservation_movieName);
                jsonObject.put("reservation_movieBranchName", reservation_movieBranchName);
                jsonObject.put("reservation_movieAuditoriumName", reservation_movieAuditoriumName);
                jsonObject.put("reservation_movieReferenceNumber", reservation_movieReferenceNumber);
                jsonObject.put("reservation_movieQuantity", reservation_movieQuantity);
                jsonObject.put("reservation_movieAmount", reservation_movieAmount);
                jsonObject.put("reservation_movieSchedule", reservation_movieSchedule);
                jsonObject.put("reservation_status", reservation_status);
                jsonObject.put("reservation_transactionDate", reservation_transactionDate);  
                
                jsonArray.put(jsonObject);
            }
            
            JSONReservationsReport.put("RESERVATIONSREPORT", jsonArray);
            response.getWriter().write(JSONReservationsReport.toString());
        } catch (SQLException ex) {
            Logger.getLogger(ShowReservationHistory.class.getName()).log(Level.SEVERE, null, ex);
            response.getWriter().write(ex.getMessage());
        }finally{
            if(pstmt != null){
                try {
                    pstmt.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ShowReservationHistory.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            if(con != null){
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ShowReservationHistory.class.getName()).log(Level.SEVERE, null, ex);
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
