import model.WeatherFrame;
import db.DBConnection;
import db.WeatherDAO;
import java.sql.Connection;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        System.out.println("Starting Weather Application...");
        System.out.println("🌤️  Weather App v1.0 with MySQL");
        System.out.println("================================");
        
        // Initialize database
        initializeDatabase();
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    WeatherFrame frame = new WeatherFrame();
                    frame.setVisible(true);
                    System.out.println("✅ GUI loaded successfully!");
                    System.out.println("💡 Search for any city to test the application!");
                } catch (Exception e) {
                    System.err.println("❌ Error loading GUI: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }
    
    private static void initializeDatabase() {
        System.out.println("🧪 Initializing MySQL database...");
        Connection conn = DBConnection.getConnection();
        if (conn != null) {
            try {
                WeatherDAO weatherDAO = new WeatherDAO();
                weatherDAO.createTable();
                System.out.println("🎉 MySQL database initialized successfully!");
                
                // Show recent searches if any exist
                weatherDAO.displayRecentSearches();
                conn.close();
            } catch (Exception e) {
                System.err.println("❌ Error initializing database: " + e.getMessage());
                System.out.println("💡 The app will work, but data won't be saved to database");
            }
        } else {
            System.out.println("💡 Running without database persistence");
            System.out.println("💡 Weather data will not be saved between sessions");
        }
    }
}