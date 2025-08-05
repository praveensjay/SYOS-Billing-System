#!/bin/bash

# Enable strict mode
set -euo pipefail

# Define variables
TOMCAT_HOME="/c/Program Files/Apache Software Foundation/Tomcat"
TOMCAT_WEBAPPS="$TOMCAT_HOME/webapps"
PROJECT_DIR="/c/Users/asus/Desktop/SYOS-MultiTier"
CLIENT_WAR="$PROJECT_DIR/SYOS-Client/target/SYOS-Client.war"
SERVER_WAR="$PROJECT_DIR/SYOS-Server/target/SYOS-Server.war"

echo "[INFO] Starting deployment process..."

# Step 1: Navigate to project directory & Build WAR files
echo "[INFO] Building project with Maven..."
cd "$PROJECT_DIR"
mvn clean package -DskipTests || {
    echo "[ERROR] Build failed! Fix errors before deploying."
    exit 1
}

# Step 2: Stop Tomcat if running
echo "[INFO] Checking if Tomcat is running on port 8080..."
if netstat -ano | grep ":8080" > /dev/null; then
    echo "[INFO] Tomcat is running. Attempting graceful shutdown..."
    "$TOMCAT_HOME/bin/shutdown.bat"
    sleep 5

    if netstat -ano | grep ":8080" > /dev/null; then
        echo "[WARN] Tomcat did not shut down. Forcing termination..."
        taskkill //F //IM java.exe > /dev/null 2>&1 || true
        sleep 5
    else
        echo "[INFO] Tomcat shut down successfully."
    fi
else
    echo "[INFO] Tomcat is not running. Skipping shutdown."
fi

# Step 3: Verify WAR files exist
if [[ ! -f "$CLIENT_WAR" || ! -f "$SERVER_WAR" ]]; then
    echo "[ERROR] WAR files not found. Ensure the build was successful."
    exit 1
fi

# Step 4: Remove old deployments and copy new WAR files
echo "[INFO] Cleaning old deployments..."
rm -rf "$TOMCAT_WEBAPPS/SYOS-Client"
rm -rf "$TOMCAT_WEBAPPS/SYOS-Server"
rm -f "$TOMCAT_WEBAPPS/SYOS-Client.war"
rm -f "$TOMCAT_WEBAPPS/SYOS-Server.war"

echo "[INFO] Deploying new WAR files..."
cp "$CLIENT_WAR" "$TOMCAT_WEBAPPS/"
cp "$SERVER_WAR" "$TOMCAT_WEBAPPS/"

# Step 5: Start Tomcat
echo "[INFO] Starting Tomcat server..."
"$TOMCAT_HOME/bin/startup.bat"
sleep 5

# Step 6: Confirm deployment
echo "[INFO] Deployment completed successfully."
echo "[INFO] Visit the following URLs:"
echo "  - Server: http://localhost:8080/SYOS-Server/"
echo "  - Client: http://localhost:8080/SYOS-Client/"

# Step 7: Check port status
echo "[INFO] Checking port 8080 usage..."
netstat -ano | grep 8080 || echo "[INFO] No process detected on port 8080."
