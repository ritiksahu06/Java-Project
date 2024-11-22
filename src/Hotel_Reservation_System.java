import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.Scanner;
import java.sql.Statement;
import java.sql.ResultSet;

public class Hotel_Reservation_System {

    public static final  String url = "jdbc:mysql://localhost:3306/hotel_db";
    public static final  String username = "root";
    public static final  String password = "1234";

    public static void main(String[] args) throws ClassNotFoundException, SQLException {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
//                System.out.println("Drivers Loaded Successfully!!!");
        }catch (ClassNotFoundException e){
            System.out.println(e.getMessage());
        }

        try {
            Connection con = DriverManager.getConnection(url, username, password);
//            System.out.println("Connection Established Successfully!!!");

            while(true){
                System.out.println();
                System.out.println("HOTEL MANAGEMENT SYSTEM");
                Scanner sc = new Scanner(System.in);
                System.out.println("1. Reserve a room");
                System.out.println("2. View Reservations");
                System.out.println("3. Get Room Number");
                System.out.println("4. Update Reservations");
                System.out.println("5. Delete Reservations");
                System.out.println("0. Exit");
                System.out.println("Choose an Option: ");
                int choice = sc.nextInt();
                switch (choice){
                    case 1:
                        reserveRoom(con, sc);
                        break;
                    case 2:
                        viewReservations(con);
                        break;
                    case 3:
                        getRoomNumber(con, sc);
                        break;
                    case 4:
                        updateReservation(con, sc);
                        break;
                    case 5:
                        deleteReservation(con, sc);
                    case 0:
                        exit();
                        sc.close();
                        return;
                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }catch (InterruptedException e){
            throw new RuntimeException(e);
        }

    }
    private static void reserveRoom(Connection con, Scanner sc){
        try{
            System.out.println("Enter guest name: ");
            String guestName = sc.next();
            sc.nextLine();
            System.out.println("Enter room number: ");
            int roomNumber = sc.nextInt();
            System.out.println("Enter contact Number: ");
            String contactNumber = sc.next();

            String sql = "INSERT INTO reservations (guest_name, room_number, contact_number) " +
                    "VALUES('"+ guestName +"', '"+ roomNumber +"', '"+ contactNumber +"')";

            try (Statement stmt = con.createStatement()){
                int affectedRows = stmt.executeUpdate(sql);
                if (affectedRows > 0){
                    System.out.println("Reservation successful!");
                }else {
                    System.out.println("Reservation failed.");
                }
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
    private static void viewReservations(Connection con) throws SQLException{

        String sql = "SELECT reservation_id, guest_name, room_number, contact_number, reservation_date FROM reservations;";

        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("Current Reservations:");
            System.out.println("+----------------+-----------------+---------------+----------------------+-------------------------+");
            System.out.println("| Reservation ID | Guest           | Room Number   | Contact Number       | Reservation Date        |");
            System.out.println("+----------------+-----------------+---------------+----------------------+-------------------------+");

            while (rs.next()){
                int reservationId = rs.getInt("reservation_id");
                String guestName = rs.getString("guest_name");
                int roomNumber = rs.getInt("room_number");
                String contactNumber = rs.getString("contact_number");
                String reservationDate = rs.getTimestamp("reservation_date").toString();

//                format and display teh reservation date in a table-like format
                System.out.printf("| %-14d | %-15s | %-13d | %-20s | %-19s |\n",
                        reservationId, guestName, roomNumber, contactNumber, reservationDate);
            }
            System.out.println("+----------------+-----------------+---------------+----------------------+-------------------------+");
        }

    }

    private static void getRoomNumber(Connection con, Scanner sc){
        try {
            System.out.println("Enter reservation ID: ");
            int reservationId = sc.nextInt();
            System.out.println("Enter guest name: ");
            String guestName = sc.next();

            String sql = "SELECT room_number FROM reservations " +
                    "WHERE reservation_id = '" + reservationId + "'" +
                    "AND guest_name = '" + guestName + "'";
            try (Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){

                if(rs.next()){
                    int roomNumber = rs.getInt("room_number");
                    System.out.println("Room number for Reservation ID " + reservationId +
                            " and guest " + guestName + " is: " + roomNumber);
                }else {
                    System.out.println("Reservation not found for the given ID and guest name.");
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    private static void updateReservation(Connection con, Scanner sc){
        try {
            System.out.println("Enter reservation ID to update: ");
            int reservationId = sc.nextInt();
            sc.nextLine(); // Consume the newLine character

            if(!reservationExists(con, reservationId)){
                System.out.println("Reservation not found for the given ID.");
                return;
            }
            System.out.println("Enter new guest name: ");
            String newGuestName = sc.nextLine();
            System.out.println("Enter new room number: ");
            int newRoomNumber = sc.nextInt();
            System.out.println("Enter new contact number: ");
            String newContactNumber = sc.next();

            String sql = "UPDATE reservations SET guest_name = '"+ newGuestName +"', " +
                    "room_number = "+ newRoomNumber + "," +
                    "contact_number = '"+ newContactNumber+"' " +
                    "WHERE reservation_id = " + reservationId;

            try (Statement stmt = con.createStatement()){
                int affectedRows = stmt.executeUpdate(sql);
                if(affectedRows > 0){
                    System.out.println("Reservation updte successfully!");
                }else {
                    System.out.println("Reservation update failed.");
                }
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    private static void deleteReservation(Connection con, Scanner sc){
        try{
            System.out.println("Enter Reservation ID to delete: ");
            int reservationId = sc.nextInt();

            if(!reservationExists(con, reservationId)){
                System.out.println("Reservation not found for the given ID. ");
                return;
            }

            String sql = "DELETE FROM reservations WHERE reservation_id = "+ reservationId;
            try(Statement stmt = con.createStatement()){
                int affectedRows = stmt.executeUpdate(sql);
                if(affectedRows > 0){
                    System.out.println("Reservation deleted successfully!");
                }else {
                    System.out.println("Reservation deletion failed.");
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    private static boolean reservationExists(Connection con, int reservationid){

        try {
            String sql = "SELECT reservation_id FROM reservations WHERE reservation_id = "+ reservationid;

            try (Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
                return rs.next();

            }
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    private static void exit() throws InterruptedException{
        System.out.print("Exiting System");
        int i=0;
        while (i<5){
            System.out.print(".");
            Thread.sleep(450);
            i++;
        }
        System.out.println();
        System.out.println("Thankyou for Using Hotel Reservation System!!!");
    }







}
