import java.sql.*;

    public class Main {
        public static void main(String[] args) throws ClassNotFoundException {

            String url ="jdbc:mysql://localhost:3306/mydatabase";
            String username = "root";
            String password = "1234";
            String query = "UPDATE employees " +
                    "SET job_title ='Full stack Developer', salary = '70000.0' " +
                    "WHERE id = 2;";
            String query2 = "SELECT * FROM employees;";

            try {
                Class.forName("com.mysql.jdbc.Driver");
                System.out.println("Drivers loaded successfully!!!");
            }catch (ClassNotFoundException e) {
                System.out.println(e.getMessage());
            }

            try{
                Connection con = DriverManager.getConnection(url, username, password);
                System.out.println("Connection Established Successfully!!!");
                Statement stmt = con.createStatement();
                int rowsaffected = stmt.executeUpdate(query);

                if(rowsaffected > 0){
                    System.out.println("UPDATE Successfull. " + rowsaffected + " row(s) affected");
                }else{
                    System.out.println("UPDATE Failed!!");
                }
                ResultSet rs = stmt.executeQuery(query2);
                while (rs.next()){
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    String job_title = rs.getString("job_title");
                    double salary = rs.getDouble("salary");

                    System.out.println();
                    System.out.println("=================================");

                    System.out.println("Id: " + id);
                    System.out.println("Name: " + name);
                    System.out.println("Job Title: " + job_title);
                    System.out.println("Salary: " + salary);

                }
                rs.close();
                stmt.close();
                con.close();

                System.out.println("\nConnection Closed Successfully!!!");

            }catch (SQLException e){
                System.out.println(e.getMessage());
            }



        }
    }