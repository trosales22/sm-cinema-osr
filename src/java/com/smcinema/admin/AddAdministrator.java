package com.smcinema.admin;

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
import java.util.logging.Level;
import java.util.logging.Logger; 
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddAdministrator extends HttpServlet {
    PrintWriter print;
    UserBean user = new UserBean();
    Connection con;
    PreparedStatement pstmt;
    Statement stmt;
    ResultSet rs;
    DatabaseMetaData dbmd;
    JsonObject status = new JsonObject();
    
    String query,user_emailAddress,userType,txtBirthMonth;
    int count,txtBirthDay,txtBirthYear;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException {    
        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            
            con = DatabaseManager.getConnection();
            dbmd = con.getMetaData();
            
            user_emailAddress = (String) request.getSession().getAttribute("COOKIE_USER_EMAILADDRESS");
            
            user.setFirstname(request.getParameter("txtFirstname"));
            user.setLastname(request.getParameter("txtLastname"));
            user.setEmailAddress(request.getParameter("txtEmailAddress"));
            user.setPassword(request.getParameter("txtPassword"));
            user.setAccountType(request.getParameter("txtAccountType"));
            user.setNameOfYourFavoritePet("TBF");
            user.setStatus("Verified");
            
            txtBirthMonth = request.getParameter("txtBirthMonth");
            txtBirthDay = Integer.parseInt(request.getParameter("txtBirthDay"));
            txtBirthYear = Integer.parseInt(request.getParameter("txtBirthYear"));
            
            //create a java timestamp object that represents the current time (i.e., a "current timestamp")
            SimpleDateFormat sdf_dateAndTimeCreated = new SimpleDateFormat("MMMMM dd, yyyy | hh:mm a");
            Calendar dateAndTimeCreated = Calendar.getInstance();
                    
            user.setDateAndTimeCreated(sdf_dateAndTimeCreated.format(dateAndTimeCreated.getTime()));
            
            stmt = con.createStatement();
            
            rs = dbmd.getTables(null, null, "TBLUSERSANDADMINS", null);
            if(rs.next()){
                //table exists
                detectIfUserIsAnITAdministrator(response);
            }else{
                //table does not exist
                createTableForUsersAndAdmins();
                detectIfUserIsAnITAdministrator(response);
            }
        } catch (SQLException ex) {
            Logger.getLogger(AddAdministrator.class.getName()).log(Level.SEVERE, null, ex);
            print.println(ex.getMessage());
            print.flush();
        }
    }
    
    public void detectIfUserIsAnITAdministrator(HttpServletResponse response) throws IOException{
        try {
            pstmt = con.prepareStatement("SELECT * FROM TRISTANROSALES.TBLUSERSANDADMINS "
                    + "WHERE USER_EMAILADDRESS=?");
            pstmt.setString(1, user_emailAddress);
            
            rs = pstmt.executeQuery();
            
            if(rs.next()){
                userType = rs.getString("USER_TYPE");
                
                if(userType.equals("IT Administrator")){
                    registerUserOrAdmin(response);
                }else{
                    status.addProperty("isAddingOfUserOrAdminSuccess", "accessDenied");
                    response.getWriter().write(status.toString());
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(AddAdministrator.class.getName()).log(Level.SEVERE, null, ex);
            response.getWriter().write(ex.getMessage());
        }
    }
    
    public void registerUserOrAdmin(HttpServletResponse response) throws IOException{
        try {
            rs = stmt.executeQuery("SELECT user_emailAddress FROM TRISTANROSALES.TBLUSERSANDADMINS "
                    + "WHERE USER_EMAILADDRESS LIKE '%" + user.getEmailAddress() + "%'");
            
            if(rs.next()){
                status.addProperty("isAddingOfUserOrAdminSuccess", "emailExists");
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
                
                stmt = con.createStatement();
                              
                pstmt.executeUpdate();
                
                status.addProperty("isAddingOfUserOrAdminSuccess", true);
                response.getWriter().write(status.toString());

                con.close();         
            }
        } catch (SQLException ex) {
            Logger.getLogger(AddAdministrator.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(AddAdministrator.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(AddAdministrator.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(AddAdministrator.class.getName()).log(Level.SEVERE, null, ex);
            print.println(ex.getMessage() + "<br>");
            print.flush();
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
