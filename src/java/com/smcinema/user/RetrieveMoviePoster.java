package com.smcinema.user;

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

public class RetrieveMoviePoster extends HttpServlet {   
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs;
    
    PrintWriter print;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            response.setContentType("text/html;charset=UTF-8");
            con = DatabaseManager.getConnection();
            
            pstmt = con.prepareStatement("SELECT MOVIE_POSTER FROM TRISTANROSALES.TBLMOVIECINEMA WHERE MOVIE_ID=?");
            pstmt.setLong(1, Long.valueOf(request.getParameter("movie_id")));
            
            rs = pstmt.executeQuery();
            
            if(rs.next()){
                response.getOutputStream().write(rs.getBytes("MOVIE_POSTER"));
            }
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(RetrieveMoviePoster.class.getName()).log(Level.SEVERE, null, ex);
            print.println(ex.getMessage() + "<br>");
            print.flush();
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
        print = response.getWriter();
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
