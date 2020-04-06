package com.smcinema.admin;

import com.google.gson.JsonObject;
import com.smcinema.classes.DatabaseManager;
import java.io.IOException;
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

public class UpdateStatusOfAccountUserOrAdmin extends HttpServlet {
    Connection con;
    PreparedStatement pstmt;
    Statement stmt;
    ResultSet rs;
    JsonObject status = new JsonObject();
    
    String user_emailAddress,selected_userEmailAddress,loggedIn_userType,userStatus,userType;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");

        con = DatabaseManager.getConnection();

        user_emailAddress = (String) request.getSession().getAttribute("COOKIE_USER_EMAILADDRESS");
        selected_userEmailAddress = (String) request.getSession().getAttribute("USER_EMAILADDRESS");
        userType = request.getParameter("txtUserType");
        userStatus = request.getParameter("txtUserStatus");

        detectIfUserIsAnITAdministrator(response);
    }
    
    public void detectIfUserIsAnITAdministrator(HttpServletResponse response) throws IOException{
        try {
            pstmt = con.prepareStatement("SELECT * FROM TRISTANROSALES.TBLUSERSANDADMINS "
                    + "WHERE USER_EMAILADDRESS=?");
            pstmt.setString(1, user_emailAddress);
            
            rs = pstmt.executeQuery();
            
            if(rs.next()){
                loggedIn_userType = rs.getString("USER_TYPE");
                
                if(loggedIn_userType.equals("IT Administrator")){
                    updateStatusOfSelectedAccountUserOrAdmin(response);
                }else{
                    status.addProperty("isUpdateStatusOfAccountUserOrAdminSuccess", "accessDenied");
                    response.getWriter().write(status.toString());
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(UpdateStatusOfAccountUserOrAdmin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void updateStatusOfSelectedAccountUserOrAdmin(HttpServletResponse response) throws IOException{
        try {
            pstmt = con.prepareStatement("UPDATE TRISTANROSALES.TBLUSERSANDADMINS "
                        + "SET USER_STATUS=?,USER_TYPE=? WHERE USER_EMAILADDRESS=?");
            pstmt.setString(1, userStatus);
            pstmt.setString(2, userType);
            pstmt.setString(3, selected_userEmailAddress);

            pstmt.executeUpdate();

            status.addProperty("isUpdateStatusOfAccountUserOrAdminSuccess", true);
            response.getWriter().write(status.toString());
            
            con.close(); 
        } catch (SQLException ex) {
            Logger.getLogger(UpdateStatusOfAccountUserOrAdmin.class.getName()).log(Level.SEVERE, null, ex);
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
