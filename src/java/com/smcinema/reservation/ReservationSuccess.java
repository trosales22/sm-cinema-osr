package com.smcinema.reservation;

import com.google.gson.JsonObject;
import com.smcinema.classes.DatabaseManager;
import com.smcinema.classes.ReservationsBean;
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

public class ReservationSuccess extends HttpServlet {
    Connection con;
    PreparedStatement pstmt;
    Statement stmt;
    ResultSet rs;
    DatabaseMetaData dbmd;
    
    String user_emailAddress,movieName,movieBranchName,movieAuditoriumName,
        movieTypeOfSeating,movieScreeningDate,movieStartTime,moviePrice,
        movieSeatQuantity,paymentOption,query,movieReference,movieSelectedSeats;
    
    int count,reservation_randomNumber,totalAmount;
    PrintWriter print;
    ReservationsBean reservation = new ReservationsBean();
    JsonObject status = new JsonObject();
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException{
        try{  
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");

            con = DatabaseManager.getConnection();
            dbmd = con.getMetaData();

            try{                           
                user_emailAddress = (String) request.getAttribute("user_emailAddress");
                movieName = (String) request.getAttribute("movieName");
                movieBranchName = (String) request.getAttribute("movieBranchName");
                movieAuditoriumName = (String) request.getAttribute("movieAuditoriumName");
                movieTypeOfSeating = (String) request.getAttribute("movieTypeOfSeating");
                movieScreeningDate = (String) request.getAttribute("movieScreeningDate");
                movieStartTime = (String) request.getAttribute("movieStartTime");
                moviePrice = (String) request.getAttribute("moviePrice");
                movieSeatQuantity = (String) request.getAttribute("movieSeatQuantity");
                paymentOption = (String) request.getAttribute("paymentOption");
                movieSelectedSeats = (String) request.getAttribute("movieSelectedSeats");

                generateMovieReservationReferenceNumber();

                reservation.setMovieReservation_movieReference(movieReference);
                reservation.setMovieReservation_randomNumber(reservation_randomNumber);
                reservation.setMovieReservation_referenceNumber(reservation.getMovieReservation_movieReference() + "-" + reservation.getMovieReservation_randomNumber());

                reservation.setMovieReservation_movieName(movieName);
                reservation.setMovieReservation_movieBranchName(movieBranchName);
                reservation.setMovieReservation_movieAuditoriumName(movieAuditoriumName);
                reservation.setMovieReservation_movieTypeOfSeating(movieTypeOfSeating);
                reservation.setMovieReservation_movieScreeningDate(movieScreeningDate);
                reservation.setMovieReservation_movieStartTime(movieStartTime);
                reservation.setMovieReservation_moviePrice(moviePrice);
                reservation.setMovieReservation_movieSeatQuantity(movieSeatQuantity);

                if(reservation.getMovieReservation_movieTypeOfSeating().equals("Free Seating")){
                    reservation.setMovieReservation_movieOccupiedSeats("N/A");
                }else{
                    reservation.setMovieReservation_movieOccupiedSeats(movieSelectedSeats);
                }

                reservation.setMovieReservation_modeOfPayment(paymentOption);

                if(reservation.getMovieReservation_modeOfPayment().equals("PayPal")){
                    reservation.setMovieReservation_status("Paid");
                }else{
                    reservation.setMovieReservation_status("Pending");
                }

                reservation.setMovieReservation_reservedBy(user_emailAddress);

                //create a java timestamp object that represents the current time (i.e., a "current timestamp")
                SimpleDateFormat sdf_dateCreated = new SimpleDateFormat("MMMMM dd, yyyy | hh:mm a");
                Calendar dateCreated = Calendar.getInstance();

                SimpleDateFormat sdf_startTime = new SimpleDateFormat("hh:mma");
                Date date = sdf_startTime.parse(reservation.getMovieReservation_movieStartTime());
                Calendar startTime = Calendar.getInstance();

                startTime.setTime(date);
                startTime.add(Calendar.MINUTE, -45);

                String paymentDue_time = sdf_startTime.format(startTime.getTime());


                reservation.setMovieReservation_dateAndTimeReserved(sdf_dateCreated.format(dateCreated.getTime()));

                reservation.setMovieReservation_paymentDue(reservation.getMovieReservation_movieScreeningDate() + " | " + paymentDue_time);


                totalAmount = (int) ((Double.parseDouble(reservation.getMovieReservation_moviePrice())
                        * Integer.parseInt(reservation.getMovieReservation_movieSeatQuantity())) 
                        + (20 * Integer.parseInt(reservation.getMovieReservation_movieSeatQuantity())));

                stmt = con.createStatement();

                rs = dbmd.getTables(null, null, "TBLRESERVATIONSREPORT", null);
                if(rs.next()){
                    //table exists
                    determineIfUserHasRecordOfCancellation(response);
                }else{
                    //table does not exist
                    createTableForReservationsReport();
                    determineIfUserHasRecordOfCancellation(response);
                }
            } catch (SQLException ex) {
                Logger.getLogger(ReservationSuccess.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReservationSuccess.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void generateMovieReservationReferenceNumber(){
        try {
            pstmt = con.prepareStatement("SELECT * FROM TRISTANROSALES.TBLMOVIECINEMA WHERE MOVIE_NAME=?");
            pstmt.setString(1, movieName);
            
            rs = pstmt.executeQuery();
            
            while(rs.next()){
                movieReference = rs.getString("MOVIE_REFERENCE");
                generateRandomNumber();
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReservationSuccess.class.getName()).log(Level.SEVERE, null, ex);
            print.println(ex.getMessage() + "<br>");
            print.flush();
        }
    }
    
    public void generateRandomNumber(){
        reservation_randomNumber = (int) ((Math.random() * 9000000) + 1000000);
    }
    
    public void determineIfUserHasRecordOfCancellation(HttpServletResponse response) throws IOException{
        try {
            pstmt = con.prepareStatement("SELECT * FROM TRISTANROSALES.TBLRECORDSOFCANCELLATIONANDFORFEITS "
                    + "WHERE RCF_EMAILADDRESS=?");
            pstmt.setString(1, user_emailAddress);
            
            count = pstmt.executeUpdate();
            //rs = pstmt.executeQuery();
            
            if(count >= 3){
                status.addProperty("status", "failedBecauseOfRecordOfCancellation");
                response.getWriter().write(status.toString());
            }else{
                reserveTicketViaOnSitePayment(response);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReservationSuccess.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void reserveTicketViaOnSitePayment(HttpServletResponse response) throws IOException{
        try {
            query = "INSERT INTO TRISTANROSALES.TBLRESERVATIONSREPORT (RESERVATION_MOVIERANDOMNUMBER,"
                    + "RESERVATION_MOVIEREFERENCE,RESERVATION_REFERENCENUMBER,RESERVATION_MOVIENAME,RESERVATION_MOVIEBRANCHNAME,RESERVATION_MOVIEAUDITORIUMNAME,"
                    + "RESERVATION_MOVIETYPEOFSEATING,RESERVATION_MOVIESCREENINGDATE,RESERVATION_MOVIESTARTTIME,"
                    + "RESERVATION_MOVIEPRICE,RESERVATION_MOVIESEATQUANTITY,RESERVATION_OCCUPIEDSEATS,RESERVATION_STATUS,"
                    + "RESERVATION_MODEOFPAYMENT,RESERVATION_MOVIERESERVEDBY,RESERVATION_PAYMENTDUE,RESERVATION_DATEANDTIMERESERVED) "
                    + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                
                pstmt = con.prepareStatement(query);
                
                pstmt.setInt(1, reservation.getMovieReservation_randomNumber());
                pstmt.setString(2, reservation.getMovieReservation_movieReference());
                pstmt.setString(3, reservation.getMovieReservation_referenceNumber());
                pstmt.setString(4, reservation.getMovieReservation_movieName());
                pstmt.setString(5, reservation.getMovieReservation_movieBranchName());
                pstmt.setString(6, reservation.getMovieReservation_movieAuditoriumName());
                pstmt.setString(7, reservation.getMovieReservation_movieTypeOfSeating());
                pstmt.setString(8, reservation.getMovieReservation_movieScreeningDate());
                pstmt.setString(9, reservation.getMovieReservation_movieStartTime());
                pstmt.setString(10, reservation.getMovieReservation_moviePrice());
                pstmt.setString(11, reservation.getMovieReservation_movieSeatQuantity());
                pstmt.setString(12, reservation.getMovieReservation_movieOccupiedSeats());
                pstmt.setString(13, reservation.getMovieReservation_status());
                pstmt.setString(14, reservation.getMovieReservation_modeOfPayment());
                pstmt.setString(15, reservation.getMovieReservation_reservedBy());
                pstmt.setString(16, reservation.getMovieReservation_paymentDue());
                pstmt.setString(17, reservation.getMovieReservation_dateAndTimeReserved());
                
                count = pstmt.executeUpdate();
                
                if(count > 0){
                    /*
                    print.println("Your movie ticket was a success! Here are the details of your booking:<br>");
                    print.println("Movie Reference Number: " + reservation.getMovieReservation_referenceNumber()  + "<br>");
                    print.println("Movie Name: " + reservation.getMovieReservation_movieName() + "<br>");
                    print.println("Movie Branch Name: " + reservation.getMovieReservation_movieBranchName() + "<br>");
                    print.println("Movie Auditorium Name: " + reservation.getMovieReservation_movieAuditoriumName() + "<br>");
                    print.println("Movie Type Of Seating: " + reservation.getMovieReservation_movieTypeOfSeating() + "<br>");
                    print.println("Movie Screening Date: " + reservation.getMovieReservation_movieScreeningDate() + "<br>");
                    print.println("Movie Start Time: " + reservation.getMovieReservation_movieStartTime() + "<br>");
                    print.println("Movie Price: " + reservation.getMovieReservation_moviePrice() + "<br>");
                    print.println("Movie Seat Quantity: " + reservation.getMovieReservation_movieSeatQuantity() + "<br>");
                    print.println("Movie Occupied Seat (s): " + reservation.getMovieReservation_movieOccupiedSeats() + "<br>");
                    print.println("Reservation Status: " + reservation.getMovieReservation_status() + "<br>");
                    print.println("Reservation Mode Of Payment: " + reservation.getMovieReservation_modeOfPayment() + "<br>");
                    print.println("Movie Reserved By: " + reservation.getMovieReservation_reservedBy() + "<br>");
                    print.println("Movie Date & Time Reserved: " + reservation.getMovieReservation_dateAndTimeReserved() + "<br>");
                    print.println("Payment Due: " + reservation.getMovieReservation_paymentDue() + "<br>");
                    print.flush();
                    */
                    
                    //sendEmailConfirmation();
                    
                    status.addProperty("status", "successToReserved");
                    response.getWriter().write(status.toString());
                }else{
                    status.addProperty("status", "failedToReserved");
                    response.getWriter().write(status.toString());
                }
                
                con.close(); 
        } catch (SQLException ex) {
            Logger.getLogger(ReservationSuccess.class.getName()).log(Level.SEVERE, null, ex);
            
            /*
            print.println("Movie Reference Number: " + reservation.getMovieReservation_referenceNumber()  + "<br>");
            print.println("Movie Name: " + reservation.getMovieReservation_movieName() + "<br>");
            print.println("Movie Branch Name: " + reservation.getMovieReservation_movieBranchName() + "<br>");
            print.println("Movie Auditorium Name: " + reservation.getMovieReservation_movieAuditoriumName() + "<br>");
            print.println("Movie Type Of Seating: " + reservation.getMovieReservation_movieTypeOfSeating() + "<br>");
            print.println("Movie Screening Date: " + reservation.getMovieReservation_movieScreeningDate() + "<br>");
            print.println("Movie Start Time: " + reservation.getMovieReservation_movieStartTime() + "<br>");
            print.println("Movie Price: " + reservation.getMovieReservation_moviePrice() + "<br>");
            print.println("Movie Seat Quantity: " + reservation.getMovieReservation_movieSeatQuantity() + "<br>");
            print.println("Movie Occupied Seat (s): " + reservation.getMovieReservation_movieOccupiedSeats() + "<br>");
            print.println("Reservation Status: " + reservation.getMovieReservation_status() + "<br>");
            print.println("Reservation Mode Of Payment: " + reservation.getMovieReservation_modeOfPayment() + "<br>");
            print.println("Movie Reserved By: " + reservation.getMovieReservation_reservedBy() + "<br>");
            print.println("Movie Date & Time Reserved: " + reservation.getMovieReservation_dateAndTimeReserved() + "<br>");
            print.println("Payment Due: " + reservation.getMovieReservation_paymentDue() + "<br>");
            print.flush();
            */
        }
    }
    
    public void sendEmailConfirmation() {
        try {
            String username = "tristanrosales0@gmail.com";
            String password = "goandroid22";
            
            // Recipient's email ID needs to be mentioned.
            String to = reservation.getMovieReservation_reservedBy();
            
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
            message.setSubject("SM Cinema Online");
            
            // Now set the actual message
            message.setText("Hello " + reservation.getMovieReservation_reservedBy() + ",\n\n"
                    + "Your movie ticket reservation was a success! Here are the details of your booking:\n\n"
                    + "Reference Number:\n" + reservation.getMovieReservation_referenceNumber() + "\n\n"
                    + "Date & Time Purchased: " + reservation.getMovieReservation_dateAndTimeReserved() + "\n"
                    + "Branch: " + reservation.getMovieReservation_movieBranchName() + "\n"
                    + "Auditorium: " + reservation.getMovieReservation_movieAuditoriumName() + "\n"
                    + "Movie Title: " + reservation.getMovieReservation_movieName() + "\n"
                    + "Screening Date: " + reservation.getMovieReservation_movieScreeningDate() + "\n"
                    + "Screening Time: " + reservation.getMovieReservation_movieStartTime() + "\n"
                    + "Screening Type: " + reservation.getMovieReservation_movieTypeOfSeating() + "\n"
                    + "Seat/s: " + reservation.getMovieReservation_movieOccupiedSeats() + "\n"
                    + "Price: " + reservation.getMovieReservation_moviePrice() + "\n"
                    + "Convenience Fee: 20\n"
                    + "Ticket Qty: " + reservation.getMovieReservation_movieSeatQuantity() + "\n"
                    + "Total Amount: " + totalAmount + "\n"
                    + "Payment Due: " + reservation.getMovieReservation_paymentDue() + "\n\n"
                    
                    + "Head to the SM Cinema Ticket Booth 30 minutes prior to the movie screening time for payment.\n"
                    + "Claim your tickets on time to avoid the cancellation of your reservation.\n\n"
                    + "Please be reminded that users with an unpaid active reservation will not be allowed to make new ticket reservation.\n\n"
                    + "Thank you for your choosing SM Cinema\n");
            
            Transport.send(message);
            print.println("Sent successfully. Please check your email...<br>");
            print.flush();
        } catch (AddressException ex) {
            Logger.getLogger(ReservationSuccess.class.getName()).log(Level.SEVERE, null, ex);
            print.println(ex.getMessage() + "<br>");
            print.flush();
        } catch (MessagingException ex) {
            Logger.getLogger(ReservationSuccess.class.getName()).log(Level.SEVERE, null, ex);
            print.println(ex.getMessage() + "<br>");
            print.flush();
        }
    }
    
    public void createTableForReservationsReport(){
        try {
            String queryForAddingTableForReservationsReport;
            
            queryForAddingTableForReservationsReport = "CREATE TABLE TRISTANROSALES.TBLRESERVATIONSREPORT ("
                + "RESERVATION_ID INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
                + "RESERVATION_MOVIERANDOMNUMBER VARCHAR(32672) NOT NULL,"
                + "RESERVATION_MOVIEREFERENCE VARCHAR(32672) NOT NULL,"
                + "RESERVATION_REFERENCENUMBER VARCHAR(32672) NOT NULL,"
                + "RESERVATION_MOVIENAME VARCHAR(32672) NOT NULL,"
                + "RESERVATION_MOVIEBRANCHNAME VARCHAR(32672) NOT NULL," 
                + "RESERVATION_MOVIEAUDITORIUMNAME VARCHAR(32672) NOT NULL,"
                + "RESERVATION_MOVIETYPEOFSEATING VARCHAR(32672) NOT NULL,"
                + "RESERVATION_MOVIESCREENINGDATE VARCHAR(32672) NOT NULL,"
                + "RESERVATION_MOVIESTARTTIME VARCHAR(32672) NOT NULL,"
                + "RESERVATION_MOVIEPRICE VARCHAR(32672) NOT NULL,"
                + "RESERVATION_MOVIESEATQUANTITY VARCHAR(32672) NOT NULL,"
                + "RESERVATION_OCCUPIEDSEATS VARCHAR(32672) NOT NULL,"
                + "RESERVATION_STATUS VARCHAR(32672) NOT NULL,"
                + "RESERVATION_MODEOFPAYMENT VARCHAR(32672) NOT NULL,"
                + "RESERVATION_MOVIERESERVEDBY VARCHAR(32672) NOT NULL,"
                + "RESERVATION_PAYMENTDUE VARCHAR(32672) NOT NULL,"
                + "RESERVATION_DATEANDTIMERESERVED VARCHAR(32672) NOT NULL)";
            
                pstmt = con.prepareStatement(queryForAddingTableForReservationsReport);
                pstmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ReservationSuccess.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(ReservationSuccess.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(ReservationSuccess.class.getName()).log(Level.SEVERE, null, ex);
            print.println(ex.getMessage() + "<br>");
            print.flush();
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
