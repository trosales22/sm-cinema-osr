package com.smcinema.admin;

import com.google.gson.JsonObject;
import com.smcinema.classes.DatabaseManager;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SaveChangesToBlockedSeats extends HttpServlet {
    Connection con;
    PreparedStatement pstmt;
    Statement stmt;
    ResultSet rs;
    DatabaseMetaData dbmd;
    JsonObject status = new JsonObject();
    PrintWriter print;
    
    String query,movieAuditoriumName,movieBlockedSeats,dateAndTimeCreated;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            
            con = DatabaseManager.getConnection();
            dbmd = con.getMetaData();
            
            movieAuditoriumName = request.getParameter("movieAuditoriumName");
            movieBlockedSeats = request.getParameter("txtBlockedSeats");
            
            SimpleDateFormat sdf_dateAndTimeCreated = new SimpleDateFormat("MMMMM dd, yyyy | hh:mm a");
            Calendar calendarDateAndTimeCreated = Calendar.getInstance();
            
            dateAndTimeCreated = sdf_dateAndTimeCreated.format(calendarDateAndTimeCreated.getTime());
            
            rs = dbmd.getTables(null, null, "TBLBLOCKEDSEATS", null);
            if(rs.next()){
                //table exists
                saveChangesForBlockedSeating(response);
            }else{
                //table does not exist
                createTableForBlockedSeating();
                saveChangesForBlockedSeating(response);
            }
        } catch (SQLException ex) {
            Logger.getLogger(SaveChangesToBlockedSeats.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void createTableForBlockedSeating(){
        try {
            query = "CREATE TABLE TRISTANROSALES.TBLBLOCKEDSEATS ("
                + "PS_ID INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
                + "PS_AUDITORIUMNAME VARCHAR(32672) NOT NULL,"
                + "PS_BLOCKEDSEATS VARCHAR(32672) NOT NULL,"
                + "PS_DATEANDTIMECREATED VARCHAR(32672) NOT NULL)";
            
            pstmt = con.prepareStatement(query);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(SaveChangesToBlockedSeats.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void saveChangesForBlockedSeating(HttpServletResponse response) throws IOException{
        try {
            query = "INSERT INTO TRISTANROSALES.TBLBLOCKEDSEATS (PS_AUDITORIUMNAME,PS_BLOCKEDSEATS,PS_DATEANDTIMECREATED) "
                    + "VALUES (?,?,?)";
            
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, movieAuditoriumName);
            pstmt.setString(2, movieBlockedSeats);
            pstmt.setString(3, dateAndTimeCreated);
            
            stmt = con.createStatement();
                              
            pstmt.executeUpdate();
                
            status.addProperty("isBlockingOfIndividualSeatsSuccess", true);
            response.getWriter().write(status.toString());
                
            con.close();       
        } catch (SQLException ex) {
            Logger.getLogger(SaveChangesToBlockedSeats.class.getName()).log(Level.SEVERE, null, ex);
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
