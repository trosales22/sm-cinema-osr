package com.smcinema.admin;

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

public class ShowAllReservationsReport extends HttpServlet {
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs;
    JSONObject JSONReservationsReport = new JSONObject();
    
    String userLoggedIn_type,reservation_movieName,reservation_movieBranchName,reservation_movieAuditoriumName,
            reservation_movieReferenceNumber,reservation_movieQuantity,reservation_movieAmount,
            reservation_movieSchedule,reservation_paymentDue,reservation_status,reservation_reservedBy,
            reservation_transactionDate,action;
    int reservation_totalAmount;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            
            con = DatabaseManager.getConnection();
            userLoggedIn_type = (String) request.getSession().getAttribute("COOKIE_USER_TYPE");
            pstmt = con.prepareStatement("SELECT * FROM TRISTANROSALES.TBLRESERVATIONSREPORT");
            
            rs = pstmt.executeQuery();
            
            JSONArray jsonArray = new JSONArray();
            
            while(rs.next()){
                JSONObject jsonObject = new JSONObject();
                 
                reservation_movieName = rs.getString("RESERVATION_MOVIENAME");
                reservation_movieBranchName = rs.getString("RESERVATION_MOVIEBRANCHNAME");
                reservation_movieAuditoriumName = rs.getString("RESERVATION_MOVIEAUDITORIUMNAME");

                reservation_movieReferenceNumber = rs.getString("RESERVATION_REFERENCENUMBER");
                reservation_movieQuantity = rs.getString("RESERVATION_MOVIESEATQUANTITY");
                reservation_movieAmount = rs.getString("RESERVATION_MOVIEPRICE");
                reservation_movieSchedule = rs.getString("RESERVATION_MOVIESCREENINGDATE") + "-" + rs.getString("RESERVATION_MOVIESTARTTIME");
                reservation_paymentDue = rs.getString("RESERVATION_PAYMENTDUE");
                reservation_status = rs.getString("RESERVATION_STATUS");
                reservation_reservedBy = rs.getString("RESERVATION_MOVIERESERVEDBY");
                
                reservation_transactionDate = rs.getString("RESERVATION_DATEANDTIMERESERVED");
                
                reservation_totalAmount = (int) ((int)(Double.parseDouble(reservation_movieAmount) * Double.parseDouble(reservation_movieQuantity)) + 20 * Double.parseDouble(reservation_movieQuantity));
                
            
                request.getSession().setAttribute("RESERVATION_REFERENCENUMBER", reservation_movieReferenceNumber);

                jsonObject.put("reservation_movieName", reservation_movieName);
                jsonObject.put("reservation_movieBranchName", reservation_movieBranchName);
                jsonObject.put("reservation_movieAuditoriumName", reservation_movieAuditoriumName);
                jsonObject.put("reservation_movieReferenceNumber", reservation_movieReferenceNumber);
                jsonObject.put("reservation_movieQuantity", reservation_movieQuantity);
                jsonObject.put("reservation_movieAmount", reservation_movieAmount);
                jsonObject.put("reservation_totalAmount", reservation_totalAmount);
                jsonObject.put("reservation_movieSchedule", reservation_movieSchedule);
                jsonObject.put("reservation_paymentDue", reservation_paymentDue);
                jsonObject.put("reservation_status", reservation_status);
                jsonObject.put("reservation_reservedBy", reservation_reservedBy);
                jsonObject.put("reservation_transactionDate", reservation_transactionDate);  
                
                if(userLoggedIn_type.equals("IT Administrator")){
                   action = "N/A";
                   jsonObject.put("action", action); 
                }else{
                    if(reservation_status.equals("Pending")){
                        action = "<a href='" + request.getContextPath() + "/ShowUpdateStatusOfUserInReservation?reservedBy=" + reservation_reservedBy + "&referenceNumber=" + reservation_movieReferenceNumber + "'><button type='button' class='btn btn-danger'>Update Status</button></a>";
                        jsonObject.put("action", action);
                    }else if(reservation_status.equals("Paid") || reservation_status.equals("Cancelled")){
                        action = "N/A";
                        jsonObject.put("action", action);
                        
                        jsonObject.put("reservation_paymentDue", "N/A");
                    }
                }
                
                jsonArray.put(jsonObject);
            }
            
            JSONReservationsReport.put("RESERVATIONSREPORT", jsonArray);
            response.getWriter().write(JSONReservationsReport.toString());
        } catch (SQLException ex) {
            Logger.getLogger(ShowAllReservationsReport.class.getName()).log(Level.SEVERE, null, ex);
            response.getWriter().write(ex.getMessage());
        }finally{
            if(pstmt != null){
                try {
                    pstmt.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ShowAllReservationsReport.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            if(con != null){
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ShowAllReservationsReport.class.getName()).log(Level.SEVERE, null, ex);
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
