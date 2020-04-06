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

public class ShowUpdateStatusOfUserInReservation extends HttpServlet {
    Connection con;
    PreparedStatement pstmt;
    ResultSet rs;
    
    String reservedBy,referenceNumber;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            response.setContentType("text/html;charset=UTF-8");
            con = DatabaseManager.getConnection();
            
            referenceNumber = request.getParameter("referenceNumber");
            reservedBy = request.getParameter("reservedBy");
            
            pstmt = con.prepareStatement("SELECT * FROM TRISTANROSALES.TBLRESERVATIONSREPORT "
                    + "WHERE RESERVATION_REFERENCENUMBER=? AND RESERVATION_MOVIERESERVEDBY=?");
            pstmt.setString(1, referenceNumber);
            pstmt.setString(2, reservedBy);
            
            rs = pstmt.executeQuery();
            
            while(rs.next()){
                referenceNumber = rs.getString("RESERVATION_REFERENCENUMBER");
                reservedBy = rs.getString("RESERVATION_REFERENCENUMBER");
                
                //ATTRIBUTE
                request.getSession().setAttribute("referenceNumber", referenceNumber);
                request.getSession().setAttribute("reservedBy", reservedBy);
                
                response.sendRedirect(request.getContextPath() + "/pages/ADMIN/adminHomePage.jsp#updateStatusOfUserInReservation");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ShowUpdateStatusOfUserInReservation.class.getName()).log(Level.SEVERE, null, ex);
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
