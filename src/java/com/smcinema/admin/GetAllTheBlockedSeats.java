package com.smcinema.admin;

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

public class GetAllTheBlockedSeats extends HttpServlet {
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs;
    
    String JSON_blockedSeats = null;
    String ps_auditoriumName,ps_blockedSeats;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            response.setContentType("application/json");
            
            ps_auditoriumName = request.getParameter("ps_auditoriumName");
            con = DatabaseManager.getConnection();
            
            pstmt = con.prepareStatement("SELECT * FROM TRISTANROSALES.TBLBLOCKEDSEATS "
                    + "WHERE PS_AUDITORIUMNAME=?");
            pstmt.setString(1, ps_auditoriumName);
            
            rs = pstmt.executeQuery();
            
            //List<String> listOfBlockedSeats = new ArrayList<>();
            
            List rowValues = new ArrayList();
            
            while(rs.next()){   
                ps_blockedSeats = rs.getString("PS_BLOCKEDSEATS");
                rowValues.add(ps_blockedSeats);
                
                /*
                listOfBlockedSeats = Arrays.asList(ps_blockedSeats.split("\\s*,\\s*"));
                JSON_blockedSeats = new Gson().toJson(listOfBlockedSeats);  
                response.getWriter().write(JSON_blockedSeats);
                */
            }
            
            response.getWriter().write(rowValues.toString());
        } catch (SQLException ex) {
            Logger.getLogger(GetAllTheBlockedSeats.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            if(pstmt != null){
                try {
                    pstmt.close();
                } catch (SQLException ex) {
                    Logger.getLogger(GetAllTheBlockedSeats.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            if(con != null){
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(GetAllTheBlockedSeats.class.getName()).log(Level.SEVERE, null, ex);
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
