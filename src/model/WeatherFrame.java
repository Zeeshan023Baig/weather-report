package model;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import db.WeatherDAO;

public class WeatherFrame extends JFrame {
    private JTextField cityField;
    private JTextArea resultArea;
    private JButton getWeatherBtn;
    private JButton showHistoryBtn;
    private JButton debugBtn; // Add debug button
    
    public WeatherFrame() {
        setTitle("Weather Application");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initializeComponents();
        setupLayout();
        setupEventListeners();
        
        setVisible(true);
    }
    
    private void initializeComponents() {
        cityField = new JTextField(20);
        resultArea = new JTextArea(15, 40);
        resultArea.setEditable(false);
        getWeatherBtn = new JButton("Get Weather");
        showHistoryBtn = new JButton("Show History");
        debugBtn = new JButton("Debug Test"); // Initialize debug button
        
        resultArea.setBorder(BorderFactory.createTitledBorder("Weather Information"));
        resultArea.setFont(new Font("Arial", Font.PLAIN, 14));
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Top panel for input
        JPanel topPanel = new JPanel();
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.add(new JLabel("Enter City:"));
        topPanel.add(cityField);
        topPanel.add(getWeatherBtn);
        topPanel.add(showHistoryBtn);
        topPanel.add(debugBtn); // Add debug button to panel
        
        // Center panel for results
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private void setupEventListeners() {
        getWeatherBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getWeather();
            }
        });
        
        showHistoryBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showHistory();
            }
        });
        
        debugBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WeatherDAO.testDatabaseManually();
            }
        });
        
        cityField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getWeather();
            }
        });
    }
    
    private void getWeather() {
        String city = cityField.getText().trim();
        
        if (city.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a city name", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        resultArea.setText("Fetching weather data for " + city + "...");
        
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000); // Simulate API call
                    
                    // Mock weather data
                    double temperature = 22.0;
                    String condition = "Sunny";
                    int humidity = 65;
                    double windSpeed = 15.0;
                    int pressure = 1013;
                    
                    // Save to database
                    WeatherDAO.saveSearch(city, temperature, condition, humidity, windSpeed, pressure);
                    
                    String weatherInfo = "Weather for: " + city + "\n\n" +
                                       "Temperature: " + temperature + "°C\n" +
                                       "Condition: " + condition + "\n" +
                                       "Humidity: " + humidity + "%\n" +
                                       "Wind Speed: " + windSpeed + " km/h\n" +
                                       "Pressure: " + pressure + " hPa\n\n" +
                                       "✅ Saved to database!";
                    
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            resultArea.setText(weatherInfo);
                        }
                    });
                    
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }
    
    private void showHistory() {
        String history = WeatherDAO.getSearchHistory();
        resultArea.setText("=== SEARCH HISTORY ===\n\n" + history);
    }
}