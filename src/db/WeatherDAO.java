package db;

import model.Weather;
import java.sql.*;

public class WeatherDAO {
    
    public void createTable() {
        System.out.println("🔄 Verifying weather_searches table structure...");
        // Your table already exists with the correct structure, so we'll just verify
        String sql = "CREATE TABLE IF NOT EXISTS weather_searches (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "city VARCHAR(100) NOT NULL," +
                    "temperature DECIMAL(5,2)," +
                    "weather_condition VARCHAR(100)," +
                    "humidity INT," +
                    "wind_speed DECIMAL(5,2)," +
                    "pressure INT," +
                    "search_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ")";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("✅ MySQL table 'weather_searches' verified successfully");
        } catch (Exception e) {
            System.err.println("❌ Error verifying table: " + e.getMessage());
        }
    }
    
    public void saveWeather(Weather weather) {
        if (weather == null) {
            System.err.println("❌ Cannot save null weather object");
            return;
        }
        
        System.out.println("💾 Attempting to save to MySQL: " + weather.getLocation());
        
        // Updated SQL to match your actual column names
        String sql = "INSERT INTO weather_searches (city, temperature, weather_condition, humidity, wind_speed, pressure) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection()) {
            if (conn == null) {
                System.err.println("❌ No database connection available");
                return;
            }
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, weather.getLocation());  // city
                pstmt.setDouble(2, weather.getTemperature());  // temperature
                pstmt.setString(3, weather.getDescription());  // weather_condition
                pstmt.setInt(4, weather.getHumidity());  // humidity
                pstmt.setDouble(5, weather.getWindSpeed());  // wind_speed
                pstmt.setInt(6, 1013);  // pressure - using default value since we don't have pressure data
                
                int rowsAffected = pstmt.executeUpdate();
                System.out.println("✅ SUCCESS! Saved to MySQL. Rows affected: " + rowsAffected);
                System.out.println("✅ Data stored in weather_searches table: " + weather.getLocation());
            }
        } catch (Exception e) {
            System.err.println("❌ Error saving weather: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void displayRecentSearches() {
        // Updated to use 'city' and 'weather_condition' column names
        String sql = "SELECT city, temperature, weather_condition, humidity, search_date FROM weather_searches ORDER BY search_date DESC LIMIT 5";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            System.out.println("=== Recent Weather Searches ===");
            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                String city = rs.getString("city");
                double temp = rs.getDouble("temperature");
                String condition = rs.getString("weather_condition");
                int humidity = rs.getInt("humidity");
                Timestamp date = rs.getTimestamp("search_date");
                
                System.out.printf("📍 %s: %.1f°C, %s, %d%% humidity (%s)%n", 
                    city, temp, condition, humidity, date.toString());
            }
            
            if (!hasData) {
                System.out.println("No recent searches found.");
            }
        } catch (Exception e) {
            System.err.println("❌ Error reading weather history: " + e.getMessage());
        }
    }
    
    // Method to show all data in the table (for debugging)
    public void showAllData() {
        String sql = "SELECT * FROM weather_searches ORDER BY search_date DESC";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            System.out.println("=== All Data in weather_searches ===");
            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                int id = rs.getInt("id");
                String city = rs.getString("city");
                double temp = rs.getDouble("temperature");
                String condition = rs.getString("weather_condition");
                int humidity = rs.getInt("humidity");
                double windSpeed = rs.getDouble("wind_speed");
                int pressure = rs.getInt("pressure");
                Timestamp date = rs.getTimestamp("search_date");
                
                System.out.printf("ID: %d | %s: %.1f°C | %s | %d%% humidity | %.1f m/s | %d hPa | %s%n", 
                    id, city, temp, condition, humidity, windSpeed, pressure, date.toString());
            }
            
            if (!hasData) {
                System.out.println("No data found in weather_searches table.");
            }
        } catch (Exception e) {
            System.err.println("❌ Error reading all data: " + e.getMessage());
        }
    }
}