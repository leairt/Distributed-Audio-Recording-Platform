# Distributed Audio-Recording Platform

A distributed audio recording and streaming platform built with Java EE technologies. The system implements a microservices-like architecture with multiple subsystems communicating through JMS (Java Message Service) and exposing RESTful APIs.

## Overview

This platform enables users to upload, categorize, and manage audio recordings while supporting a subscription-based model with different package tiers. The system tracks user activity, audio playback statistics, and manages pricing dynamically across different time periods.

## Architecture

The application consists of four main components:

### Central Server
- Core REST API gateway
- Coordinates communication between subsystems
- Handles client requests and routing
- Implements business logic orchestration

### Subsystem 1: User and Location Management
- User registration and authentication
- Location (city) management
- User profile management
- Exposes REST endpoints for user operations
- Publishes user-related events via JMS

### Subsystem 2: Audio Recording Management
- Audio file upload and storage
- Category management and assignment
- Audio metadata handling
- Recording ownership tracking
- Publishes audio-related events via JMS

### Subsystem 3: Package and Statistics Management
- Subscription package management
- Dynamic pricing based on time periods
- User subscription handling
- Audio playback tracking
- Comprehensive statistics generation
- Consumes events from other subsystems via JMS

### Client Application
- User interface for platform interaction
- Consumes REST API endpoints
- Handles user sessions and authentication

## Technology Stack

- **Java EE**: Core application framework
- **JPA (Java Persistence API)**: Object-relational mapping and database operations
- **JMS (Java Message Service)**: Asynchronous inter-subsystem communication
- **REST (JAX-RS)**: HTTP-based API for client-server communication
- **MySQL**: Relational database management
- **Maven**: Dependency management and build automation

## Key Features

### User Management
- User registration with demographic data
- Email and username uniqueness validation
- Location-based user organization
- User profile updates

### Audio Recording Management
- Audio file upload with metadata
- Multi-category classification support
- Duration tracking
- Owner attribution
- Timestamp-based sorting

### Subscription System
- Multiple package tiers
- Time-based pricing flexibility
- Subscription activation and expiration tracking
- Package upgrade/downgrade capability

### Analytics and Statistics
- User activity tracking
- Audio playback metrics
- Listening duration analysis
- Category popularity statistics
- User engagement metrics

### Event-Driven Communication
- JMS topics for inter-subsystem events
- Asynchronous processing
- Loose coupling between components
- Scalable message handling

## REST API Endpoints

The platform exposes RESTful endpoints for:
- User CRUD operations
- Audio recording management
- Category assignment
- Package subscription
- Statistics retrieval
- Search and filtering

## Message Queue Topics

JMS topics facilitate communication:
- User creation events
- Audio upload notifications
- Subscription changes
- Playback tracking events

## Project Structure

```
IS1 - PREDAJA/
├── baze/
│   ├── baza.txt          # Database schema DDL
│   └── podaci.txt        # Sample data DML
├── java aplikacije/
│   ├── centralni_server.zip
│   ├── podsistem1.zip
│   ├── podsistem2.zip
│   ├── podsistem3.zip
│   ├── klijentska_aplikacija.zip
│   └── libs/             # Shared libraries
└── uml/
    ├── klase/            # Class diagrams
    └── sekvenca/         # Sequence diagrams
```

## Use Cases

### User Workflow
1. User registers with demographic information
2. User subscribes to a package tier
3. User uploads audio recordings
4. User assigns categories to recordings
5. User browses and plays audio content
6. System tracks playback statistics

### Admin Workflow
1. Manage subscription packages
2. Set pricing for different periods
3. View platform-wide statistics
4. Monitor user activity
5. Analyze category popularity

## Business Logic

### Subscription Validation
- Active subscriptions enable audio playback
- Expired subscriptions require renewal
- Package changes handled with grace periods

### Pricing Strategy
- Time-based pricing allows promotional periods
- Historical pricing preserved for reporting
- Package price lookup based on subscription start date

### Statistics Generation
- Real-time playback tracking
- Aggregated metrics calculation
- Category-based analytics
- User engagement scoring

## Development Notes

### JPA Relationships
- One-to-Many: Korisnik to AudioSnimak
- One-to-Many: Korisnik to Pretplata
- One-to-Many: Paket to CenaPaketa
- Many-to-Many: AudioSnimak to Kategorija

### JMS Message Flow
1. Subsystem 1/2 publishes entity creation events
2. Subsystem 3 consumes events for statistics
3. Central server coordinates message routing

### Error Handling
- Database constraint validation
- JMS message delivery guarantees
- REST API error responses
- Transaction rollback on failures
