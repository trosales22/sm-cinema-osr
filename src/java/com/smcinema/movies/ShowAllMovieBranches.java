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

public class ShowAllMovieBranches extends HttpServlet {
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs;
    
    String movieBranchName,movieBranchName_dateAndTimeCreated;
    JSONObject JSONMovieBranches = new JSONObject();
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setIntHeader("refresh", -1);
            
            con = DatabaseManager.getConnection();
            pstmt = con.prepareStatement("SELECT * FROM TRISTANROSALES.TBLMOVIEBRANCHES");
            
            rs = pstmt.executeQuery();
            
            JSONArray jsonArray = new JSONArray();
            
            while(rs.next()){
                JSONObject jsonObject = new JSONObject();
                
                movieBranchName = rs.getString("MOVIE_BRANCHNAME");
                movieBranchName_dateAndTimeCreated = rs.getString("MOVIE_DATEANDTIMECREATED");
           
                jsonObject.put("movieBranchName", movieBranchName);
                jsonObject.put("movieBranchName_dateAndTimeCreated", movieBranchName_dateAndTimeCreated); 
                
                jsonArray.put(jsonObject);
            }
            
            JSONMovieBranches.put("MOVIE_BRANCHES", jsonArray);
            response.getWriter().write(JSONMovieBranches.toString());
        } catch (SQLException ex) {
            Logger.getLogger(ShowAllMovieBranches.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            if(pstmt != null){
                try {
                    pstmt.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ShowAllMovieBranches.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            if(con != null){
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ShowAllMovieBranches.class.getName()).log(Level.SEVERE, null, ex);
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
