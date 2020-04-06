package com.smcinema.admin;

import com.smcinema.classes.DatabaseManager;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DetectIfUserForfeitsReservation extends HttpServlet {
    PrintWriter print;
    Connection con;
    PreparedStatement pstmt;
    Statement stmt;
    ResultSet rs;
    DatabaseMetaData dbmd;
    
    String query,user_emailAddress,user_reservationReferenceNumber,blockedUntil,
        dateAndTimeReserved,dateAndTimeHappened,paymentDue;
    int count;
    boolean isDateAndTimeNowEqualToPaymentDue,isDateAndTimeNowAfterThePaymentDue,isDateAndTimeNowBeforeThePaymentDue;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException {
        try {
            response.setContentType("text/html;charset=UTF-8");
            con = DatabaseManager.getConnection();
            dbmd = con.getMetaData();
            
            SimpleDateFormat sdf_dateAndTimeHappened = new SimpleDateFormat("MMMMM dd, yyyy | hh:mm a");
            Calendar calendar_dateAndTimeHappened = Calendar.getInstance();
            
            dateAndTimeHappened = sdf_dateAndTimeHappened.format(calendar_dateAndTimeHappened.getTime());
            
            rs = dbmd.getTables(null, null, "TBLRECORDSOFCANCELLATIONANDFORFEITS", null);
            if(rs.next()){
                //table exists
                retrieveAllWhoReservesTicket();
            }else{
                //table does not exist
                createTableForRecordsOfCancellationAndForteits();
                retrieveAllWhoReservesTicket();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DetectIfUserForfeitsReservation.class.getName()).log(Level.SEVERE, null, ex);
            print.println(ex.getMessage() + "<br>");
            print.flush();
        }
    }
    
    public void retrieveAllWhoReservesTicket() throws ParseException{
        try {
            pstmt = con.prepareStatement("SELECT * FROM TRISTANROSALES.TBLRESERVATIONSREPORT");
            rs = pstmt.executeQuery();
            
            while(rs.next()){
                user_emailAddress = rs.getString("RESERVATION_MOVIERESERVEDBY");
                user_reservationReferenceNumber = rs.getString("RESERVATION_REFERENCENUMBER");
                dateAndTimeReserved = rs.getString("RESERVATION_DATEANDTIMERESERVED");
                paymentDue = rs.getString("RESERVATION_PAYMENTDUE");
                
                SimpleDateFormat sdf = new SimpleDateFormat("E, MMMMM dd, yyyy | hh:mma");
                Calendar dateAndTimeToday_calendar = Calendar.getInstance();
                String dateAndTimeToday_string = sdf.format(dateAndTimeToday_calendar.getTime());
                Date newDateAndTimeToday_date = sdf.parse(dateAndTimeToday_string);
                Date newPaymentDue_date = sdf.parse(paymentDue);
                
                Calendar newDateAndTimeToday_calendar = Calendar.getInstance();
                newDateAndTimeToday_calendar.setTime(newDateAndTimeToday_date);
                
                Calendar newPaymentDue_calendar = Calendar.getInstance();
                newPaymentDue_calendar.setTime(newPaymentDue_date);
                
                isDateAndTimeNowEqualToPaymentDue = newDateAndTimeToday_calendar.equals(newPaymentDue_calendar);
                isDateAndTimeNowAfterThePaymentDue = newDateAndTimeToday_calendar.after(newPaymentDue_calendar);
                isDateAndTimeNowBeforeThePaymentDue = newDateAndTimeToday_calendar.before(newPaymentDue_calendar);
                
                if(isDateAndTimeNowEqualToPaymentDue || isDateAndTimeNowAfterThePaymentDue){
                    recordForfeitOfUser();
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DetectIfUserForfeitsReservation.class.getName()).log(Level.SEVERE, null, ex);
            print.println(ex.getMessage() + "<br>");
            print.flush();
        }
    }
    
    public void recordForfeitOfUser(){
        try {
            query = "INSERT INTO TRISTANROSALES.TBLRECORDSOFCANCELLATIONANDFORFEITS "
                + "(RCF_EMAILADDRESS,RCF_RESERVATIONREFERENCENUMBER,RCF_TYPEOFRECORD,"
                + "RCF_BLOCKEDUNTIL,RCF_DATEANDTIMEHAPPENED) VALUES(?,?,?,?,?)";
                
            pstmt = con.prepareStatement(query);
                
            pstmt.setString(1, user_emailAddress);
            pstmt.setString(2, user_reservationReferenceNumber);
            pstmt.setString(3, "Forfeited");
            
            detectIfUserForfeitsReservationThreeTimes();
            
            pstmt.setString(4, blockedUntil);
            pstmt.setString(5, dateAndTimeHappened);
        } catch (SQLException ex) {
            Logger.getLogger(DetectIfUserForfeitsReservation.class.getName()).log(Level.SEVERE, null, ex);
            print.println(ex.getMessage() + "<br>");
            print.flush();
        }
    }
    
    public void detectIfUserForfeitsReservationThreeTimes(){
        try {
            pstmt = con.prepareStatement("SELECT COUNT(*) FROM TRISTANROSALES.TBLRECORDSOFCANCELLATIONANDFORFEITS "
                    + "WHERE RCF_TYPEOFRECORD=? AND RCF_EMAILADDRESS=?");
            pstmt.setString(1, "Forfeited");
            pstmt.setString(2, user_emailAddress);
            
            count = pstmt.executeUpdate();
            
            if(count >= 3){
                SimpleDateFormat sdf_dateFormat = new SimpleDateFormat("E, MMMMM dd, yyyy");
                Calendar cal = Calendar.getInstance();
                // get starting date
                cal.add(Calendar.DAY_OF_WEEK, 30);
                
                blockedUntil = sdf_dateFormat.format(cal.getTime());
            }else{
                blockedUntil = "N/A";
            }
        } catch (SQLException ex) {
            Logger.getLogger(DetectIfUserForfeitsReservation.class.getName()).log(Level.SEVERE, null, ex);
            print.println(ex.getMessage() + "<br>");
            print.flush();
        }
    }
    
    public void createTableForRecordsOfCancellationAndForteits(){
        try {
            query = "CREATE TABLE TRISTANROSALES.TBLRECORDSOFCANCELLATIONANDFORFEITS ("
                + "RCF_ID INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
                + "RCF_EMAILADDRESS VARCHAR(32672) NOT NULL,"
                + "RCF_RESERVATIONREFERENCENUMBER VARCHAR(32672) NOT NULL,"
                + "RCF_TYPEOFRECORD VARCHAR(32672) NOT NULL,"
                + "RCF_BLOCKEDUNTIL VARCHAR(32672) NOT NULL,"
                + "RCF_DATEANDTIMEHAPPENED VARCHAR(32672) NOT NULL)";
            
            pstmt = con.prepareStatement(query);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DetectIfUserForfeitsReservation.class.getName()).log(Level.SEVERE, null, ex);
            print.println(ex.getMessage() + "<br>");
            print.flush();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            print = response.getWriter();
            processRequest(request, response);
        } catch (ParseException ex) {
            Logger.getLogger(DetectIfUserForfeitsReservation.class.getName()).log(Level.SEVERE, null, ex);
            print.println(ex.getMessage() + "<br>");
            print.flush();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            print = response.getWriter();
            processRequest(request, response);
        } catch (ParseException ex) {
            Logger.getLogger(DetectIfUserForfeitsReservation.class.getName()).log(Level.SEVERE, null, ex);
            print.println(ex.getMessage() + "<br>");
            print.flush();
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
