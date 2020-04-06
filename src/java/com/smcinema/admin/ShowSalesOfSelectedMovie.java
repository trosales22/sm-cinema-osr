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

public class ShowSalesOfSelectedMovie extends HttpServlet {
    Connection con;
    PreparedStatement pstmt;
    ResultSet rs;
    PrintWriter print;
    JsonObject status = new JsonObject();
    
    String user_emailAddress,userType,movieName,totalSalesOfSelectedMovie;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        
        con = DatabaseManager.getConnection();
        user_emailAddress = (String) request.getSession().getAttribute("COOKIE_USER_EMAILADDRESS");
        movieName = request.getParameter("movie_name");
        
        detectIfUserIsAnITSupervisor(request,response);
    }

    public void detectIfUserIsAnITSupervisor(HttpServletRequest request, HttpServletResponse response) throws IOException{
        try {
            pstmt = con.prepareStatement("SELECT * FROM TRISTANROSALES.TBLUSERSANDADMINS "
                    + "WHERE USER_EMAILADDRESS=?");
            pstmt.setString(1, user_emailAddress);
            
            rs = pstmt.executeQuery();
            
            if(rs.next()){
                userType = rs.getString("USER_TYPE");
                
                if(!userType.equals("Operations Supervisor")){
                    status.addProperty("isShowSalesOfSelectedMovieSuccess", "accessDenied");
                    response.getWriter().write(status.toString());
                }else{
                    showSalesOfSelectedMovie(request,response);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ShowSalesOfSelectedMovie.class.getName()).log(Level.SEVERE, null, ex);
            print.println(ex.getMessage() + "<br>");
            print.flush();
        }
    }
    
    public void showSalesOfSelectedMovie(HttpServletRequest request, HttpServletResponse response) throws IOException{
        try {
            pstmt = con.prepareStatement("SELECT SUM(CAST(RESERVATION_MOVIESEATQUANTITY AS INT)) * SUM(CAST(RESERVATION_MOVIEPRICE AS DECIMAL(9,2))) + 20"
                    + "AS TOTALSALESFORSELECTEDMOVIE FROM TRISTANROSALES.TBLRESERVATIONSREPORT "
                    + "WHERE RESERVATION_STATUS='Paid' AND RESERVATION_MOVIENAME LIKE '%" + movieName + "%'");
            
            rs = pstmt.executeQuery();
            
            while(rs.next()){
                request.getSession().setAttribute("movieName", movieName);
                
                totalSalesOfSelectedMovie = rs.getString("TOTALSALESFORSELECTEDMOVIE");
                
                if(totalSalesOfSelectedMovie == null){            
                    status.addProperty("isShowSalesOfSelectedMovieSuccess", false);
                    response.getWriter().write(status.toString());
                    
                    //request.getSession().setAttribute("totalSalesOfSelectedMovie", "There are no sales for this movie. Please try again.");
                }else{
                    request.getSession().setAttribute("totalSalesOfSelectedMovie", "&#8369;" + totalSalesOfSelectedMovie);
                    status.addProperty("isShowSalesOfSelectedMovieSuccess", true);
                    response.getWriter().write(status.toString());
                }
            }  
        } catch (SQLException ex) {
            Logger.getLogger(ShowSalesOfSelectedMovie.class.getName()).log(Level.SEVERE, null, ex);
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
