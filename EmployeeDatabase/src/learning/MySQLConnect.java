
package learning;

import java.sql.*;
import javax.swing.*;

public class MySQLConnect {
    String connectionString = "jdbc:mysql://localhost:3306/empdb";
    String userName = "root";
    String passWord = "";
    Connection conn =null;
    
    public Connection open(String connectionString, String userName, String passWord){
        
        try{

            conn = DriverManager.getConnection(connectionString, userName, passWord);
            //JOptionPane.showMessageDialog(null, "Connection Successfull");
            return conn; 
        }
        catch (SQLException e){
            JOptionPane.showMessageDialog(null, e.getMessage());
            return null;
        }
    }
       
  
}
