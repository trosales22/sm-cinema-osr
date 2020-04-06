package com.smcinema.admin;

import com.google.gson.Gson;
import com.smcinema.classes.DatabaseManager;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetAllMovieAuditorium extends HttpServlet {
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs;
    
    List<String> listOfMovieAuditorium = new ArrayList<>();
    String JSON_movieAuditorium = null;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            con = DatabaseManager.getConnection();
            
            pstmt = con.prepareStatement("SELECT * FROM TRISTANROSALES.TBLMOVIEAUDITORIUM");
            
            rs = pstmt.executeQuery();
            
            listOfMovieAuditorium.clear();
            
            while(rs.next()){    
                listOfMovieAuditorium.add(rs.getString("MOVIE_AUDITORIUMNAME"));
            }
            
            JSON_movieAuditorium = new Gson().toJson(listOfMovieAuditorium);  
            response.getWriter().write(JSON_movieAuditorium);
            response.setContentType("application/json");
        } catch (SQLException ex) {
            Logger.getLogger(GetAllMovieAuditorium.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            if(pstmt != null){
                try {
                    pstmt.close();
                } catch (SQLException ex) {
                    Logger.getLogger(GetAllMovieAuditorium.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            if(con != null){
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(GetAllMovieAuditorium.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
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
