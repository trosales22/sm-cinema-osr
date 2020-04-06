package com.smcinema.user;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ReserveTicketForPreferredSeating extends HttpServlet {
    String selectedSeats,selectedSeats_improvedName;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        selectedSeats = request.getParameter("txtBlockedSeats");
        selectedSeats_improvedName = request.getParameter("txtBlockedSeats_improvedName");
        
        if(selectedSeats == null){
            request.getSession().setAttribute("selectedSeats", "N/A"); 
        }else{
            request.getSession().setAttribute("selectedSeats", selectedSeats);
            request.getSession().setAttribute("selectedSeats_improvedName", selectedSeats_improvedName);
        }
        
        //response.sendRedirect(request.getContextPath() + "/pages/USER/buyTickets.jsp");
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
