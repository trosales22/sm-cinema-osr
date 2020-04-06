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

public class GetAllTheReservedSeats extends HttpServlet {
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs;
    
    List<String> listOfReservedSeats = new ArrayList<>();
    String JSON_reservedSeats = null;
    String reservation_auditoriumName,reservation_reservedSeats;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            response.setContentType("application/json");
            
            reservation_auditoriumName = request.getParameter("ps_auditoriumName");
            con = DatabaseManager.getConnection();
            
            pstmt = con.prepareStatement("SELECT * FROM TRISTANROSALES.TBLRESERVATIONSREPORT "
                    + "WHERE RESERVATION_MOVIEAUDITORIUMNAME=?");
            pstmt.setString(1, reservation_auditoriumName);
            
            rs = pstmt.executeQuery();
            
            List rowValues = new ArrayList();
            
            while(rs.next()){          
                reservation_reservedSeats = rs.getString("RESERVATION_OCCUPIEDSEATS");
                rowValues.add(reservation_reservedSeats);
                
                /*
                listOfReservedSeats = Arrays.asList(reservation_reservedSeats.split("\\s*,\\s*"));
                
                JSON_reservedSeats = new Gson().toJson(listOfReservedSeats);  
                response.getWriter().write(JSON_reservedSeats);
                */
            }

            response.getWriter().write(rowValues.toString());
        } catch (SQLException ex) {
            Logger.getLogger(GetAllTheReservedSeats.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            if(pstmt != null){
                try {
                    pstmt.close();
                } catch (SQLException ex) {
                    Logger.getLogger(GetAllTheReservedSeats.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            if(con != null){
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(GetAllTheReservedSeats.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    public static String[] join(String [] ... parms) {
        // calculate size of target array
        int size = 0;
        for (String[] array : parms) {
          size += array.length;
        }

        String[] result = new String[size];

        int j = 0;
        for (String[] array : parms) {
          for (String s : array) {
            result[j++] = s;
          }
        }
        return result;
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
