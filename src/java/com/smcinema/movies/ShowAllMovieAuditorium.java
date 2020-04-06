package com.smcinema.movies;

import com.smcinema.classes.DatabaseManager;
import java.io.IOException;
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

public class ShowAllMovieAuditorium extends HttpServlet {
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs;
    JSONObject JSONMovieAuditorium = new JSONObject();
    
    String userLoggedIn_type,movieAuditoriumName,movieAuditoriumName_totalNumberOfSeats,
            movieAuditoriumName_dateAndTimeCreated,action;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            
            con = DatabaseManager.getConnection();
            userLoggedIn_type = (String) request.getSession().getAttribute("COOKIE_USER_TYPE");
            pstmt = con.prepareStatement("SELECT * FROM TRISTANROSALES.TBLMOVIEAUDITORIUM");
            
            rs = pstmt.executeQuery();
            
            JSONArray jsonArray = new JSONArray();
            
            while(rs.next()){               
                JSONObject jsonObject = new JSONObject();
                
                movieAuditoriumName = rs.getString("MOVIE_AUDITORIUMNAME");
                movieAuditoriumName_totalNumberOfSeats = rs.getString("MOVIE_TOTALNUMBEROFSEATS");
                movieAuditoriumName_dateAndTimeCreated = rs.getString("MOVIE_DATEANDTIMECREATED");
                
           
                jsonObject.put("movieAuditoriumName", movieAuditoriumName);
                jsonObject.put("movieAuditoriumName_totalNumberOfSeats", movieAuditoriumName_totalNumberOfSeats);
                jsonObject.put("movieAuditoriumName_dateAndTimeCreated", movieAuditoriumName_dateAndTimeCreated); 
                
                if(!userLoggedIn_type.equals("IT Administrator")){
                   action = "N/A";
                   jsonObject.put("action", action); 
                }else{
                   action = "<a href='" + request.getContextPath() + "/ShowDetailsOfSelectedMovieAuditorium?movieAuditoriumName=" + movieAuditoriumName + "'><button type='button' class='btn btn-danger'>Details</button></a>";
                   jsonObject.put("action", action);
                }
                
                jsonArray.put(jsonObject);
                
            }
            
            JSONMovieAuditorium.put("MOVIE_AUDITORIUM", jsonArray);
            
            response.getWriter().write(JSONMovieAuditorium.toString());
        } catch (SQLException ex) {
            Logger.getLogger(ShowAllMovieAuditorium.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            if(pstmt != null){
                try {
                    pstmt.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ShowAllMovieAuditorium.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            if(con != null){
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ShowAllMovieAuditorium.class.getName()).log(Level.SEVERE, null, ex);
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
