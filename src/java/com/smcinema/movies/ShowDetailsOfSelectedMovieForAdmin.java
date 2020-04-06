package com.smcinema.movies;

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

public class ShowDetailsOfSelectedMovieForAdmin extends HttpServlet {
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs;
    
    PrintWriter print;
    String movieID;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            response.setContentType("text/html;charset=UTF-8");
            con = DatabaseManager.getConnection();
            
            movieID = request.getParameter("movie_id");
            pstmt = con.prepareStatement("SELECT * FROM TRISTANROSALES.TBLMOVIECINEMA WHERE MOVIE_ID=?");
            pstmt.setString(1, movieID);
            
            rs = pstmt.executeQuery();
            
            while(rs.next()){
                request.getSession().setAttribute("MOVIE_ID", rs.getString("MOVIE_ID"));
                request.getSession().setAttribute("MOVIE_REFERENCE", rs.getString("MOVIE_REFERENCE"));
                request.getSession().setAttribute("MOVIE_NAME", rs.getString("MOVIE_NAME"));
                request.getSession().setAttribute("MOVIE_YEAR", rs.getString("MOVIE_YEAR"));
                request.getSession().setAttribute("MOVIE_DIRECTOR", rs.getString("MOVIE_DIRECTOR"));
                request.getSession().setAttribute("MOVIE_RELEASEDBY", rs.getString("MOVIE_RELEASEDBY"));
                request.getSession().setAttribute("MOVIE_TRAILERLINK", rs.getString("MOVIE_TRAILERLINK"));
                request.getSession().setAttribute("MOVIE_DURATION", rs.getString("MOVIE_DURATION"));
                request.getSession().setAttribute("MOVIE_PRICE", rs.getString("MOVIE_PRICE"));
                request.getSession().setAttribute("MOVIE_TYPEOFSEATING", rs.getString("MOVIE_TYPEOFSEATING")); 
                request.getSession().setAttribute("MOVIE_CAST", rs.getString("MOVIE_CAST"));
                request.getSession().setAttribute("MOVIE_SYNOPSIS", rs.getString("MOVIE_SYNOPSIS"));
                request.getSession().setAttribute("MTRCB_RATING", rs.getString("MTRCB_RATING"));
                request.getSession().setAttribute("MOVIE_GENRE", rs.getString("MOVIE_GENRE"));
                request.getSession().setAttribute("MOVIE_AUDITORIUM", rs.getString("MOVIE_AUDITORIUM"));
                request.getSession().setAttribute("MOVIE_RELEASEDATE", rs.getString("MOVIE_RELEASEDATE"));
                request.getSession().setAttribute("MOVIE_CLOSEDATE", rs.getString("MOVIE_CLOSEDATE"));
                request.getSession().setAttribute("MOVIE_STATUS", rs.getString("MOVIE_STATUS"));
                
                response.sendRedirect(request.getContextPath() + "/pages/ADMIN/adminHomePage.jsp?movie_id=" + movieID + "#showMovieDetails"); 
            }
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(ShowDetailsOfSelectedMovieForAdmin.class.getName()).log(Level.SEVERE, null, ex);
            print.println(ex.getMessage() + "<br>");
            print.flush();
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
