package com.smcinema.admin;

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

public class ShowDetailsOfSelectedMovieBranch extends HttpServlet {
    Connection con;
    PreparedStatement pstmt;
    ResultSet rs;
    
    PrintWriter print;
    String branchName;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        con = DatabaseManager.getConnection();
        
        branchName = request.getParameter("branchName");
        
        fetchSelectedMovieBranchInformation(request,response);
    }
    
    public void fetchSelectedMovieBranchInformation(HttpServletRequest request,HttpServletResponse response) throws IOException{
        try {
            pstmt = con.prepareStatement("SELECT * FROM TRISTANROSALES.TBLMOVIEBRANCHES "
                    + "WHERE MOVIE_BRANCHNAME=?");
            pstmt.setString(1, branchName);
            
            rs = pstmt.executeQuery();
            
            while(rs.next()){
                request.getSession().setAttribute("MOVIE_BRANCHID", rs.getString("MOVIE_ID"));  
                request.getSession().setAttribute("MOVIE_BRANCHNAME", rs.getString("MOVIE_BRANCHNAME"));  
                response.sendRedirect(request.getContextPath() + "/pages/ADMIN/adminHomePage.jsp?branchName=" + branchName + "#showMovieBranchDetails"); 
            }
            
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(ShowDetailsOfSelectedMovieBranch.class.getName()).log(Level.SEVERE, null, ex);
            print.println(ex.getMessage() + "<br>");
            print.flush();
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
