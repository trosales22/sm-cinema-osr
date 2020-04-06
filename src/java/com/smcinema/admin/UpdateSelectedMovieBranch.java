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

public class UpdateSelectedMovieBranch extends HttpServlet {
    Connection con;
    PreparedStatement pstmt;
    Statement stmt;
    ResultSet rs;
    JsonObject status = new JsonObject();
    
    String user_emailAddress,userType,movieBranchName,movieBranchID;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            
            con = DatabaseManager.getConnection();
            
            user_emailAddress = (String) request.getSession().getAttribute("COOKIE_USER_EMAILADDRESS");
            movieBranchName = request.getParameter("txtMovieBranchName");
            movieBranchID = (String) request.getSession().getAttribute("MOVIE_BRANCHID");
            
            stmt = con.createStatement();
            
            rs = stmt.executeQuery("SELECT RESERVATION_MOVIEBRANCHNAME FROM TRISTANROSALES.TBLRESERVATIONSREPORT "
                    + "WHERE RESERVATION_MOVIEBRANCHNAME LIKE '%" + movieBranchName + "%'");
            
            if(rs.next()){    
                status.addProperty("isUpdateMovieBranchSuccess", "reservationIsInProgress");
                response.getWriter().write(status.toString());
            }else{
                detectIfUserIsAnITSupervisor(response);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UpdateSelectedMovieBranch.class.getName()).log(Level.SEVERE, null, ex);
            detectIfUserIsAnITSupervisor(response);
        }
    }   
    
    public void detectIfUserIsAnITSupervisor(HttpServletResponse response) throws IOException{
        try {
            pstmt = con.prepareStatement("SELECT * FROM TRISTANROSALES.TBLUSERSANDADMINS "
                    + "WHERE USER_EMAILADDRESS=?");
            pstmt.setString(1, user_emailAddress);
            
            rs = pstmt.executeQuery();
            
            if(rs.next()){
                userType = rs.getString("USER_TYPE");
                
                if(userType.equals("IT Supervisor")){
                    updateSelectedMovieBranchName(response);
                }else{
                    status.addProperty("isUpdateMovieBranchSuccess", "accessDenied");
                    response.getWriter().write(status.toString());
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(UpdateSelectedMovieBranch.class.getName()).log(Level.SEVERE, null, ex);
            response.getWriter().write(ex.getMessage());
        }
    }
    
    public void updateSelectedMovieBranchName(HttpServletResponse response) throws IOException{
        try {
            rs = stmt.executeQuery("SELECT MOVIE_BRANCHNAME FROM TRISTANROSALES.TBLMOVIEBRANCHES "
                    + "WHERE MOVIE_BRANCHNAME LIKE '%" + movieBranchName + "%'");
            
            if(rs.next()){    
                status.addProperty("isUpdateMovieBranchSuccess", "existingMovieBranchName");
                response.getWriter().write(status.toString());
            }else{
                pstmt = con.prepareStatement("UPDATE TRISTANROSALES.TBLMOVIEBRANCHES "
                        + "SET MOVIE_BRANCHNAME=? WHERE MOVIE_ID=?");
                pstmt.setString(1, movieBranchName);
                pstmt.setString(2, movieBranchID);
                
                pstmt.executeUpdate();

                status.addProperty("isUpdateMovieBranchSuccess", true);
                response.getWriter().write(status.toString());  
            }
            
            con.close(); 
        } catch (SQLException ex) {
            Logger.getLogger(UpdateSelectedMovieBranch.class.getName()).log(Level.SEVERE, null, ex);
            response.getWriter().write(ex.getMessage());
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
