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

public class GetAllMovieBranches extends HttpServlet {
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs;
    
    List<String> listOfMovieBranches = new ArrayList<>();
    String JSON_movieBranches = null;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            con = DatabaseManager.getConnection();
            
            pstmt = con.prepareStatement("SELECT * FROM TRISTANROSALES.TBLMOVIEBRANCHES");
            
            rs = pstmt.executeQuery();
            
            listOfMovieBranches.clear();
            
            while(rs.next()){    
                listOfMovieBranches.add(rs.getString("MOVIE_BRANCHNAME"));
            }
            
            JSON_movieBranches = new Gson().toJson(listOfMovieBranches);  
            response.getWriter().write(JSON_movieBranches);
            response.setContentType("application/json");
        } catch (SQLException ex) {
            Logger.getLogger(GetAllMovieBranches.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            if(pstmt != null){
                try {
                    pstmt.close();
                } catch (SQLException ex) {
                    Logger.getLogger(GetAllMovieBranches.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            if(con != null){
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(GetAllMovieBranches.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
