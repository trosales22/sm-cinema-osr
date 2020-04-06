package com.smcinema.admin;

import com.google.gson.JsonObject;
import com.smcinema.classes.DatabaseManager;
import com.smcinema.classes.MovieBean;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@MultipartConfig(maxFileSize = 16177215) // upload file's size up to 16MB
public class AddMovie extends HttpServlet {
    PrintWriter print;
    InputStream inputStream = null;
    Part filePart;
    MovieBean movie = new MovieBean();
    String movieDuration_hours,movieDuration_minutes,movieDuration,user_emailAddress,userType;
    int count;
    
    Connection con = null;
    PreparedStatement pstmt = null;
    Statement stmt;
    ResultSet rs;
    DatabaseMetaData dbmd;
    JsonObject status = new JsonObject();
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException{
        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            
            con = DatabaseManager.getConnection();
            dbmd = con.getMetaData();
            
            user_emailAddress = (String) request.getSession().getAttribute("COOKIE_USER_EMAILADDRESS");
            
            filePart = request.getPart("moviePoster");
            
            if(filePart != null){
                inputStream = filePart.getInputStream();
            }
             
            movie.setMovieReference(request.getParameter("txtMovieReference"));
            movie.setMovieName(request.getParameter("txtMovieName"));
            movie.setMovieYear(request.getParameter("txtMovieYear"));
            movie.setMovieDirector(request.getParameter("txtMovieDirector"));
            movie.setReleasedBy(request.getParameter("txtMovieReleasedBy"));
            movie.setMovieTrailerLink(request.getParameter("txtMovieTrailerLink"));
            
            movieDuration_minutes = request.getParameter("txtMovieDuration_minutes");
                
            movieDuration_hours = request.getParameter("txtMovieDuration_hours");
                
            movieDuration_minutes = request.getParameter("txtMovieDuration_minutes");
                
            movieDuration = movieDuration_hours + " hr (s) and " + movieDuration_minutes + " min (s)";
                
            movie.setMovieDuration(movieDuration);
            movie.setMoviePrice(request.getParameter("txtMoviePrice"));
            movie.setTypeOfSeating(request.getParameter("txtMovieTypeOfSeating"));
            movie.setMovieCast(request.getParameter("txtMovieCast"));
            movie.setMovieSynopsis(request.getParameter("txtMovieSynopsis"));
            movie.setMTRCB_rating(request.getParameter("txtMTRCB_Rating"));
            movie.setMovieGenre(request.getParameter("txtMovieGenre"));  
            movie.setMovieSchedule(request.getParameter("txtMovieSchedules"));
            movie.setMovieBranches(request.getParameter("txtMovieBranches"));

            String txtMovieReleaseDate = request.getParameter("txtMovieReleaseDate");
            String txtMovieCloseDate = request.getParameter("txtMovieCloseDate");
            
            DateFormat wantedFormat = new SimpleDateFormat("E, MMMMM dd, yyyy");
            DateFormat currentFormat = new SimpleDateFormat("yyyy-MM-dd");
            
            wantedFormat.format(currentFormat.parse(txtMovieReleaseDate));
            wantedFormat.format(currentFormat.parse(txtMovieCloseDate));
            
            movie.setMovieReleaseDate(wantedFormat.format(currentFormat.parse(txtMovieReleaseDate)));
            movie.setMovieCloseDate(wantedFormat.format(currentFormat.parse(txtMovieCloseDate)));
                
            movie.setMovieStatus("Activated");
            
            //create a java timestamp object that represents the current time (i.e., a "current timestamp")
            SimpleDateFormat sdf_dateAndTimeCreated = new SimpleDateFormat("MMMMM dd, yyyy | hh:mm a");
            Calendar dateAndTimeCreated = Calendar.getInstance();
            
            movie.setDateAndTimeCreated(sdf_dateAndTimeCreated.format(dateAndTimeCreated.getTime()));
            
            movie.setMovieAuditorium(request.getParameter("txtMovieAuditorium"));
            rs = dbmd.getTables(null, null, "TBLMOVIECINEMA", null);
            if(rs.next()){
                //table exists
                detectIfUserIsAnITAdministrator(response);
            }else{
                //table does not exist
                addTableForMovieCinema();
                detectIfUserIsAnITAdministrator(response);
            }
        } catch (SQLException ex) {
            Logger.getLogger(AddMovie.class.getName()).log(Level.SEVERE, null, ex);
        }     
    }
    
    public void detectIfUserIsAnITAdministrator(HttpServletResponse response) throws IOException{
        try {
            pstmt = con.prepareStatement("SELECT * FROM TRISTANROSALES.TBLUSERSANDADMINS "
                    + "WHERE USER_EMAILADDRESS=?");
            pstmt.setString(1, user_emailAddress);
            
            rs = pstmt.executeQuery();
            
            if(rs.next()){
                userType = rs.getString("USER_TYPE");
                
                if(userType.equals("IT Administrator")){
                    addMovie(response);
                }else{
                    status.addProperty("isAddingMovieSuccess", "accessDenied");
                    response.getWriter().write(status.toString());
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(AddMovie.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void addMovie(HttpServletResponse response) throws IOException{
        try{
            String addMovieQuery;
            
            addMovieQuery = "INSERT INTO TRISTANROSALES.TBLMOVIECINEMA"
                + "(movie_poster,movie_reference,movie_name,movie_year,movie_director,movie_releasedBy,"
                + "movie_trailerLink,movie_duration,movie_price,movie_typeOfSeating,movie_cast,"
                + "movie_synopsis,MTRCB_rating,movie_genre,movie_auditorium,movie_schedule,movie_branches,"
                + "movie_releaseDate,movie_closeDate,movie_status,movie_dateAndTimeCreated) "
                    + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                pstmt = con.prepareStatement(addMovieQuery);
                
                if(inputStream != null){
                    // fetches input stream of the upload file for the blob column
                    pstmt.setBlob(1, inputStream);
                }
                
                pstmt.setString(2, movie.getMovieReference());
                
                pstmt.setString(3, movie.getMovieName());
                pstmt.setString(4, movie.getMovieYear());
                pstmt.setString(5, movie.getMovieDirector());
                pstmt.setString(6, movie.getReleasedBy());
                pstmt.setString(7, movie.getMovieTrailerLink());
                pstmt.setString(8, movie.getMovieDuration());
                pstmt.setString(9, movie.getMoviePrice());
                pstmt.setString(10, movie.getTypeOfSeating());
                pstmt.setString(11, movie.getMovieCast());
                pstmt.setString(12, movie.getMovieSynopsis());
                pstmt.setString(13, movie.getMTRCB_rating());
                pstmt.setString(14, movie.getMovieGenre());
                pstmt.setString(15, movie.getMovieAuditorium());
                pstmt.setString(16, movie.getMovieSchedule());
                pstmt.setString(17, movie.getMovieBranches());
                pstmt.setString(18, movie.getMovieReleaseDate());
                pstmt.setString(19, movie.getMovieCloseDate());
                pstmt.setString(20, movie.getMovieStatus());
                pstmt.setString(21, movie.getDateAndTimeCreated());

                pstmt.executeUpdate();

                status.addProperty("isAddingMovieSuccess", true);
                response.getWriter().write(status.toString());
        }catch(SQLException ex){
            print.println(ex.getMessage());
            print.flush();
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
    
    public void addTableForMovieCinema(){
        try {
            String addTableForMovieCinemaQuery;
            addTableForMovieCinemaQuery = "CREATE TABLE TRISTANROSALES.tblMovieCinema ("
                    + "MOVIE_ID INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
                    + "MOVIE_POSTER BLOB(16777215) NOT NULL,"
                    + "MOVIE_REFERENCE VARCHAR(32672) NOT NULL,"
                    + "MOVIE_NAME VARCHAR(32672) NOT NULL,"
                    + "MOVIE_YEAR VARCHAR(32672) NOT NULL,"
                    + "MOVIE_DIRECTOR VARCHAR(32672) NOT NULL,"
                    + "MOVIE_RELEASEDBY VARCHAR(32672) NOT NULL,"
                    + "MOVIE_TRAILERLINK VARCHAR(32672) NOT NULL,"
                    + "MOVIE_DURATION VARCHAR(32672) NOT NULL,"
                    + "MOVIE_PRICE VARCHAR(32672) NOT NULL,"
                    + "MOVIE_TYPEOFSEATING VARCHAR(32672) NOT NULL,"
                    + "MOVIE_CAST VARCHAR(32672) NOT NULL,"
                    + "MOVIE_SYNOPSIS VARCHAR(32672) NOT NULL,"
                    + "MTRCB_RATING VARCHAR(32672) NOT NULL,"
                    + "MOVIE_GENRE VARCHAR(32672) NOT NULL,"
                    + "MOVIE_AUDITORIUM VARCHAR(32672) NOT NULL,"
                    + "MOVIE_SCHEDULE VARCHAR(32672) NOT NULL,"
                    + "MOVIE_BRANCHES VARCHAR(32672) NOT NULL,"
                    + "MOVIE_RELEASEDATE VARCHAR(32672) NOT NULL,"
                    + "MOVIE_CLOSEDATE VARCHAR(32672) NOT NULL,"
                    + "MOVIE_STATUS VARCHAR(32672) NOT NULL,"
                    + "MOVIE_DATEANDTIMECREATED VARCHAR(32672) NOT NULL)";
            
            pstmt = con.prepareStatement(addTableForMovieCinemaQuery);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(AddMovie.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            print = response.getWriter();
            processRequest(request, response);
        } catch (ParseException ex) {
            Logger.getLogger(AddMovie.class.getName()).log(Level.SEVERE, null, ex);
            print.println(ex.getMessage() + "<br>");
            print.flush();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            print = response.getWriter();
            processRequest(request, response);
        } catch (ParseException ex) {
            Logger.getLogger(AddMovie.class.getName()).log(Level.SEVERE, null, ex);
            print.println(ex.getMessage() + "<br>");
            print.flush();
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
