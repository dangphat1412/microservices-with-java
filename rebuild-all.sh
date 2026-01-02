#!/bin/bash

# Script to rebuild all microservices
set -e

echo "=========================================="
echo "Rebuilding All Microservices"
echo "=========================================="

SERVICES=("configserver" "eurekaserver" "accounts" "loans" "cards" "gatewayserver")

for service in "${SERVICES[@]}"; do
    echo ""
    echo "===================="
    echo "Building $service..."
    echo "===================="
    cd "$service"

    # Build with Maven
    echo "Running Maven build for $service..."
    mvn clean package -DskipTests

    # Build Docker image
    echo "Building Docker image for $service..."
    docker build -t dangphat1412/$service:latest .

    cd ..
    echo "✅ $service build completed"
done

echo ""
echo "=========================================="
echo "All services built successfully!"
echo "=========================================="
echo ""
echo "To restart services, run:"
echo "  cd docker-compose/default"
echo "  docker-compose down"
echo "  docker-compose up -d"

