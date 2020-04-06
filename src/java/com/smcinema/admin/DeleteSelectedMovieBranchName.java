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

public class DeleteSelectedMovieBranchName extends HttpServlet {
    PrintWriter print;
    Connection con;
    PreparedStatement pstmt;
    Statement stmt;
    ResultSet rs;
    
    String user_emailAddress,movieBranchName,userType;
    JsonObject status = new JsonObject();
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            
            con = DatabaseManager.getConnection();
            
            user_emailAddress = (String) request.getSession().getAttribute("COOKIE_USER_EMAILADDRESS");
            movieBranchName = (String) request.getSession().getAttribute("MOVIE_BRANCHNAME");
            
            stmt = con.createStatement();
            
            rs = stmt.executeQuery("SELECT RESERVATION_MOVIEBRANCHNAME FROM TRISTANROSALES.TBLRESERVATIONSREPORT "
                    + "WHERE RESERVATION_MOVIEBRANCHNAME LIKE '%" + movieBranchName + "%'");
            
            if(rs.next()){    
                status.addProperty("isDeletingMovieBranchNameSuccess", "reservationIsInProgress");
                response.getWriter().write(status.toString());
            }else{
                detectIfUserIsAnITSupervisor(response);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DeleteSelectedMovieBranchName.class.getName()).log(Level.SEVERE, null, ex);
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
                    deleteSelectedMovieBranchName(response);
                }else{
                    status.addProperty("isDeletingMovieBranchNameSuccess", "accessDenied");
                    response.getWriter().write(status.toString());
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DeleteSelectedMovie.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void deleteSelectedMovieBranchName(HttpServletResponse response) throws IOException{
        try {
            pstmt = con.prepareStatement("DELETE FROM TRISTANROSALES.TBLMOVIEBRANCHES "
                    + "WHERE MOVIE_BRANCHNAME=?");
            pstmt.setString(1, movieBranchName);

            pstmt.executeUpdate();

            status.addProperty("isDeletingMovieBranchNameSuccess", true);
            response.getWriter().write(status.toString());

            con.close(); 
        } catch (SQLException ex) {
            Logger.getLogger(DeleteSelectedMovie.class.getName()).log(Level.SEVERE, null, ex);
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
