package com.smcinema.admin;

import com.google.gson.JsonObject;
import com.smcinema.classes.DatabaseManager;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ChangeAdminPassword extends HttpServlet {
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    Statement stmt = null;
    
    PrintWriter print;
    String admin_emailAddress,admin_oldPassword,admin_newPassword;
    
    JsonObject status = new JsonObject();
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            
            con = DatabaseManager.getConnection();
            
            admin_emailAddress = request.getParameter("admin_emailAddress");
            admin_oldPassword = request.getParameter("txtADMIN_oldPassword");
            admin_newPassword = request.getParameter("txtADMIN_newPassword");
            
            pstmt = con.prepareStatement("SELECT * FROM TRISTANROSALES.TBLUSERSANDADMINS "
                        + "WHERE USER_EMAILADDRESS=? AND USER_PASSWORD=?");
            pstmt.setString(1, admin_emailAddress);
            pstmt.setString(2, admin_oldPassword);
            
            rs = pstmt.executeQuery();
            
            if(rs.next()){
                pstmt = con.prepareStatement("UPDATE TRISTANROSALES.TBLUSERSANDADMINS SET USER_PASSWORD=? WHERE USER_EMAILADDRESS=?");
                pstmt.setString(1, admin_newPassword);
                pstmt.setString(2, admin_emailAddress);
                pstmt.executeUpdate();
                
                status.addProperty("isChangingPasswordForAdminSuccess", true);
                response.getWriter().write(status.toString());
            }else{
                status.addProperty("isChangingPasswordForAdminSuccess", false);
                response.getWriter().write(status.toString());
            }
        } catch (SQLException ex) {
            Logger.getLogger(ChangeAdminPassword.class.getName()).log(Level.SEVERE, null, ex);
            print.println(ex.getMessage() + "<br>");
            print.flush();
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
