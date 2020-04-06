package com.smcinema.user;

import com.smcinema.classes.DatabaseManager;
import java.io.IOException;
import java.io.PrintWriter;
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

public class ShowDetailsOfSelectedReservation extends HttpServlet {
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs;
    
    PrintWriter print;
    
    String user_emailAddress,reference_number,reservation_movieName,reservation_movieBranchName,reservation_movieAuditoriumName,
            reservation_movieReferenceNumber,reservation_movieQuantity,reservation_movieAmount,
            reservation_movieSchedule,reservation_screeningDate,reservation_screeningTime,reservation_typeOfSeating,reservation_status,reservation_modeOfPayment,
            reservation_transactionDate,reservation_paymentDue,reservation_occupiedSeats;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            response.setContentType("text/html;charset=UTF-8");
            
            con = DatabaseManager.getConnection();
            
            user_emailAddress = request.getParameter("user_emailAddress");
            reference_number = request.getParameter("reference_number");
 
            pstmt = con.prepareStatement("SELECT * FROM TRISTANROSALES.TBLRESERVATIONSREPORT "
                    + "WHERE RESERVATION_REFERENCENUMBER=?");
            pstmt.setString(1, reference_number);
            
            rs = pstmt.executeQuery();
            
            while(rs.next()){
                //get resultset value
                reservation_movieName = rs.getString("RESERVATION_MOVIENAME");
                reservation_movieBranchName = rs.getString("RESERVATION_MOVIEBRANCHNAME");
                reservation_movieAuditoriumName = rs.getString("RESERVATION_MOVIEAUDITORIUMNAME");
                reservation_movieReferenceNumber = rs.getString("RESERVATION_REFERENCENUMBER");
                reservation_movieQuantity = rs.getString("RESERVATION_MOVIESEATQUANTITY");
                reservation_movieAmount = rs.getString("RESERVATION_MOVIEPRICE");
                
                reservation_screeningDate = rs.getString("RESERVATION_MOVIESCREENINGDATE");
                reservation_screeningTime = rs.getString("RESERVATION_MOVIESTARTTIME");
                
                reservation_movieSchedule = rs.getString("RESERVATION_MOVIESCREENINGDATE") + "-" + rs.getString("RESERVATION_MOVIESTARTTIME");
                reservation_typeOfSeating = rs.getString("RESERVATION_MOVIETYPEOFSEATING");
                reservation_occupiedSeats = rs.getString("RESERVATION_OCCUPIEDSEATS_IMPROVEDNAME");
                reservation_modeOfPayment = rs.getString("RESERVATION_MODEOFPAYMENT");
                reservation_paymentDue = rs.getString("RESERVATION_PAYMENTDUE"); 
                reservation_status = rs.getString("RESERVATION_STATUS");
                reservation_transactionDate = rs.getString("RESERVATION_DATEANDTIMERESERVED"); 
                
                //ATTRIBUTE
                request.getSession().setAttribute("reservation_movieReferenceNumber", reservation_movieReferenceNumber);
                request.getSession().setAttribute("reservation_movieName", reservation_movieName);
                request.getSession().setAttribute("reservation_movieBranchName", reservation_movieBranchName);
                request.getSession().setAttribute("reservation_movieAuditoriumName", reservation_movieAuditoriumName);
                request.getSession().setAttribute("reservation_typeOfSeating", reservation_typeOfSeating);
                
                request.getSession().setAttribute("reservation_screeningDate", reservation_screeningDate);
                request.getSession().setAttribute("reservation_screeningTime", reservation_screeningTime);
                
                request.getSession().setAttribute("reservation_movieSchedule", reservation_movieSchedule);
                request.getSession().setAttribute("reservation_movieAmount", reservation_movieAmount);
                request.getSession().setAttribute("reservation_movieQuantity", reservation_movieQuantity);
                request.getSession().setAttribute("reservation_occupiedSeats", reservation_occupiedSeats); 
                request.getSession().setAttribute("reservation_status", reservation_status);
                request.getSession().setAttribute("reservation_modeOfPayment", reservation_modeOfPayment);
                request.getSession().setAttribute("reservation_paymentDue", reservation_paymentDue);
                request.getSession().setAttribute("reservation_transactionDate", reservation_transactionDate);
                
                response.sendRedirect(request.getContextPath() + "/pages/USER/myAccount.jsp?user_emailAddress=" 
                        + user_emailAddress + "&reference_number=" + reference_number + "#viewReservation");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ShowDetailsOfSelectedReservation.class.getName()).log(Level.SEVERE, null, ex);
            print.println(ex.getMessage() + "<br>");
            print.flush();
        }finally{
            if(pstmt != null){
                try {
                    pstmt.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ShowDetailsOfSelectedReservation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            if(con != null){
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ShowDetailsOfSelectedReservation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        print = response.getWriter();
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        print = response.getWriter();
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
