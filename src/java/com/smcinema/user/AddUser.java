package com.smcinema.user;

import com.google.gson.JsonObject;
import com.smcinema.classes.DatabaseManager;
import com.smcinema.classes.UserBean;
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

public class AddUser extends HttpServlet { 
    PrintWriter print;
    UserBean user = new UserBean();
    Connection con;
    PreparedStatement pstmt;
    Statement stmt;
    ResultSet rs;
    DatabaseMetaData dbmd;   
    JsonObject status = new JsonObject();   
    
    int count,yearNow,txtBirthDay,txtBirthYear;
    String query,txtBirthMonth;	
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException {
            try{  
                response.setContentType("application/json");
                response.setCharacterEncoding("utf-8");
            
                con = DatabaseManager.getConnection();
                dbmd = con.getMetaData();
                
                try{
                    user.setFirstname(request.getParameter("txtFirstname"));
                    user.setLastname(request.getParameter("txtLastname"));
                    user.setEmailAddress(request.getParameter("txtEmailAddress"));    
                    user.setPassword(request.getParameter("txtPassword"));
                    user.setAccountType("Customer");
                    user.setNameOfYourFavoritePet("TBF");
                    user.setStatus("Verified");
                    
                    txtBirthMonth = request.getParameter("txtBirthMonth");
                    txtBirthDay = Integer.parseInt(request.getParameter("txtBirthDay"));
                    txtBirthYear = Integer.parseInt(request.getParameter("txtBirthYear"));
            
                    yearNow = Calendar.getInstance().get(Calendar.YEAR);
                    
                    //create a java timestamp object that represents the current time (i.e., a "current timestamp")
                    SimpleDateFormat sdf_dateAndTimeCreated = new SimpleDateFormat("MMMMM dd, yyyy | hh:mm a");
                    Calendar dateAndTimeCreated = Calendar.getInstance();

                    user.setDateAndTimeCreated(sdf_dateAndTimeCreated.format(dateAndTimeCreated.getTime()));
                    
                    stmt = con.createStatement();
                    
                    rs = dbmd.getTables(null, null, "TBLUSERSANDADMINS", null);
                    if(rs.next()){
                        //table exists
                        registerUser(response);
                    }else{
                        //table does not exist
                        createTableForUsersAndAdmins();
                        registerUser(response);
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(AddUser.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (SQLException ex) {
                Logger.getLogger(AddUser.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    
    public void registerUser(HttpServletResponse response) throws IOException{
        try {
            rs = stmt.executeQuery("SELECT USER_EMAILADDRESS FROM TRISTANROSALES.TBLUSERSANDADMINS "
                    + "WHERE USER_EMAILADDRESS LIKE '%" + user.getEmailAddress() + "%'");
            
            if(rs.next()){
                status.addProperty("isRegisterOfUserSuccess", "emailExists");
                response.getWriter().write(status.toString());
            }else{
                if((yearNow - txtBirthYear) < 18){
                    status.addProperty("isRegisterOfUserSuccess", "notEligible");
                    response.getWriter().write(status.toString());
                }else{
                    query = "INSERT INTO TRISTANROSALES.TBLUSERSANDADMINS (user_firstname,user_lastname,"
                            + "user_emailAddress,user_password,user_type,"
                            + "user_nameOfFavoritePet,user_status,user_birthMonth,user_birthDay,"
                            + "user_birthYear,user_dateAndTimeCreated) "
                            + "VALUES(?,?,?,?,?,?,?,?,?,?,?)";
                
                    pstmt = con.prepareStatement(query);

                    pstmt.setString(1, user.getFirstname());
                    pstmt.setString(2, user.getLastname());
                    pstmt.setString(3, user.getEmailAddress());
                    pstmt.setString(4, user.getPassword());
                    pstmt.setString(5, user.getAccountType());
                    pstmt.setString(6, user.getNameOfYourFavoritePet());
                    pstmt.setString(7, user.getStatus());
                    pstmt.setString(8, txtBirthMonth);
                    pstmt.setInt(9, txtBirthDay);
                    pstmt.setInt(10, txtBirthYear);
                    pstmt.setString(11, user.getDateAndTimeCreated());

                    pstmt.executeUpdate();
                    
                    sendEmailConfirmation(response);
                    status.addProperty("isRegisterOfUserSuccess", true);
                    response.getWriter().write(status.toString());
                }
            }
            con.close(); 
        } catch (SQLException ex) {
            Logger.getLogger(AddUser.class.getName()).log(Level.SEVERE, null, ex);
            response.getWriter().write(ex.getMessage());
        }
    }
    
    public void createTableForUsersAndAdmins(){
        try {
            String queryForAddingTableForUsersAndAdmins;
            
            queryForAddingTableForUsersAndAdmins = "CREATE TABLE TRISTANROSALES.tblUsersAndAdmins ("
                + "USER_ID INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
                + "USER_FIRSTNAME VARCHAR(32672) NOT NULL,"
                + "USER_LASTNAME VARCHAR(32672) NOT NULL,"
                + "USER_EMAILADDRESS VARCHAR(32672) NOT NULL,"
                + "USER_PASSWORD VARCHAR(32672) NOT NULL,"
                + "USER_TYPE VARCHAR(32672) NOT NULL,"
                + "USER_NAMEOFFAVORITEPET VARCHAR(32672) NOT NULL,"
                + "USER_STATUS VARCHAR(32672) NOT NULL,"
                + "USER_BIRTHMONTH VARCHAR(32672) NOT NULL,"
                + "USER_BIRTHDAY VARCHAR(32672) NOT NULL,"
                + "USER_BIRTHYEAR VARCHAR(32672) NOT NULL,"
                + "USER_DATEANDTIMECREATED VARCHAR(32672) NOT NULL)";
            
                pstmt = con.prepareStatement(queryForAddingTableForUsersAndAdmins);
                pstmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(AddUser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void sendEmailConfirmation(HttpServletResponse response) throws IOException {
        try {
            String username = "tristanrosales0@gmail.com";
            String password = "goandroid22";
            
            // Recipient's email ID needs to be mentioned.
            String to = user.getEmailAddress();
            
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
            message.setText("Dear " + user.getFirstname() + ",\n\n"
                    + "Thank you for registering for the SM Cinema Website.\n\n"
                    + "Below is your account information:\n"
                    + "Email: " + user.getEmailAddress() + "\n"
                    + "Name: " + user.getFirstname() + " " + user.getLastname() + "\n"
                    + "Date of Birth: " + txtBirthMonth + " " + txtBirthDay + ", " + txtBirthYear + "\n"
                    + "You can now log in and start purchasing tickets.\n"
                    + "Welcome to SM Cinema!\n\n"
                    + "Regards,\nSM Cinema.\n\n"
                    + "Do not reply. This email is computer generated from SM Cinema.\n");
            
            Transport.send(message);
            
            //status.addProperty("isRegisterOfUserSuccess", "emailConfirmationSuccess");
            //response.getWriter().write(status.toString());
                    
            //print.println("Send successfully. Please check your email...<br>");
            //print.flush();
        } catch (AddressException ex) {
            Logger.getLogger(AddUser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(AddUser.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(AddUser.class.getName()).log(Level.SEVERE, null, ex);
            response.getWriter().write(ex.getMessage());
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            print = response.getWriter();
            processRequest(request, response);
        } catch (ParseException ex) {
            Logger.getLogger(AddUser.class.getName()).log(Level.SEVERE, null, ex);
            response.getWriter().write(ex.getMessage());
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
