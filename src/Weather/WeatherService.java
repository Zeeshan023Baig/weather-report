package weather;
import model.Weather;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

public class WeatherService {
    private static final String API_KEY = "cb90687e03675cb44c4d08a1ddc7ae72";
    private static final String API_URL = "http://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=metric";
    
    public WeatherService() {
        // Simple constructor
    }
    
    public Weather getWeather(String location) {
        // Basic validation
        if (location == null || location.trim().isEmpty()) {
            throw new IllegalArgumentException("City name cannot be empty");
        }
        
        // Remove extra spaces and validate
        location = location.trim();
        if (!isValidCityName(location)) {
            throw new IllegalArgumentException("Invalid city name: " + location);
        }
        
        try {
            Weather weather = fetchRealWeatherData(location);
            System.out.println(" Successfully fetched weather for: " + location);
            return weather;
        } catch (Exception e) {
            System.err.println(" Failed to fetch real weather data: " + e.getMessage());
            
            // Check if it's a "city not found" error
            if (e.getMessage().contains("not found") || e.getMessage().contains("404")) {
                throw new IllegalArgumentException("City not found: '" + location + "'. Please check the spelling.");
            }
            
            System.out.println(" Using simulated data for: " + location);
            return getSimulatedWeather(location);
        }
    }
    
    private boolean isValidCityName(String cityName) {
        // Basic validation - city names should not contain special characters
        return cityName.matches("^[a-zA-Z\\s',.-]+$");
    }
    
    private Weather fetchRealWeatherData(String location) throws Exception {
        String formattedUrl = String.format(API_URL, location.replace(" ", "%20"), API_KEY);
        System.out.println(" Fetching from: " + formattedUrl);
        
        URL url = new URL(formattedUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(10000);
        connection.setReadTimeout(10000);
        
        int responseCode = connection.getResponseCode();
        System.out.println(" API Response Code: " + responseCode);
        
        if (responseCode == 200) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            
            System.out.println("API Response received");
            return parseWeatherData(response.toString(), location);
        } else if (responseCode == 401) {
            throw new Exception("Invalid API Key");
        } else if (responseCode == 404) {
            throw new Exception("City not found: " + location);
        } else if (responseCode == 429) {
            throw new Exception("API rate limit exceeded. Please try again later.");
        } else {
            throw new Exception("API request failed with code: " + responseCode);
        }
    }
    
    private Weather parseWeatherData(String jsonResponse, String location) {
        try {
            // Extract temperature
            int tempStart = jsonResponse.indexOf("\"temp\":") + 7;
            int tempEnd = jsonResponse.indexOf(",", tempStart);
            double temperature = Double.parseDouble(jsonResponse.substring(tempStart, tempEnd).trim());
            
            // Extract humidity
            int humidityStart = jsonResponse.indexOf("\"humidity\":") + 11;
            int humidityEnd = jsonResponse.indexOf(",", humidityStart);
            int humidity = Integer.parseInt(jsonResponse.substring(humidityStart, humidityEnd).trim());
            
            // Extract weather description
            int descStart = jsonResponse.indexOf("\"description\":\"") + 15;
            int descEnd = jsonResponse.indexOf("\"", descStart);
            String description = jsonResponse.substring(descStart, descEnd);
            
            // Extract wind speed
            double windSpeed = 0.0;
            try {
                int windStart = jsonResponse.indexOf("\"speed\":") + 8;
                int windEnd = jsonResponse.indexOf(",", windStart);
                if (windEnd == -1) windEnd = jsonResponse.indexOf("}", windStart);
                windSpeed = Double.parseDouble(jsonResponse.substring(windStart, windEnd).trim());
            } catch (Exception e) {
                System.out.println("Could not extract wind speed, using default");
            }
            
            // Capitalize description
            description = description.substring(0, 1).toUpperCase() + description.substring(1);
            
            System.out.println("Parsed weather: " + location + " - " + temperature + "°C - " + description);
            
            return new Weather(location, temperature, description, humidity, windSpeed);
            
        } catch (Exception e) {
            System.err.println(" Error parsing JSON: " + e.getMessage());
            throw new RuntimeException("Failed to parse weather data");
        }
    }
    
    private Weather getSimulatedWeather(String location) {
        Random random = new Random();
        double temperature = 15 + random.nextDouble() * 20;
        String[] descriptions = {"Sunny", "Cloudy", "Partly Cloudy", "Rainy", "Clear"};
        String description = descriptions[random.nextInt(descriptions.length)];
        int humidity = 30 + random.nextInt(50);
        double windSpeed = random.nextDouble() * 10;
        
        System.out.println("Using simulated data: " + location + " - " + temperature + "°C - " + description);
        
        return new Weather(location, temperature, description, humidity, windSpeed);
    }
    
    // Helper method to suggest similar city names (basic implementation)
    public String suggestCity(String wrongCity) {
        // You could implement a list of common cities and find the closest match
        // For now, just return a helpful message
        return "Please check the city name spelling. Try: London, New York, Tokyo, Paris, etc.";
    }
}