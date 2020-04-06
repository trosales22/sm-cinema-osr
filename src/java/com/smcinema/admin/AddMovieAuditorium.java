package com.smcinema.admin;

import com.google.gson.JsonObject;
import com.smcinema.classes.DatabaseManager;
import com.smcinema.classes.MovieBranchAndAuditoriumBean;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddMovieAuditorium extends HttpServlet {
    MovieBranchAndAuditoriumBean movie = new MovieBranchAndAuditoriumBean();
    PrintWriter print;
    int count;
    String user_emailAddress,userType;
    
    Connection con = null;
    PreparedStatement pstmt = null;
    Statement stmt;
    ResultSet rs;
    DatabaseMetaData dbmd;
    JsonObject status = new JsonObject();
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            
            con = DatabaseManager.getConnection();
            dbmd = con.getMetaData();
            
            user_emailAddress = (String) request.getSession().getAttribute("COOKIE_USER_EMAILADDRESS");
            
            movie.setMovie_auditoriumName(request.getParameter("txtMovieAuditoriumName"));
            movie.setMovie_auditorium_totalNumberOfSeats(request.getParameter("txtMovieTotalNumberOfSeats"));
            
            //create a java timestamp object that represents the current time (i.e., a "current timestamp")
            SimpleDateFormat sdf_dateAndTimeCreated = new SimpleDateFormat("MMMMM dd, yyyy | hh:mm a");
            Calendar dateAndTimeCreated = Calendar.getInstance();
            
            movie.setDateAndTimeCreated(sdf_dateAndTimeCreated.format(dateAndTimeCreated.getTime()));
            
            stmt = con.createStatement();
            
            rs = dbmd.getTables(null, null, "TBLMOVIEAUDITORIUM", null);
            if(rs.next()){
                //table exists
                detectIfUserIsAnITSupervisor(response);
            }else{
                //table does not exist
                addTableForMovieAuditorium();
                detectIfUserIsAnITSupervisor(response);
            }
        } catch (SQLException ex) {
            Logger.getLogger(AddMovieAuditorium.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void detectIfUserIsAnITSupervisor(HttpServletResponse response) throws IOException{
        try {
            pstmt = con.prepareStatement("SELECT * FROM TRISTANROSALES.TBLUSERSANDADMINS "
                    + "WHERE USER_EMAILADDRESS=?");
            pstmt.setString(1, user_emailAddress);
            
            rs = pstmt.executeQuery();
            
            if(rs.next()){
                userType = rs.getString("USER_TYPE");
                
                if(userType.equals("IT Supervisor")){
                    addMovieAuditorium(response);
                }else{
                    status.addProperty("isAddingMovieAuditoriumSuccess", "accessDenied");
                    response.getWriter().write(status.toString());
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(AddMovieAuditorium.class.getName()).log(Level.SEVERE, null, ex);
            response.getWriter().write(ex.getMessage());
        }
    }
    
    public void addMovieAuditorium(HttpServletResponse response) throws IOException{
        try {   
            rs = stmt.executeQuery("SELECT MOVIE_AUDITORIUMNAME FROM TRISTANROSALES.TBLMOVIEAUDITORIUM "
                    + "WHERE MOVIE_AUDITORIUMNAME LIKE '%" + movie.getMovie_auditoriumName() + "%'");
            
            if(rs.next()){
                status.addProperty("isAddingMovieAuditoriumSuccess", "auditoriumExists");
                response.getWriter().write(status.toString());
            }else{
                String addMovieAuditoriumQuery;
            
                addMovieAuditoriumQuery = "INSERT INTO TRISTANROSALES.TBLMOVIEAUDITORIUM"
                    + "(MOVIE_AUDITORIUMNAME,MOVIE_TOTALNUMBEROFSEATS,MOVIE_DATEANDTIMECREATED) VALUES (?,?,?)";
            
                pstmt = con.prepareStatement(addMovieAuditoriumQuery);
            
                pstmt.setString(1, movie.getMovie_auditoriumName());
                pstmt.setString(2, movie.getMovie_auditorium_totalNumberOfSeats());
                pstmt.setString(3, movie.getDateAndTimeCreated());

                pstmt.executeUpdate(); 
                
                status.addProperty("isAddingMovieAuditoriumSuccess", true);
                response.getWriter().write(status.toString());
            }        
        } catch (SQLException ex) {
            Logger.getLogger(AddMovieBranch.class.getName()).log(Level.SEVERE, null, ex);
            response.getWriter().write(ex.getMessage());
        }finally{
            if(pstmt != null){
                try {
                    pstmt.close();
                } catch (SQLException ex) {
                    Logger.getLogger(AddMovie.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            if(con != null){
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(AddMovie.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    public void addTableForMovieAuditorium(){
        try {
            String addTableForMovieAuditoriumQuery;
            addTableForMovieAuditoriumQuery = "CREATE TABLE TRISTANROSALES.TBLMOVIEAUDITORIUM ("
                    + "MOVIE_ID INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
                    + "MOVIE_AUDITORIUMNAME VARCHAR(32672) NOT NULL,"
                    + "MOVIE_TOTALNUMBEROFSEATS VARCHAR(32672) NOT NULL,"
                    + "MOVIE_DATEANDTIMECREATED VARCHAR(32672) NOT NULL)";
            
            pstmt = con.prepareStatement(addTableForMovieAuditoriumQuery);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(AddMovieAuditorium.class.getName()).log(Level.SEVERE, null, ex);
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
