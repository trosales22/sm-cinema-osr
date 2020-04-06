package com.smcinema.reservation;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ProceedToPayment extends HttpServlet {
    RequestDispatcher rd;      
    
    String user_emailAddress,movieName,movieBranchName,movieAuditoriumName,
            movieTypeOfSeating,movieScreeningDate,movieStartTime,moviePrice,
            movieSeatQuantity,paymentOption,movieSelectedSeats; 
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        
        user_emailAddress = request.getParameter("user_emailAddress");
        movieName = request.getParameter("movieName");
        movieBranchName = request.getParameter("movieBranchName");
        movieAuditoriumName = request.getParameter("movieAuditoriumName");
        movieTypeOfSeating = request.getParameter("movieTypeOfSeating");
        movieScreeningDate = request.getParameter("movieScreeningDate");
        movieStartTime = request.getParameter("movieStartTime");
        moviePrice = request.getParameter("moviePrice");
        movieSeatQuantity = request.getParameter("movieSeatQuantity");    
        paymentOption = request.getParameter("txtPaymentOption");
        movieSelectedSeats = request.getParameter("movieSelectedSeats");

        
        request.setAttribute("user_emailAddress", user_emailAddress);
        request.setAttribute("movieName", movieName);
        request.setAttribute("movieBranchName", movieBranchName);
        request.setAttribute("movieAuditoriumName", movieAuditoriumName);
        request.setAttribute("movieTypeOfSeating", movieTypeOfSeating);
        request.setAttribute("movieScreeningDate", movieScreeningDate);
        request.setAttribute("movieStartTime", movieStartTime);
        request.setAttribute("moviePrice", moviePrice);
        request.setAttribute("movieSeatQuantity", movieSeatQuantity);
        request.setAttribute("paymentOption", paymentOption);
        request.setAttribute("movieSelectedSeats", movieSelectedSeats);
        
        switch (paymentOption) {
            case "On-Site Payment":
                rd = request.getRequestDispatcher("ReservationSuccess");
                rd.forward(request, response);
                //response.sendRedirect(request.getContextPath() + "/ReservationSuccess");
                break;
            case "PayPal":
                rd = request.getRequestDispatcher("ReservationSuccess");
                rd.forward(request, response);
                break;
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
