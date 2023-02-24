#!/bin/bash
docker rm -f vulnerableapp
docker build -t vulnerableapp . && \
	docker run --name=vulnerableapp --rm -p:8080:8080 -it vulnerableapp
