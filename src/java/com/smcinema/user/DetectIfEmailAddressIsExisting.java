package com.smcinema.user;

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

public class DetectIfEmailAddressIsExisting extends HttpServlet {
    Connection con;
    PreparedStatement pstmt;
    ResultSet rs;
    Statement stmt;
   
    PrintWriter print;
    String user_emailAddress,user_fullname;
    int count;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            JsonObject status = new JsonObject();
        
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
        
            con = DatabaseManager.getConnection();
            
            user_emailAddress = request.getParameter("txtEmailAddress");
            stmt = con.createStatement();
            
            rs = stmt.executeQuery("SELECT * FROM TRISTANROSALES.TBLUSERSANDADMINS "
                    + "WHERE USER_EMAILADDRESS LIKE '%" + user_emailAddress + "%'");
            
            if(rs.next()){
                request.getSession().setAttribute("user_emailAddress", user_emailAddress);
                user_fullname = rs.getString("USER_FIRSTNAME") + " " + rs.getString("USER_LASTNAME");
                request.getSession().setAttribute("user_fullname", user_fullname);
                
                status.addProperty("isExisting", true);
                response.getWriter().write(status.toString());
            }else{
                status.addProperty("isExisting", false);
                response.getWriter().write(status.toString());
            }
        } catch (SQLException ex) {
            Logger.getLogger(DetectIfEmailAddressIsExisting.class.getName()).log(Level.SEVERE, null, ex);
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
