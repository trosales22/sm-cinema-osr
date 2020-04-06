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

public class ShowDetailsOfSelectedAdministrator extends HttpServlet {
    Connection con;
    PreparedStatement pstmt;
    ResultSet rs;
    
    PrintWriter print;
    String emailAddress;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        con = DatabaseManager.getConnection();
        
        emailAddress = request.getParameter("emailAddress");
        
        fetchSelectedAdministratorInformation(request,response);
    }
    
    public void fetchSelectedAdministratorInformation(HttpServletRequest request,HttpServletResponse response) throws IOException{
        try {
            pstmt = con.prepareStatement("SELECT * FROM TRISTANROSALES.TBLUSERSANDADMINS "
                    + "WHERE USER_EMAILADDRESS=?");
            pstmt.setString(1, emailAddress);
            
            rs = pstmt.executeQuery();
            
            while(rs.next()){
                request.getSession().setAttribute("USER_FULLNAME", rs.getString("USER_FIRSTNAME") + " " + rs.getString("USER_LASTNAME"));
                request.getSession().setAttribute("USER_EMAILADDRESS", rs.getString("USER_EMAILADDRESS"));
                request.getSession().setAttribute("USER_TYPE", rs.getString("USER_TYPE"));
                request.getSession().setAttribute("USER_STATUS", rs.getString("USER_STATUS"));
                
                response.sendRedirect(request.getContextPath() + "/pages/ADMIN/adminHomePage.jsp?emailAddress=" + emailAddress + "#showAdministratorDetails"); 
            }
            
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(ShowDetailsOfSelectedAdministrator.class.getName()).log(Level.SEVERE, null, ex);
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
