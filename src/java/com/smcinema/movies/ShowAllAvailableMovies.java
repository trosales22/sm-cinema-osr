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
import org.json.JSONArray;
import org.json.JSONObject;

public class ShowAllAvailableMovies extends HttpServlet {
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs;
    PrintWriter print;
    
    String movieID,movieName,movieYear;
    JSONObject JSONMovies = new JSONObject();
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            
            con = DatabaseManager.getConnection();
            pstmt = con.prepareStatement("SELECT * FROM TRISTANROSALES.TBLMOVIECINEMA");
            
            rs = pstmt.executeQuery();
            
            JSONArray jsonArray = new JSONArray();
            
            while(rs.next()){
                JSONObject jsonObject = new JSONObject();
                
                movieID = rs.getString("MOVIE_ID");
                movieName = rs.getString("MOVIE_NAME");
                movieYear = rs.getString("MOVIE_YEAR");
                
                jsonObject.put("movieID", movieID);
                jsonObject.put("movieName", movieName);
                jsonObject.put("movieYear", movieYear);  
                
                jsonArray.put(jsonObject);
            }            
            
            JSONMovies.put("MOVIES", jsonArray);
            response.getWriter().write(JSONMovies.toString());
        } catch (SQLException ex) {
            Logger.getLogger(ShowAllAvailableMovies.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            if(pstmt != null){
                try {
                    pstmt.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ShowAllAvailableMovies.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            if(con != null){
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ShowAllAvailableMovies.class.getName()).log(Level.SEVERE, null, ex);
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
