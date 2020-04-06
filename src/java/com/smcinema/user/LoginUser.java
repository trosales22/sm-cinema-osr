package com.smcinema.user;

import com.google.gson.JsonObject;
import com.smcinema.classes.DatabaseManager;
import com.smcinema.classes.UserBean;
import com.smcinema.classes.ValidateUser;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginUser extends HttpServlet {
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs;
    PrintWriter print;
    
    UserBean user = new UserBean();
    HttpSession session;
    Cookie loginCookie;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        JsonObject status = new JsonObject();
        
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");

        user.setEmailAddress(request.getParameter("txtEmailAddress"));
        user.setPassword(request.getParameter("txtPassword"));
        
        if(ValidateUser.checkUserOrAdmin(user.getEmailAddress(), user.getPassword())){
            try {
                loginCookie = new Cookie("user_emailAddress",user.getEmailAddress());
                
                //setting cookie to expiry in 8 hours
                loginCookie.setMaxAge(28800);
                response.addCookie(loginCookie);
                
                con = DatabaseManager.getConnection();
                
                pstmt = con.prepareStatement("SELECT * FROM TRISTANROSALES.TBLUSERSANDADMINS "
                        + "WHERE USER_EMAILADDRESS=? AND USER_STATUS='Verified'");
                pstmt.setString(1, user.getEmailAddress());
                
                rs = pstmt.executeQuery();
                 
                while(rs.next()){
                    user.setFirstname(rs.getString("USER_FIRSTNAME"));
                    user.setLastname(rs.getString("USER_LASTNAME"));
                    user.setAccountType(rs.getString("USER_TYPE"));
                    user.setEmailAddress(rs.getString("USER_EMAILADDRESS"));
                    user.setNameOfYourFavoritePet(rs.getString("USER_NAMEOFFAVORITEPET"));
                    
                    request.getSession().setAttribute("COOKIE_USER_FIRSTNAME", user.getFirstname());
                    request.getSession().setAttribute("COOKIE_USER_LASTNAME", user.getLastname());
                    request.getSession().setAttribute("COOKIE_USER_TYPE", user.getAccountType());
                    request.getSession().setAttribute("COOKIE_USER_EMAILADDRESS", user.getEmailAddress());
                    request.getSession().setAttribute("COOKIE_USER_NAMEOFFAVORITEPET", user.getNameOfYourFavoritePet());
                    
                    if(user.getAccountType().equals("Customer")){
                        status.addProperty("isLoginSuccess", "userSide");
                        response.getWriter().write(status.toString());
                    }else{
                        status.addProperty("isLoginSuccess", "adminSide");
                        response.getWriter().write(status.toString());
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(LoginUser.class.getName()).log(Level.SEVERE, null, ex);
                status.addProperty("isLoginSuccess", false);
                response.getWriter().write(status.toString());
            }
        }else{
            status.addProperty("isLoginSuccess", false);
            response.getWriter().write(status.toString());
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
