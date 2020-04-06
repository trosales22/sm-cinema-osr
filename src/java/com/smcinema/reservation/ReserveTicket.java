package com.smcinema.reservation;

import com.google.gson.JsonObject;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ReserveTicket extends HttpServlet {
    JsonObject status = new JsonObject();
    
    String selected_movieName,selected_movieBranch,selected_movieAuditorium,selected_movieAvailableDay,
            selected_movieSchedule,selected_seatQuantity,selected_movieTypeOfSeating;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        
        String user_emailAddress = null;
        Cookie[] cookies = request.getCookies();
        if(cookies !=null){
            for(Cookie cookie : cookies){
                if(cookie.getName().equals("user_emailAddress")){
                    user_emailAddress = cookie.getValue();
                }
            }
        }
                    
        selected_movieName = request.getParameter("movie_name");
        selected_movieBranch = request.getParameter("txtSelectMovieBranch");
        selected_movieAuditorium = request.getParameter("txtSelectMovieAuditorium");
        selected_movieAvailableDay = request.getParameter("txtSelectMovieAvailableDay");
        selected_movieSchedule = request.getParameter("txtSelectMovieSchedule");
        selected_seatQuantity = request.getParameter("txtChooseSeatQuantity");
        selected_movieTypeOfSeating = request.getParameter("movie_typeOfSeating");
        
        request.getSession().setAttribute("user_emailAddress", user_emailAddress);
        request.getSession().setAttribute("selected_movieName", selected_movieName);
        request.getSession().setAttribute("selected_movieBranch", selected_movieBranch);
        request.getSession().setAttribute("selected_movieAuditorium", selected_movieAuditorium);
        request.getSession().setAttribute("selected_movieAvailableDay", selected_movieAvailableDay);
        request.getSession().setAttribute("selected_movieSchedule", selected_movieSchedule);
        request.getSession().setAttribute("selected_seatQuantity", selected_seatQuantity);
        request.getSession().setAttribute("selectedSeats_improvedName", "N/A");
        
        switch (selected_movieTypeOfSeating) {
            case "Free Seating":
                if(user_emailAddress == null){
                    status.addProperty("isUserLoggedIn", false);
                    response.getWriter().write(status.toString());
                }else{
                    response.sendRedirect(request.getContextPath() + "/pages/USER/buyTickets.jsp");
                }
                
                break;
            case "Preferred Seating":
                if(user_emailAddress == null){
                    status.addProperty("isUserLoggedIn", false);
                    response.getWriter().write(status.toString());
                }else{
                    response.sendRedirect(request.getContextPath() + "/pages/USER/buyTicketsAndChoosePreferredSeating.jsp");  
                }
                
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
