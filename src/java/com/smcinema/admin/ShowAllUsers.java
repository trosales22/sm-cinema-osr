package com.smcinema.admin;

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

public class ShowAllUsers extends HttpServlet {
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs;
    
    String fullName,emailAddress,accountType,dateAndTimeCreated;
    JSONObject JSONUsers = new JSONObject();
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            
            con = DatabaseManager.getConnection();
            
            pstmt = con.prepareStatement("SELECT * FROM TRISTANROSALES.TBLUSERSANDADMINS WHERE USER_TYPE = 'Customer'");
            
            rs = pstmt.executeQuery();
            
            JSONArray jsonArray = new JSONArray();
            
            while(rs.next()){
                JSONObject jsonObject = new JSONObject();
                
                fullName = rs.getString("USER_FIRSTNAME") + " " + rs.getString("USER_LASTNAME");
                emailAddress = rs.getString("USER_EMAILADDRESS");
                accountType = rs.getString("USER_TYPE");        
                dateAndTimeCreated = rs.getString("USER_DATEANDTIMECREATED");
                
                String birthMonth,birthDay,birthYear,fullBirthDay;
                birthMonth = rs.getString("USER_BIRTHMONTH");
                birthDay = rs.getString("USER_BIRTHDAY");
                birthYear = rs.getString("USER_BIRTHYEAR");
                
                fullBirthDay = birthMonth + " " + birthDay + ", " + birthYear;
                
                jsonObject.put("fullName", fullName);
                jsonObject.put("emailAddress", emailAddress);
                jsonObject.put("accountType", accountType);
                jsonObject.put("dateAndTimeCreated", dateAndTimeCreated); 
                jsonObject.put("fullBirthDay", fullBirthDay); 
                
                jsonArray.put(jsonObject);
            }
            
            JSONUsers.put("USERS", jsonArray);
            response.getWriter().write(JSONUsers.toString());
        } catch (SQLException ex) {
            Logger.getLogger(ShowAllUsers.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            if(pstmt != null){
                try {
                    pstmt.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ShowAllUsers.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            if(con != null){
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ShowAllUsers.class.getName()).log(Level.SEVERE, null, ex);
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
