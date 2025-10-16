import model.WeatherFrame;
import db.DBConnection;
import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        System.out.println("Starting Weather Application...");
        
        // Test database connection first
        System.out.println("=== DATABASE CONNECTION TEST ===");
        try {
            Connection conn = DBConnection.getConnection();
            if (conn != null) {
                System.out.println("✅ Database connection test: PASSED");
                conn.close();
            }
        } catch (Exception e) {
            System.out.println("❌ Database connection test: FAILED");
            System.out.println("Error: " + e.getMessage());
        }
        System.out.println("=================================");
        
        // Start the GUI regardless
        new WeatherFrame();
    }
}