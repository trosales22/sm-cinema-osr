package com.smcinema.user;

import com.smcinema.classes.DatabaseManager;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

public class GetAllTheComingSoonMovies extends HttpServlet {
    Connection con;
    PreparedStatement pstmt;
    ResultSet rs;
    JSONObject JSONComingSoon_movies = new JSONObject();
    
    String movieID,movieName,movieYear,movieReleaseDate;
    boolean isDateTodayEqualToMovieReleaseDate,isDateTodayBeforeToMovieReleaseDate,
            isDateTodayAfterToMovieReleaseDate;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        con = DatabaseManager.getConnection();
        detectIfMovieReleaseDateIsBeforeDateToday(response);
    }
    
    public void detectIfMovieReleaseDateIsBeforeDateToday(HttpServletResponse response) throws ParseException, IOException{
        try {
            pstmt = con.prepareStatement("SELECT * FROM TRISTANROSALES.TBLMOVIECINEMA "
                    + "WHERE MOVIE_RELEASEDATE <= ?");
            
            SimpleDateFormat sdf = new SimpleDateFormat("E, MMMMM dd, yyyy");
            
            Calendar dateToday_calendar = Calendar.getInstance();
            String dateToday_string = sdf.format(dateToday_calendar.getTime());
            
            pstmt.setString(1, dateToday_string);
            
            rs = pstmt.executeQuery();
            
            JSONArray jsonArray = new JSONArray();
            
            while(rs.next()){
                JSONObject jsonObject = new JSONObject();
                
                movieID = rs.getString("MOVIE_ID");
                movieName = rs.getString("MOVIE_NAME");
                movieYear = rs.getString("MOVIE_YEAR");
                movieReleaseDate = rs.getString("MOVIE_RELEASEDATE");
                
                jsonObject.put("movieID", movieID);
                jsonObject.put("movieName", movieName);
                jsonObject.put("movieYear", movieYear);  
                
                jsonArray.put(jsonObject);
            }

            JSONComingSoon_movies.put("COMINGSOON_MOVIES", jsonArray);
            response.getWriter().write(JSONComingSoon_movies.toString());
        } catch (SQLException ex) {
            Logger.getLogger(GetAllTheComingSoonMovies.class.getName()).log(Level.SEVERE, null, ex);
            response.getWriter().write(ex.getMessage());
        }
    }
    
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (ParseException ex) {
            Logger.getLogger(GetAllTheComingSoonMovies.class.getName()).log(Level.SEVERE, null, ex);
            response.getWriter().write(ex.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (ParseException ex) {
            Logger.getLogger(GetAllTheComingSoonMovies.class.getName()).log(Level.SEVERE, null, ex);
            response.getWriter().write(ex.getMessage());
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
