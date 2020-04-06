package com.smcinema.user;

import com.google.gson.JsonObject;
import com.smcinema.classes.DatabaseManager;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ChangeSeat extends HttpServlet {
    Connection con;
    PreparedStatement pstmt;
    ResultSet rs;
    JsonObject status = new JsonObject();
    
    String user_emailAddress,selected_movieTypeOfSeating,selected_movieStatus;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
            
        con = DatabaseManager.getConnection();
        
        user_emailAddress = (String) request.getSession().getAttribute("COOKIE_USER_EMAILADDRESS");
        selected_movieTypeOfSeating = (String) request.getSession().getAttribute("reservation_typeOfSeating");
        selected_movieStatus = (String) request.getSession().getAttribute("reservation_status"); 
        
        detecIfSelectedReservationIsAPreferredSeating(response);
    }
    
    public void detecIfSelectedReservationIsAPreferredSeating(HttpServletResponse response) throws IOException{
        if(selected_movieTypeOfSeating.equals("Preferred Seating")){
            if(selected_movieStatus.equals("Paid")){
                status.addProperty("isChangeSeatSuccess", "accessDenied");
                response.getWriter().write(status.toString());
            }else{
                status.addProperty("isChangeSeatSuccess", "preferredSeating");
                response.getWriter().write(status.toString());
            }
        }else{
            status.addProperty("isChangeSeatSuccess", "freeSeating");
            response.getWriter().write(status.toString());
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
