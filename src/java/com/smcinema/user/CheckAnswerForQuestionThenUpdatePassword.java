package com.smcinema.user;

import com.google.gson.JsonObject;
import com.smcinema.classes.DatabaseManager;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CheckAnswerForQuestionThenUpdatePassword extends HttpServlet {
    Connection con;
    PreparedStatement pstmt;
    ResultSet rs;
    Statement stmt;
    PrintWriter print;
    JsonObject status = new JsonObject();
    
    String user_emailAddress,user_nameOfFavoritePet,user_newPassword;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //response.setContentType("application/json");
        //response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=UTF-8");
        
        con = DatabaseManager.getConnection();

        user_emailAddress = request.getParameter("user_emailAddress");
        user_nameOfFavoritePet = request.getParameter("txtNameOfFavoritePet");
        user_newPassword = request.getParameter("txtNewPassword");
        
        detectIfCredentialsIsCorrect(response);
    }

    public void detectIfCredentialsIsCorrect(HttpServletResponse response) throws IOException{
        try {
            pstmt = con.prepareStatement("SELECT * FROM TRISTANROSALES.TBLUSERSANDADMINS "
                    + "WHERE USER_EMAILADDRESS=? AND USER_NAMEOFFAVORITEPET=?");
            pstmt.setString(1, user_emailAddress);
            pstmt.setString(2, user_nameOfFavoritePet);
            
            rs = pstmt.executeQuery();
            
            if(rs.next()){
                updatePassword(response);
            }else{
                status.addProperty("isChangingPasswordSuccess", false);
                response.getWriter().write(status.toString());
            }
        } catch (SQLException ex) {
            Logger.getLogger(CheckAnswerForQuestionThenUpdatePassword.class.getName()).log(Level.SEVERE, null, ex);
            print.println(ex.getMessage() + "<br>");
            print.flush();
        }
    }
    
    public void updatePassword(HttpServletResponse response) throws IOException{
        try {
            pstmt = con.prepareStatement("UPDATE TRISTANROSALES.TBLUSERSANDADMINS SET USER_PASSWORD=? WHERE USER_EMAILADDRESS=?");
            pstmt.setString(1, user_newPassword);
            pstmt.setString(2, user_emailAddress);
            pstmt.executeUpdate();
            
            status.addProperty("isChangingPasswordSuccess", true);
            response.getWriter().write(status.toString());
        } catch (SQLException ex) {
            Logger.getLogger(CheckAnswerForQuestionThenUpdatePassword.class.getName()).log(Level.SEVERE, null, ex);
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
