# HarryPotterApp

HarryPotterApp is an Android application that provides comprehensive information about characters
from the Harry Potter universe. The app leverages modern Android development practices and follows
the recommended Clean Architecture principles with distinct **Domain**, **UI**, and **Data** layers,
ensuring a scalable, maintainable, and testable codebase.

## Features

- **Character List Display**: Automatically loads and displays a list of Harry Potter characters
  upon app launch.
- **Search Functionality**: Allows users to search for characters by name or actor, with seamless
  support for both online and offline modes.
- **Character Details**: Provides detailed information about each character, including their house,
  species, date of birth, and more.
- **Adaptive UI**: Supports dynamic font sizes and themes, including dark mode, to enhance
  accessibility and user experience across various devices.
- **Offline Support**: Caches character data locally, enabling users to access information even
  without an internet connection.
- **Performance Optimizations**: Employs efficient data loading and state management techniques to
  ensure smooth and responsive user interactions.

## Architecture

HarryPotterApp adheres to the **Clean Architecture** paradigm, dividing the project into three main
layers:

- **Learn More**: For more information, refer to the
  official [Android documentation](https://developer.android.com/topic/architecture/domain-layer?authuser=1).

### 1. Domain Layer

- **Responsibilities**:
    - Contains the core business logic and application rules.
    - Defines use cases that encapsulate specific operations (e.g., fetching characters, searching).
    - Interfaces for repositories are declared here, abstracting the data sources.

- **Benefits**:
    - Promotes reusability and testability of business logic.
    - Decouples the core logic from implementation details.

### 2. UI Layer

- **Responsibilities**:
    - Manages the presentation of data and handles user interactions.
    - Built using **Jetpack Compose** for a declarative and efficient UI development experience.
    - Utilizes **ViewModels** to maintain UI-related data and handle state changes.

- **Benefits**:
    - Provides a responsive and interactive user interface.
    - Ensures a clear separation between UI and business logic.

### 3. Data Layer

- **Responsibilities**:
    - Manages data operations, including fetching from remote APIs and local databases.
    - Implements repositories defined in the domain layer.
    - Handles data caching and persistence strategies.

- **Benefits**:
    - Ensures efficient and reliable data retrieval and storage.
    - Abstracts data sources, allowing for easy integration and replacement.

## Tech Stack

### Core Technologies

- **Kotlin**: Utilized for all development, offering concise and expressive code.
- **Jetpack Compose**: Modern toolkit for building native Android UI with declarative components.
- **Material Design 3**: Provides a cohesive and intuitive user interface adhering to Google's
  design guidelines.

### Networking

- **Retrofit**: Simplifies HTTP API communication.
- **OkHttp**: Efficient HTTP client supporting HTTP/2 and web sockets.
- **Gson**: Handles JSON parsing and serialization.

### Dependency Injection

- **Dagger-Hilt**: Simplifies dependency injection, promoting modularity and ease of testing.

### Local Storage

- **Room**: Provides an abstraction layer over SQLite for robust local data persistence.

### Asynchronous Programming

- **Kotlin Coroutines**: Facilitates easy and efficient asynchronous operations.

### Testing

- **JUnit**: Framework for writing and running unit tests.
- **Mockk**: Kotlin mocking library for creating mocks and stubs in tests.
- **Coroutine Test**: Tools for testing coroutine-based code.

  Run unit tests with the following command:

  ```bash
  ./gradlew testDebugUnitTest