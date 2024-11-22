import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class image_Handling {
    public static void main(String[] args) {

        String url = "jdbc:mysql://localhost:3306/mydatabase";
        String username = "root";
        String password = "1234";

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Drivers loaded Successfully!!!");
        }catch (ClassNotFoundException e){
            System.out.println(e.getMessage());
        }

        try{
            Connection con = DriverManager.getConnection(url, username, password);
            System.out.println("Connection Established Successfully!!!");
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }


    }
}
