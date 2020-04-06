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

public class UpdateSelectedMovieAuditoriumName extends HttpServlet {
    Connection con;
    PreparedStatement pstmt;
    Statement stmt;
    ResultSet rs;
    JsonObject status = new JsonObject();
    
    String user_emailAddress,userType,selected_movieAuditoriumName,new_movieAuditoriumName,movieAuditoriumName_totalNumberOfSeats;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            
            con = DatabaseManager.getConnection();
            
            user_emailAddress = (String) request.getSession().getAttribute("COOKIE_USER_EMAILADDRESS");
            selected_movieAuditoriumName = (String) request.getSession().getAttribute("MOVIE_AUDITORIUMNAME");
            new_movieAuditoriumName = request.getParameter("txtMovieAuditoriumName");
            movieAuditoriumName_totalNumberOfSeats = request.getParameter("txtMovieAuditoriumName_totalNumberOfSeats");
            
            stmt = con.createStatement();
            
            rs = stmt.executeQuery("SELECT RESERVATION_MOVIEAUDITORIUMNAME FROM TRISTANROSALES.TBLRESERVATIONSREPORT "
                    + "WHERE RESERVATION_MOVIEAUDITORIUMNAME LIKE '%" + new_movieAuditoriumName + "%'");
            
            if(rs.next()){    
                status.addProperty("isUpdateMovieAuditoriumSuccess", "reservationIsInProgress");
                response.getWriter().write(status.toString());
            }else{
                detectIfUserIsAnITSupervisor(response);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UpdateSelectedMovieAuditoriumName.class.getName()).log(Level.SEVERE, null, ex);
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
                    updateSelectedMovieAuditoriumName(response);
                }else{
                    status.addProperty("isUpdateMovieAuditoriumSuccess", "accessDenied");
                    response.getWriter().write(status.toString());
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(UpdateSelectedMovieAuditoriumName.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void updateSelectedMovieAuditoriumName(HttpServletResponse response) throws IOException{
        try {
            rs = stmt.executeQuery("SELECT MOVIE_AUDITORIUMNAME FROM TRISTANROSALES.TBLMOVIEAUDITORIUM "
                    + "WHERE MOVIE_AUDITORIUMNAME LIKE '%" + new_movieAuditoriumName + "%'");
            
            if(rs.next()){    
                status.addProperty("isUpdateMovieAuditoriumSuccess", "existingMovieAuditoriumName");
                response.getWriter().write(status.toString());
            }else{
                pstmt = con.prepareStatement("UPDATE TRISTANROSALES.TBLMOVIEAUDITORIUM "
                        + "SET MOVIE_AUDITORIUMNAME=?,MOVIE_TOTALNUMBEROFSEATS=? WHERE MOVIE_AUDITORIUMNAME=?");
                pstmt.setString(1, new_movieAuditoriumName);
                pstmt.setString(2, movieAuditoriumName_totalNumberOfSeats);
                pstmt.setString(3, selected_movieAuditoriumName);
                
                pstmt.executeUpdate();

                status.addProperty("isUpdateMovieAuditoriumSuccess", true);
                response.getWriter().write(status.toString());  
            }
            
            con.close(); 
        } catch (SQLException ex) {
            Logger.getLogger(UpdateSelectedMovieAuditoriumName.class.getName()).log(Level.SEVERE, null, ex);
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
