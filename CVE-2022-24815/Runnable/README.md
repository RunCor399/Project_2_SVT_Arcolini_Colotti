# Runnable

This directory contains two bash scripts that can be used to start the application: 

- `run-docker.sh` will build the docker image from the executable `.jar` and it will start the application in a container. The application will start a HTTP server at address `localhost` and port `8080`.
- `run-local.sh` will directly execute the `.jar` on the local machine. Please note that a JVM is required to execute the `.jar` file. 