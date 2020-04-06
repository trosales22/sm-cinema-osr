package com.smcinema.classes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ValidateAdmin {
    public static boolean checkAdmin(String admin_emailAddress, String admin_password){
        boolean status = false;
        
        try{
            Connection con = DatabaseManager.getConnection();
            PreparedStatement pstmt = con.prepareStatement("SELECT * FROM TRISTANROSALES.TBLUSERSANDADMINS "
                    + "WHERE USER_EMAILADDRESS=? AND USER_PASSWORD=?");
            pstmt.setString(1, admin_emailAddress);
            pstmt.setString(2, admin_password);
            
            ResultSet rs = pstmt.executeQuery();
            status = rs.next();
        }catch(Exception ex){
            System.out.print(ex.getMessage());
        }
        
        return status;
    }
}
