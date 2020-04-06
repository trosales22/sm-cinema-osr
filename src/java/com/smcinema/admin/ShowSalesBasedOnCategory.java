package com.smcinema.admin;

import com.google.gson.JsonObject;
import com.smcinema.classes.DatabaseManager;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

public class ShowSalesBasedOnCategory extends HttpServlet {
    Connection con;
    PreparedStatement pstmt;
    ResultSet rs;
    JsonObject status = new JsonObject();
    JSONObject JSONSales = new JSONObject();
    
    SimpleDateFormat wantedFormat = new SimpleDateFormat("LLLL");
    SimpleDateFormat currentFormat = new SimpleDateFormat("MM");
                
    String user_emailAddress,userType,category,reservation_dateAndTimeReserved,movieName,
            year,month,day,orders;
    double subtotal;
    int totalAmount;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        
        con = DatabaseManager.getConnection();
        
        user_emailAddress = (String) request.getSession().getAttribute("COOKIE_USER_EMAILADDRESS");
        category = request.getParameter("txtCategory");
        
        detectIfUserIsAnOperationsSupervisor(request,response);
    }
    
    public void detectIfUserIsAnOperationsSupervisor(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException{
        try {
            pstmt = con.prepareStatement("SELECT * FROM TRISTANROSALES.TBLUSERSANDADMINS "
                    + "WHERE USER_EMAILADDRESS=?");
            pstmt.setString(1, user_emailAddress);
            
            rs = pstmt.executeQuery();
            
            if(rs.next()){
                userType = rs.getString("USER_TYPE");
                
                if(!userType.equals("Operations Supervisor")){
                    status.addProperty("isShowSalesOfSelectedMovieSuccess", "accessDenied");
                    response.getWriter().write(status.toString());
                }else{
                    if(category.equals("Daily")){
                        showDailySales(response);
                    }else if(category.equals("Monthly")){
                        showMonthlySales(response);
                    }else if(category.equals("Yearly")){
                        showYearlySales(response);
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ShowSalesBasedOnCategory.class.getName()).log(Level.SEVERE, null, ex);
            response.getWriter().write(ex.getMessage());
        }
    }
    
    public void showDailySales(HttpServletResponse response) throws IOException, ParseException{
        try {
            pstmt = con.prepareStatement("SELECT DAY(RESERVATION_DATEANDTIMERESERVED) AS SalesDay,"
                    + "MONTH(RESERVATION_DATEANDTIMERESERVED) AS SalesMonth,"
                    + "YEAR(RESERVATION_DATEANDTIMERESERVED) AS SalesYear,"
                    + "SUM((DOUBLE(RESERVATION_MOVIEPRICE) * INT(RESERVATION_MOVIESEATQUANTITY)) "
                    + "+ (20 * INT(RESERVATION_MOVIESEATQUANTITY))) AS TotalSales,"
                    + "COUNT(*) AS Orders FROM TRISTANROSALES.TBLRESERVATIONSREPORT "
                    + "WHERE RESERVATION_STATUS='Paid' "
                    + "GROUP BY DAY(RESERVATION_DATEANDTIMERESERVED), MONTH(RESERVATION_DATEANDTIMERESERVED), YEAR(RESERVATION_DATEANDTIMERESERVED)");
            
            rs = pstmt.executeQuery();
            
            JSONArray jsonArray = new JSONArray();
            
            while(rs.next()){
                JSONObject jsonObject = new JSONObject();
                
                day = rs.getString("SalesDay");
                month = rs.getString("SalesMonth");
                year = rs.getString("SalesYear");
                subtotal = rs.getDouble("TotalSales");
                orders = rs.getString("Orders");
                
                jsonObject.put("day", day);
                jsonObject.put("month", wantedFormat.format(currentFormat.parse(month)));
                jsonObject.put("year", year);
                jsonObject.put("subtotal", String.format("%.2f", subtotal));
                jsonObject.put("orders", orders);
               
                jsonArray.put(jsonObject);
            }
            
            JSONSales.put("SALES", jsonArray);
            response.getWriter().write(JSONSales.toString());  
        } catch (SQLException ex) {
            Logger.getLogger(ShowSalesBasedOnCategory.class.getName()).log(Level.SEVERE, null, ex);
            response.getWriter().write(ex.getMessage());
        }
         
         
    }
    
    public void showMonthlySales(HttpServletResponse response) throws IOException, ParseException{
        try {
            pstmt = con.prepareStatement("SELECT YEAR(RESERVATION_DATEANDTIMERESERVED) AS SalesYear,"
                    + "MONTH(RESERVATION_DATEANDTIMERESERVED) AS SalesMonth,"
                    + "SUM((DOUBLE(RESERVATION_MOVIEPRICE) * INT(RESERVATION_MOVIESEATQUANTITY)) "
                    + "+ (20 * INT(RESERVATION_MOVIESEATQUANTITY))) AS TotalSales,"
                    + "COUNT(*) AS Orders FROM TRISTANROSALES.TBLRESERVATIONSREPORT "
                    + "WHERE RESERVATION_STATUS='Paid' "
                    + "GROUP BY YEAR(RESERVATION_DATEANDTIMERESERVED), MONTH(RESERVATION_DATEANDTIMERESERVED)");
            
            rs = pstmt.executeQuery();
            
            JSONArray jsonArray = new JSONArray();
            
            while(rs.next()){
                JSONObject jsonObject = new JSONObject();
                
                year = rs.getString("SalesYear");
                month = rs.getString("SalesMonth");
                subtotal = rs.getDouble("TotalSales");
                orders = rs.getString("Orders");
                
                jsonObject.put("year", year);
                jsonObject.put("month", wantedFormat.format(currentFormat.parse(month)));
                jsonObject.put("subtotal", String.format("%.2f", subtotal));
                jsonObject.put("orders", orders);
   
                jsonArray.put(jsonObject);
            }
            
            JSONSales.put("SALES", jsonArray);
            response.getWriter().write(JSONSales.toString());  
        } catch (SQLException ex) {
            Logger.getLogger(ShowSalesBasedOnCategory.class.getName()).log(Level.SEVERE, null, ex);
            response.getWriter().write(ex.getMessage());
        }
    }    
    
    public void showYearlySales(HttpServletResponse response) throws IOException{
        try {
            pstmt = con.prepareStatement("SELECT YEAR(RESERVATION_DATEANDTIMERESERVED) AS SalesYear,"
                    + "SUM((DOUBLE(RESERVATION_MOVIEPRICE) * INT(RESERVATION_MOVIESEATQUANTITY)) "
                    + "+ (20 * INT(RESERVATION_MOVIESEATQUANTITY))) AS TotalSales,"
                    + "COUNT(*) AS Orders FROM TRISTANROSALES.TBLRESERVATIONSREPORT "
                    + "WHERE RESERVATION_STATUS='Paid' "
                    + "GROUP BY YEAR(RESERVATION_DATEANDTIMERESERVED)");
            
            rs = pstmt.executeQuery();
            
            JSONArray jsonArray = new JSONArray();
            
            while(rs.next()){
                JSONObject jsonObject = new JSONObject();
                
                year = rs.getString("SalesYear");
                subtotal = rs.getDouble("TotalSales");
                orders = rs.getString("Orders");
                
                jsonObject.put("year", year);
                jsonObject.put("subtotal", String.format("%.2f", subtotal));
                jsonObject.put("orders", orders);
   
                jsonArray.put(jsonObject);
            }
            
            JSONSales.put("SALES", jsonArray);
            response.getWriter().write(JSONSales.toString());  
        } catch (SQLException ex) {
            Logger.getLogger(ShowSalesBasedOnCategory.class.getName()).log(Level.SEVERE, null, ex);
            response.getWriter().write(ex.getMessage());
        }
    }    
    
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (ParseException ex) {
            Logger.getLogger(ShowSalesBasedOnCategory.class.getName()).log(Level.SEVERE, null, ex);
            response.getWriter().write(ex.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (ParseException ex) {
            Logger.getLogger(ShowSalesBasedOnCategory.class.getName()).log(Level.SEVERE, null, ex);
            response.getWriter().write(ex.getMessage());
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
