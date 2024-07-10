# E-commerce Microservices Application

## Overview
This is an e-commerce application built using microservices architecture with Spring Boot. The application consists of the following microservices:

- **Order Service**
- **Payment Service**
- **Product Service**
- **Customer Service**
- **Notification Service**

Each microservice has its own independent database and runs inside a Docker container. The microservices communicate with each other using Feign Client, RestTemplate, and Kafka for asynchronous messaging.

## Microservices

### Order Service
- Manages orders and communicates with the Payment and Customer services.
- Uses Feign Client and RestTemplate for inter-service communication.
- Produces messages to Kafka for order processing events.

### Payment Service
- Handles payment transactions.
- Produces messages to Kafka for payment events.

### Product Service
- Manages product information.

### Customer Service
- Manages customer information.

### Notification Service
- Consumes messages from Kafka for order and payment events.
- Sends notifications via email.

## Architecture

### Communication
- **Feign Client** and **RestTemplate** are used for synchronous communication between services.
- **Kafka** is used as an asynchronous message broker. The Order and Payment services produce messages, and the Notification service consumes these messages.

### Configuration
- **Config Server**: Microservices fetch their database and Kafka configurations from the Config Server.

### Service Discovery
- **Eureka Discovery Server**: Provides service discovery. Each microservice registers with the Eureka server, allowing them to discover and communicate with each other.

### API Gateway
- **API Gateway**: Routes incoming requests to the appropriate microservice based on the request path.

### Distributed Log Tracing
- **Zipkin**: Used for distributed log tracing, allowing tracking of requests as they propagate through the microservices.