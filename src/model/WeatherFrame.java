package model;
import weather.WeatherService;
import db.WeatherDAO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WeatherFrame extends JFrame {
    private WeatherService weatherService;
    private WeatherDAO weatherDAO;
    private JLabel locationLabel;
    private JLabel temperatureLabel;
    private JLabel humidityLabel;
    private JLabel descriptionLabel;
    private JLabel weatherIconLabel;
    private JTextField searchField;
    private JButton searchButton;
    private JButton refreshButton;

    public WeatherFrame() {
        this.weatherService = new WeatherService();
        this.weatherDAO = new WeatherDAO();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Weather Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 500);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main panel with border
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(Color.WHITE);

        // Search panel
        JPanel searchPanel = createSearchPanel();
        mainPanel.add(searchPanel, BorderLayout.NORTH);

        // Weather display panel
        JPanel weatherPanel = createWeatherPanel();
        mainPanel.add(weatherPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new BorderLayout(10, 0));
        searchPanel.setBackground(Color.WHITE);

        searchField = new JTextField();
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        searchField.setToolTipText("Enter city name (e.g., London, New York, Tokyo)");

        searchButton = new JButton("Search");
        searchButton.setFont(new Font("Arial", Font.BOLD, 12));
        searchButton.setBackground(new Color(70, 130, 180));
        searchButton.setForeground(Color.WHITE);
        searchButton.setFocusPainted(false);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchWeather();
            }
        });

        // Enter key listener for search field
        searchField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchWeather();
            }
        });

        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);

        return searchPanel;
    }

    private JPanel createWeatherPanel() {
        JPanel weatherPanel = new JPanel();
        weatherPanel.setLayout(new BoxLayout(weatherPanel, BoxLayout.Y_AXIS));
        weatherPanel.setBackground(Color.WHITE);
        weatherPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));

        // Weather icon
        weatherIconLabel = new JLabel();
        weatherIconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        weatherIconLabel.setPreferredSize(new Dimension(100, 100));
        weatherIconLabel.setText("⛅");
        weatherIconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));

        // Location
        locationLabel = new JLabel("Enter a city name");
        locationLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        locationLabel.setFont(new Font("Arial", Font.BOLD, 20));
        locationLabel.setForeground(new Color(70, 130, 180));

        // Temperature
        temperatureLabel = new JLabel("--°C");
        temperatureLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        temperatureLabel.setFont(new Font("Arial", Font.BOLD, 32));
        temperatureLabel.setForeground(Color.BLACK);

        // Description
        descriptionLabel = new JLabel("--");
        descriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        descriptionLabel.setForeground(Color.GRAY);

        // Humidity
        humidityLabel = new JLabel("Humidity: --%");
        humidityLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        humidityLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        humidityLabel.setForeground(Color.DARK_GRAY);

        // Add components with spacing
        weatherPanel.add(Box.createVerticalStrut(20));
        weatherPanel.add(weatherIconLabel);
        weatherPanel.add(Box.createVerticalStrut(15));
        weatherPanel.add(locationLabel);
        weatherPanel.add(Box.createVerticalStrut(10));
        weatherPanel.add(temperatureLabel);
        weatherPanel.add(Box.createVerticalStrut(10));
        weatherPanel.add(descriptionLabel);
        weatherPanel.add(Box.createVerticalStrut(15));
        weatherPanel.add(humidityLabel);
        weatherPanel.add(Box.createVerticalStrut(20));

        return weatherPanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);

        refreshButton = new JButton("Refresh");
        refreshButton.setFont(new Font("Arial", Font.BOLD, 12));
        refreshButton.setBackground(new Color(34, 139, 34));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFocusPainted(false);

        JButton exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Arial", Font.BOLD, 12));
        exitButton.setBackground(new Color(178, 34, 34));
        exitButton.setForeground(Color.WHITE);
        exitButton.setFocusPainted(false);

        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshWeather();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        buttonPanel.add(refreshButton);
        buttonPanel.add(exitButton);

        return buttonPanel;
    }

    private void searchWeather() {
        String location = searchField.getText().trim();
        if (location.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a city name", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        System.out.println("🔍 Searching weather for: " + location);
        
        // Show loading indicator
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        searchButton.setEnabled(false);
        locationLabel.setText("Searching...");

        // Use SwingWorker to prevent UI freezing
        SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
            private Weather weather;
            private String errorMessage;

            @Override
            protected Boolean doInBackground() throws Exception {
                try {
                    weather = weatherService.getWeather(location);
                    return true;
                } catch (Exception e) {
                    errorMessage = e.getMessage();
                    return false;
                }
            }

            @Override
            protected void done() {
                setCursor(Cursor.getDefaultCursor());
                searchButton.setEnabled(true);
                
                try {
                    if (get()) {
                        displayWeatherData(weather);
                        saveWeatherToDatabase(weather); // Save to database after successful search
                    } else {
                        locationLabel.setText("City Not Found");
                        temperatureLabel.setText("--°C");
                        descriptionLabel.setText("--");
                        humidityLabel.setText("Humidity: --%");
                        
                        // Show helpful error message
                        String message = errorMessage;
                        if (errorMessage.contains("not found")) {
                            message = errorMessage + "\n\nTry: London, New York, Tokyo, Paris, Delhi, etc.";
                        }
                        
                        JOptionPane.showMessageDialog(WeatherFrame.this, 
                            message, "Weather Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception e) {
                    locationLabel.setText("Error");
                    JOptionPane.showMessageDialog(WeatherFrame.this, 
                        "Unexpected error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };

        worker.execute();
    }

    private void refreshWeather() {
        String currentLocation = locationLabel.getText();
        if (!currentLocation.equals("Enter a city name") && !currentLocation.equals("Error") && !currentLocation.equals("Searching...") && !currentLocation.equals("City Not Found")) {
            searchField.setText(currentLocation);
            searchWeather();
        } else {
            JOptionPane.showMessageDialog(this, "No location to refresh", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void displayWeatherData(Weather weather) {
        locationLabel.setText(weather.getLocation());
        temperatureLabel.setText(String.format("%.1f°C", weather.getTemperature()));
        descriptionLabel.setText(weather.getDescription());
        humidityLabel.setText(String.format("Humidity: %d%%", weather.getHumidity()));
        
        // Update icon based on weather description
        updateWeatherIcon(weather.getDescription());
        
        System.out.println("Weather data displayed for: " + weather.getLocation());
    }

    private void saveWeatherToDatabase(Weather weather) {
        try {
            weatherDAO.saveWeather(weather);
            System.out.println(" Successfully saved weather data to database: " + weather.getLocation());
        } catch (Exception e) {
            System.err.println("Failed to save weather data to database: " + e.getMessage());
            // Don't show error to user - database saving is optional
            // You can uncomment the line below for debugging:
            // e.printStackTrace();
        }
    }

    private void updateWeatherIcon(String description) {
        String desc = description.toLowerCase();
        String iconText = "⛅"; // default
        
        if (desc.contains("sun") || desc.contains("clear")) {
            iconText = "☀️";
        } else if (desc.contains("cloud")) {
            iconText = "☁️";
        } else if (desc.contains("rain") || desc.contains("drizzle")) {
            iconText = "🌧️";
        } else if (desc.contains("snow")) {
            iconText = "❄️";
        } else if (desc.contains("storm") || desc.contains("thunder")) {
            iconText = "⛈️";
        } else if (desc.contains("fog") || desc.contains("mist")) {
            iconText = "🌫️";
        }
        
        weatherIconLabel.setText(iconText);
        weatherIconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
    }
}