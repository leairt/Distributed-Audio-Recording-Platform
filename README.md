# Distributed Audio-Recording Platform

A enterprise-grade distributed audio recording and streaming platform built with Java EE technologies. The system implements a microservices architecture with multiple subsystems communicating through JMS (Java Message Service) and exposing RESTful APIs for seamless integration.

## Overview

This platform enables users to upload, categorize, and manage audio recordings while supporting a subscription-based model with different package tiers. The system provides comprehensive analytics, tracks user activity, audio playback statistics, and manages dynamic pricing across different time periods.

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

| Technology | Purpose | Version |
|------------|---------|---------|
| **Java EE** | Core application framework | 11 |
| **JPA** | Object-relational mapping and database operations | 2.2 |
| **JMS** | Asynchronous inter-subsystem communication | 2.0 |
| **JAX-RS** | RESTful web services | 2.1 |
| **MySQL** | Relational database management | 8.0+ |
| **Maven** | Dependency management and build automation | 3.6+ |

The project runs on **GlassFish 5**, used as the Java EE application server.

## Features

### Core Functionality
- User registration and authentication
- Audio file upload and management  
- Category-based content organization
- Subscription package management
- Real-time playback tracking
- Comprehensive analytics dashboard

### Technical Features
- Distributed microservices architecture
- Asynchronous message processing
- RESTful API with JSON responses
- Database transaction management
- Event-driven communication
- Scalable horizontal architecture



## API Documentation

### REST Endpoints

#### User Management
```http
GET    /api/users              # List all users
POST   /api/users              # Create new user
GET    /api/users/{id}          # Get user by ID
PUT    /api/users/{id}          # Update user
DELETE /api/users/{id}          # Delete user
```

#### Audio Management
```http
GET    /api/audio              # List audio recordings
POST   /api/audio              # Upload new recording
GET    /api/audio/{id}          # Get recording details
PUT    /api/audio/{id}          # Update recording metadata
DELETE /api/audio/{id}          # Delete recording
```

#### Package Management
```http
GET    /api/packages           # List subscription packages
POST   /api/subscriptions      # Subscribe to package
GET    /api/subscriptions/{id} # Get subscription details
```

#### Statistics
```http
GET    /api/stats/users        # User statistics
GET    /api/stats/audio        # Audio playback statistics
GET    /api/stats/categories   # Category popularity
```

## Database Schema

The platform uses a normalized MySQL database schema with the following key entities:

### Core Tables

**Mesto (Location)**
```sql
CREATE TABLE Mesto (
    Naziv VARCHAR(100) PRIMARY KEY
);
```

**Korisnik (User)**
```sql
CREATE TABLE Korisnik (
    Id INT AUTO_INCREMENT PRIMARY KEY,
    Ime VARCHAR(100) UNIQUE NOT NULL,
    Email VARCHAR(100) UNIQUE NOT NULL,
    Godiste INT NOT NULL,
    Pol ENUM('M','Z') NOT NULL,
    Mesto VARCHAR(100) NOT NULL,
    FOREIGN KEY (Mesto) REFERENCES Mesto(Naziv)
);
```

**AudioSnimak (Audio Recording)**
```sql
CREATE TABLE AudioSnimak (
    Id INT AUTO_INCREMENT PRIMARY KEY,
    Naziv VARCHAR(200) NOT NULL,
    Trajanje INT NOT NULL,
    IdVlasnik INT NOT NULL,
    Postavljeno DATETIME NOT NULL,
    FOREIGN KEY (IdVlasnik) REFERENCES Korisnik(Id)
);
```

For complete schema details, see `baze/baza.txt`.

## Usage

### For End Users

1. **Registration**: Create an account with personal information
2. **Subscription**: Choose and subscribe to a package tier
3. **Upload**: Upload audio files with metadata and categories
4. **Browse**: Discover and play audio content
5. **Analytics**: View personal listening statistics

### For Administrators

1. **Package Management**: Create and modify subscription packages
2. **Pricing**: Set time-based pricing strategies
3. **Monitoring**: Track platform usage and user engagement
4. **Analytics**: Generate comprehensive reports

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
