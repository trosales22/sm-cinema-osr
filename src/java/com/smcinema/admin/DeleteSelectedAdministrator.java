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

public class DeleteSelectedAdministrator extends HttpServlet {
    PrintWriter print;
    Connection con;
    PreparedStatement pstmt;
    Statement stmt;
    ResultSet rs;
    
    JsonObject status = new JsonObject();
    String user_emailAddress,selected_emailAddress,userType;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
            
        con = DatabaseManager.getConnection();
        
        user_emailAddress = (String) request.getSession().getAttribute("COOKIE_USER_EMAILADDRESS");
        selected_emailAddress = (String) request.getSession().getAttribute("USER_EMAILADDRESS");
       
        detectIfUserIsAnITAdministrator(response);
    }
    
    public void detectIfUserIsAnITAdministrator(HttpServletResponse response) throws IOException{
        try {
            pstmt = con.prepareStatement("SELECT * FROM TRISTANROSALES.TBLUSERSANDADMINS "
                    + "WHERE USER_EMAILADDRESS=?");
            pstmt.setString(1, user_emailAddress);
            
            rs = pstmt.executeQuery();
            
            if(rs.next()){
                userType = rs.getString("USER_TYPE");
                
                if(userType.equals("IT Administrator")){
                    deleteSelectedAdministrator(response);
                }else{
                    status.addProperty("isDeletingAdministratorSuccess", "accessDenied");
                    response.getWriter().write(status.toString());
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DeleteSelectedAdministrator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void deleteSelectedAdministrator(HttpServletResponse response) throws IOException{
        try {
            pstmt = con.prepareStatement("DELETE FROM TRISTANROSALES.TBLUSERSANDADMINS "
                    + "WHERE USER_EMAILADDRESS=?");
            pstmt.setString(1, selected_emailAddress);

            pstmt.executeUpdate();

            status.addProperty("isDeletingAdministratorSuccess", true);
            response.getWriter().write(status.toString());

            con.close(); 
        } catch (SQLException ex) {
            Logger.getLogger(DeleteSelectedAdministrator.class.getName()).log(Level.SEVERE, null, ex);
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
