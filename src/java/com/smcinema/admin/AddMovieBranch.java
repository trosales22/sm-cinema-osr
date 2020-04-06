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

public class AddMovieBranch extends HttpServlet {
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
            
            movie.setMovie_branchName(request.getParameter("txtMovieBranchName"));
            
            //create a java timestamp object that represents the current time (i.e., a "current timestamp")
            SimpleDateFormat sdf_dateAndTimeCreated = new SimpleDateFormat("MMMMM dd, yyyy | hh:mm a");
            Calendar dateAndTimeCreated = Calendar.getInstance();
            
            movie.setDateAndTimeCreated(sdf_dateAndTimeCreated.format(dateAndTimeCreated.getTime()));
            
            stmt = con.createStatement();
            
            rs = dbmd.getTables(null, null, "TBLMOVIEBRANCHES", null);
            if(rs.next()){
                //table exists
                detectIfUserIsAnITSupervisor(response);
            }else{
                //table does not exist
                addTableForMovieBranches();
                detectIfUserIsAnITSupervisor(response);
            }
        } catch (SQLException ex) {
            Logger.getLogger(AddMovieBranch.class.getName()).log(Level.SEVERE, null, ex);
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
                    addMovieBranch(response);
                }else{
                    status.addProperty("isAddingMovieBranchSuccess", "accessDenied");
                    response.getWriter().write(status.toString());
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(AddMovieBranch.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void addMovieBranch(HttpServletResponse response) throws IOException{
        try {   
            rs = stmt.executeQuery("SELECT MOVIE_BRANCHNAME FROM TRISTANROSALES.TBLMOVIEBRANCHES "
                    + "WHERE MOVIE_BRANCHNAME LIKE '%" + movie.getMovie_branchName() + "%'");
            
            if(rs.next()){
                status.addProperty("isAddingMovieBranchSuccess", "branchNameExists");
                response.getWriter().write(status.toString());
            }else{
                String addMovieBranchQuery;
            
                addMovieBranchQuery = "INSERT INTO TRISTANROSALES.TBLMOVIEBRANCHES"
                    + "(MOVIE_BRANCHNAME,MOVIE_DATEANDTIMECREATED) VALUES (?,?)";
            
                pstmt = con.prepareStatement(addMovieBranchQuery);
            
                pstmt.setString(1, movie.getMovie_branchName());
                pstmt.setString(2, movie.getDateAndTimeCreated());

                pstmt.executeUpdate(); 
                
                status.addProperty("isAddingMovieBranchSuccess", true);
                response.getWriter().write(status.toString());
            }        
        } catch (SQLException ex) {
            Logger.getLogger(AddMovieBranch.class.getName()).log(Level.SEVERE, null, ex);
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
    
    public void addTableForMovieBranches(){
        try {
            String addTableForMovieBranchesQuery;
            addTableForMovieBranchesQuery = "CREATE TABLE TRISTANROSALES.TBLMOVIEBRANCHES ("
                    + "MOVIE_ID INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
                    + "MOVIE_BRANCHNAME VARCHAR(32672) NOT NULL,"
                    + "MOVIE_DATEANDTIMECREATED VARCHAR(32672) NOT NULL)";
            
            pstmt = con.prepareStatement(addTableForMovieBranchesQuery);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(AddMovieBranch.class.getName()).log(Level.SEVERE, null, ex);
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
