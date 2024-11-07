//package ABC;

import java.sql.*;
import java.util.Scanner;
public class Main {

    public static void main(String[] args) {
    	System.out.println("Connection Established...........");
        //String url = "jdbc:mysql://10.10.13.97:3306/sys"; for college
        String url = "jdbc:mysql://localhost:3306/test_db"; //for home
        String user = "root";
        String password = "root";

        try (Connection con = DriverManager.getConnection(url, user, password);
             Statement stmt = con.createStatement();
             Scanner scanner = new Scanner(System.in)) {

            // Register the driver class
            //Class.forName("com.mysql.cj.jdbc.Driver");

            // Create table if it doesn't exist
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS city2 (" +
                               "id INT UNSIGNED NOT NULL AUTO_INCREMENT, " +
                               "PRIMARY KEY(id), " +
                               "name VARCHAR(30))");

            while (true) {
                // Display menu
                System.out.println("\nMenu:");
                System.out.println("1. Add City");
                System.out.println("2. Delete City");
                System.out.println("3. Search City");
                System.out.println("4. Show All Cities");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); 

                switch (choice) {
                    case 1:
                        System.out.print("Enter city name to add: ");
                        String cityToAdd = scanner.nextLine();
                        addCity(con, cityToAdd);
                        break;

                    case 2:
                        System.out.print("Enter city name to delete: ");
                        String cityToDelete = scanner.nextLine();
                        deleteCity(con, cityToDelete);
                        break;

                    case 3:
                        System.out.print("Enter city name to search: ");
                        String cityToSearch = scanner.nextLine();
                        searchCity(con, cityToSearch);
                        break;

                    case 4:
                        showAllCities(con);
                        break;

                    case 5:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        }

        catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }
    }

    private static void addCity(Connection con, String city) {
        String sql = "INSERT INTO city2 (name) VALUES (?)";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, city);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("City added successfully.");
            } else {
                System.out.println("Failed to add city.");
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception while adding city: " + e.getMessage());
        }
    }
    
    private static void showAllCities(Connection con) {
        String sql = "SELECT * FROM city2";
        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("ID\tName");
            while (rs.next()) {
                System.out.println(rs.getInt("id") + "\t" + rs.getString("name"));
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception while showing cities: " + e.getMessage());
        }
    }
    
    private static void deleteCity(Connection con, String city) {
        String sql = "DELETE FROM city2 WHERE name = ?";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, city);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("City deleted successfully.");
            } else {
                System.out.println("City not found.");
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception while deleting city: " + e.getMessage());
        }
    }

    private static void searchCity(Connection con, String city) {
        String sql = "SELECT * FROM city2 WHERE name = ?";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, city);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    System.out.println("City found: ID = " + rs.getInt("id") + ", Name = " + rs.getString("name"));
                } else {
                    System.out.println("City not found.");
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception while searching city: " + e.getMessage());
        }
    }
}
