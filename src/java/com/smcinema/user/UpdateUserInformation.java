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

public class UpdateUserInformation extends HttpServlet {
    Connection con;
    PreparedStatement pstmt;
    ResultSet rs;
    
    String user_firstname,user_lastname,user_nameOfFavoritePet,user_emailAddress;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            response.setContentType("text/html;charset=UTF-8");
            con = DatabaseManager.getConnection();
            
            user_firstname = request.getParameter("txtFirstname");
            user_lastname = request.getParameter("txtLastname");
            user_nameOfFavoritePet = request.getParameter("txtNameOfFavoritePet");
            user_emailAddress = (String) request.getSession().getAttribute("COOKIE_USER_EMAILADDRESS");
            
            pstmt = con.prepareStatement("UPDATE TRISTANROSALES.TBLUSERSANDADMINS "
                    + "SET USER_FIRSTNAME=?,USER_LASTNAME=?,USER_NAMEOFFAVORITEPET=? "
                    + "WHERE USER_EMAILADDRESS=?");
            pstmt.setString(1, user_firstname);
            pstmt.setString(2, user_lastname);
            pstmt.setString(3, user_nameOfFavoritePet);
            pstmt.setString(4, user_emailAddress);
            
            pstmt.executeUpdate();
              
            request.getSession().setAttribute("COOKIE_USER_FIRSTNAME", user_firstname);
            request.getSession().setAttribute("COOKIE_USER_LASTNAME", user_lastname);
            request.getSession().setAttribute("COOKIE_USER_NAMEOFFAVORITEPET", user_nameOfFavoritePet); 

            con.close(); 
        } catch (SQLException ex) {
            Logger.getLogger(UpdateUserInformation.class.getName()).log(Level.SEVERE, null, ex);
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
