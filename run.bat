@echo off
echo Starting Weather Application Build...
echo Cleaning previous build...
if exist out rmdir /s /q out

echo Creating output directory...
mkdir out

echo Compiling all files...
javac -cp ".;mysql-connector-j-8.0.33.jar" -d out src\*.java src\db\*.java src\model\*.java src\weather\*.java

if %errorlevel% equ 0 (
    echo.
    echo ✅ Build successful!
    echo 🚀 Running application...
    echo.
    java -cp "out;mysql-connector-j-8.0.33.jar" Main
) else (
    echo ---
    echo ❌ ERROR: Compilation failed!
    echo ---
)

pausea