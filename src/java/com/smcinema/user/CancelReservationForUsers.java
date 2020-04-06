package com.smcinema.user;

import com.google.gson.JsonObject;
import com.smcinema.classes.DatabaseManager;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CancelReservationForUsers extends HttpServlet {
    Connection con;
    PreparedStatement pstmt;
    Statement stmt;
    ResultSet rs;
    DatabaseMetaData dbmd;
    JsonObject status = new JsonObject();
    
    String query,user_emailAddress,user_reservationReferenceNumber,blockedUntil,dateAndTimeHappened;
    int count;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            
            con = DatabaseManager.getConnection();
            dbmd = con.getMetaData();
            
            user_emailAddress = request.getParameter("user_emailAddress");
            user_reservationReferenceNumber = request.getParameter("reference_number");
            
            SimpleDateFormat sdf_dateAndTimeHappened = new SimpleDateFormat("MMMMM dd, yyyy | hh:mm a");
            Calendar calendar_dateAndTimeHappened = Calendar.getInstance();
            
            dateAndTimeHappened = sdf_dateAndTimeHappened.format(calendar_dateAndTimeHappened.getTime());
                    
            rs = dbmd.getTables(null, null, "TBLRECORDSOFCANCELLATIONANDFORFEITS", null);
            if(rs.next()){
                //table exists
                recordCancellationOfUser(response);
            }else{
                //table does not exist
                createTableForRecordsOfCancellationAndForteits();
                recordCancellationOfUser(response);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CancelReservationForUsers.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void recordCancellationOfUser(HttpServletResponse response) throws IOException{
        try {
            query = "INSERT INTO TRISTANROSALES.TBLRECORDSOFCANCELLATIONANDFORFEITS "
                    + "(RCF_EMAILADDRESS,RCF_RESERVATIONREFERENCENUMBER,RCF_TYPEOFRECORD,"
                    + "RCF_DATEANDTIMEHAPPENED) VALUES(?,?,?,?)";
                
            pstmt = con.prepareStatement(query);
                
            pstmt.setString(1, user_emailAddress);
            pstmt.setString(2, user_reservationReferenceNumber);
            pstmt.setString(3, "Cancelled");
            pstmt.setString(4, dateAndTimeHappened);
            
            pstmt.executeUpdate();

            cancelReservation(response);
        } catch (SQLException ex) {
            Logger.getLogger(CancelReservationForUsers.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void cancelReservation(HttpServletResponse response) throws IOException{
        try {
            /*
            pstmt = con.prepareStatement("DELETE FROM TRISTANROSALES.TBLRESERVATIONSREPORT "
                    + "WHERE RESERVATION_MOVIERESERVEDBY=? AND RESERVATION_REFERENCENUMBER=?");
            */
            pstmt = con.prepareStatement("UPDATE TRISTANROSALES.TBLRESERVATIONSREPORT "
                    + "SET RESERVATION_STATUS=? WHERE RESERVATION_MOVIERESERVEDBY=? AND RESERVATION_REFERENCENUMBER=?");
            
            pstmt.setString(1, "Cancelled");
            pstmt.setString(2, user_emailAddress);
            pstmt.setString(3, user_reservationReferenceNumber);
            pstmt.executeUpdate();
             
            sendEmailConfirmation(response);
            status.addProperty("isCancelReservationForUserSuccess", true);
            response.getWriter().write(status.toString()); 

            detectIfUserCancelReservationThreeTimes();
        } catch (SQLException ex) {
            Logger.getLogger(CancelReservationForUsers.class.getName()).log(Level.SEVERE, null, ex);
            response.getWriter().write(ex.getMessage());
        }
    }
    
    public void detectIfUserCancelReservationThreeTimes(){
        try {
            pstmt = con.prepareStatement("SELECT COUNT(*) AS countOfCancellation FROM TRISTANROSALES.TBLRECORDSOFCANCELLATIONANDFORFEITS "
                    + "WHERE RCF_TYPEOFRECORD=? AND RCF_EMAILADDRESS=?");
            pstmt.setString(1, "Cancelled");
            pstmt.setString(2, user_emailAddress);
      
            rs = pstmt.executeQuery();
            
            while(rs.next()){
                count = rs.getInt("countOfCancellation");
            }
            
            if(count >= 3){
                rs = dbmd.getTables(null, null, "TBLBLOCKEDUSERS", null);
                if(rs.next()){
                    //table exists
                    recordToBlockUsers();
                }else{
                    //table does not exist
                    createTableForBlockedUsers();
                    recordToBlockUsers();
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(CancelReservationForUsers.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void recordToBlockUsers(){
        try {
            query = "INSERT INTO TRISTANROSALES.TBLBLOCKEDUSERS "
                    + "(USER_EMAILADDRESS,USER_BLOCKEDUNTIL) VALUES(?,?)";
            
            pstmt = con.prepareStatement(query);
                
            pstmt.setString(1, user_emailAddress);
            
            SimpleDateFormat sdf_dateFormat = new SimpleDateFormat("E, MMMMM dd, yyyy");
            Calendar cal = Calendar.getInstance();
            // get starting date
            cal.add(Calendar.DAY_OF_WEEK, 14);
            
            blockedUntil = sdf_dateFormat.format(cal.getTime());
            
            pstmt.setString(2, blockedUntil);
            
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(CancelReservationForUsers.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void createTableForRecordsOfCancellationAndForteits(){
        try {
            query = "CREATE TABLE TRISTANROSALES.TBLRECORDSOFCANCELLATIONANDFORFEITS ("
                    + "RCF_ID INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
                    + "RCF_EMAILADDRESS VARCHAR(32672) NOT NULL,"
                    + "RCF_RESERVATIONREFERENCENUMBER VARCHAR(32672) NOT NULL,"
                    + "RCF_TYPEOFRECORD VARCHAR(32672) NOT NULL,"
                    + "RCF_DATEANDTIMEHAPPENED VARCHAR(32672) NOT NULL)";
            
            pstmt = con.prepareStatement(query);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(CancelReservationForUsers.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void createTableForBlockedUsers(){
        try {
            String queryForAddingTableForBlockedUsers;
            
            queryForAddingTableForBlockedUsers = "CREATE TABLE TRISTANROSALES.TBLBLOCKEDUSERS ("
                + "USER_ID INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
                + "USER_EMAILADDRESS VARCHAR(32672) NOT NULL,"
                + "USER_BLOCKEDUNTIL VARCHAR(32672) NOT NULL)";
            
                pstmt = con.prepareStatement(queryForAddingTableForBlockedUsers);
                pstmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(CancelReservationForUsers.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void sendEmailConfirmation(HttpServletResponse response) throws IOException {
        try {
            String username = "tristanrosales0@gmail.com";
            String password = "goandroid22";
            
            // Recipient's email ID needs to be mentioned.
            String to = user_emailAddress;
            
            // Sender's email ID needs to be mentioned
            String from = "tristanrosales0@gmail.com";

            // Assuming you are sending email through relay.jangosmtp.net
            String host = "smtp.gmail.com";

            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.port", "587");

            // Get the default Session object.
            Session session = Session.getDefaultInstance(props, 
                new javax.mail.Authenticator(){
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(
                        username, password);// Specify the Username and the PassWord
                    }
            });

            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);
            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));
            // Set To: header field of the header.
            message.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(to));

            // Set Subject: header field
            message.setSubject("SM Cinema Registration");
            
            // Now set the actual message
            message.setText("Dear " + user_emailAddress + ",\n\n"
                    + "You've cancelled your reservation. Please check information below.\n\n"
                    + "Please take note that cancelling of reservation thrice may result to account suspension. Thank you!\n\n"
                    + "Reference Number: " + user_reservationReferenceNumber + "\n"
                    + "Regards,\nSM Cinema.\n\n"
                    + "Do not reply. This email is computer generated from SM Cinema.\n");
            
            Transport.send(message);
        } catch (AddressException ex) {
            Logger.getLogger(CancelReservationForUsers.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(CancelReservationForUsers.class.getName()).log(Level.SEVERE, null, ex);
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
