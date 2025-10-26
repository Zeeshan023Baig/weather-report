@echo off
echo === DEBUG COMPILATION ===
echo Cleaning...
del src\weather\*.class 2>nul

echo Step 1: Compiling WeatherService...
javac -cp ".;lib/mysql-connector-j-8.0.33.jar;src" src/weather/WeatherService.java

if errorlevel 1 (
    echo ❌ WeatherService compilation failed!
    echo Check WeatherService.java for errors
) else (
    echo ✅ WeatherService compiled successfully!
    dir src\weather\*.class
)

pause