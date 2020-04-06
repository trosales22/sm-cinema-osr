package com.smcinema.admin;

import com.google.gson.JsonObject;
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

public class ResetDatabaseForBlockedSeats extends HttpServlet {
    Connection con;
    PreparedStatement pstmt;
    ResultSet rs;
    PrintWriter print;
    JsonObject status = new JsonObject();
    
    String query,movieAuditoriumName,user_emailAddress,userType;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
            
        con = DatabaseManager.getConnection();
        
        movieAuditoriumName = request.getParameter("movieAuditoriumName");
        user_emailAddress = (String) request.getSession().getAttribute("COOKIE_USER_EMAILADDRESS");
        
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
                    resetDatabaseForBlockedSeats(response);
                }else{
                    status.addProperty("isResetDatabaseForBlockedSeatsSuccess", "accessDenied");
                    response.getWriter().write(status.toString());
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ResetDatabaseForBlockedSeats.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void resetDatabaseForBlockedSeats(HttpServletResponse response) throws IOException{
        try {
            query = "DELETE FROM TRISTANROSALES.TBLBLOCKEDSEATS WHERE PS_AUDITORIUMNAME=?";
            
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, movieAuditoriumName);
            
            pstmt.executeUpdate();

            status.addProperty("isResetDatabaseForBlockedSeatsSuccess", true);
            response.getWriter().write(status.toString());
        } catch (SQLException ex) {
            Logger.getLogger(ResetDatabaseForBlockedSeats.class.getName()).log(Level.SEVERE, null, ex);
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
