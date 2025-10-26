package model;

public class Weather {
    private String location;
    private double temperature;
    private String description;
    private int humidity;
    private double windSpeed;
    
    // This constructor matches what your WeatherService is using
    public Weather(String location, double temperature, String description, int humidity, double windSpeed) {
        this.location = location;
        this.temperature = temperature;
        this.description = description;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
    }
    
    // Getters
    public String getLocation() { return location; }
    public double getTemperature() { return temperature; }
    public String getDescription() { return description; }
    public int getHumidity() { return humidity; }
    public double getWindSpeed() { return windSpeed; }
    
    @Override
    public String toString() {
        return String.format("Weather{city='%s', temperature=%.1f°C, condition='%s'}", location, temperature, description);
    }
}