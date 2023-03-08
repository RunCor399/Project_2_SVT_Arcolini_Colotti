#!/bin/bash

# Check if Java 17 is installed
java_version=$(java --version 2>&1 | head -n 1)
expected_version="java 17.0.6 2023-01-17 LTS"

if [ "$java_version" != "$expected_version" ]; then
    echo "Java 17 is not installed or the version installed is not supported. Please install Java 17 and try again."
    exit 1
fi

# Start the vulnerable application
java -jar ./target/vulnerable-application.jar
