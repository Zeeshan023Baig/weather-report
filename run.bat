@echo off
chcp 65001 > nul
echo Starting Weather Application...

echo Cleaning previous compilation...
if exist bin rmdir /s /q bin
mkdir bin

echo Compiling all files...
javac -cp ".;lib\*" -d bin src\db\*.java src\model\*.java src\weather\*.java src\Main.java

if %errorlevel% neq 0 (
    echo ERROR: Compilation failed!
    pause
    exit /b 1
)

echo Running Application...
java -cp ".;bin;lib\*" Main

pause