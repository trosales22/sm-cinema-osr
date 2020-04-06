package com.smcinema.classes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ValidateUser {
    public static boolean checkUserOrAdmin(String emailAddress, String password){
        boolean status = false;
        
        try{
            Connection con = DatabaseManager.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM TRISTANROSALES.TBLUSERSANDADMINS "
                    + "WHERE USER_EMAILADDRESS=? AND USER_PASSWORD=? AND USER_STATUS=?");
            ps.setString(1, emailAddress);
            ps.setString(2, password);
            ps.setString(3, "Verified");
            ResultSet rs = ps.executeQuery();
            status = rs.next();
        }catch(Exception ex){
            System.out.print(ex.getMessage());
        }
        
        return status;
    }
}
