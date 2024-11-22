import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.PreparedStatement;

public class Prepared_Statement {
    public static void main(String[] args) throws ClassNotFoundException{

        String url = "jdbc:mysql://localhost:3306/mydatabase";
        String username = "root";
        String password = "1234";
        String query = "INSERT INTO employees(id, name, job_title, salary)" +
                " VALUES(?, ?, ?, ?)";                            //      to insert data into databases
//        String query = "DELETE FROM employees WHERE id = " + "?";
        String query2 = "Select * from employees;";

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Drivers loaded Successfully!!!");
        }catch (ClassNotFoundException e){
            System.out.println(e.getMessage());
        }

        try{
            Connection con = DriverManager.getConnection(url, username, password);
            System.out.println("Connection Established Successfully!!!");
//            Statement stmt = con.createStatement();
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, 3);
            ps.setString(2, "Prabhat");
            ps.setString(3, "DevOps Engineer");
            ps.setDouble(4, 85000.5);
//            ps.setInt(1, 7);          to delete database from database
            int rowsAffected = ps.executeUpdate();
            if(rowsAffected > 0){
                System.out.println("Data Inserted Successfully!!!");
            }
            else {
                System.out.println("Data Insertion Failed !!");
            }

            ResultSet rs = ps.executeQuery(query2);
            while (rs.next()){
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String job_title = rs.getString("job_title");
                double salary = rs.getDouble("salary");

                System.out.println("Id: " + id);
                System.out.println("Name: " + name);
                System.out.println("Job Title: " + job_title);
                System.out.println("Salary: " + salary);
            }

            rs.close();
            ps.close();
            con.close();
            System.out.println();
            System.out.println("Connection Closed Successfully!!!");

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

    }
}
