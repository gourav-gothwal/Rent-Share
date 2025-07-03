# Rent-Share

A Kotlin-based project for managing shared rentals.

[![Build Status](https://img.shields.io/badge/build-passing-brightgreen)](https://github.com/gourav-gothwal/Rent-Share/actions/workflows/build.yml)
[![License](https://img.shields.io/badge/license-MIT-blue)](https://opensource.org/licenses/MIT)
[![Kotlin Version](https://img.shields.io/badge/kotlin-1.9.0-blueviolet)](https://kotlinlang.org/)

## Table of Contents

- [Introduction](#introduction)
- [Features](#features)
- [Project Structure](#project-structure)
- [Dependencies](#dependencies)
- [Installation](#installation)
- [Usage](#usage)
- [Contributing](#contributing)
- [License](#license)

## Introduction

Rent-Share is a project developed in Kotlin to help manage shared rental expenses. This application aims to simplify the process of tracking expenses, managing payments, and coordinating tasks among roommates. This README provides information on how to set up, use, and contribute to the project.

## Features

-   **Expense Tracking**: Easily record and categorize shared expenses.
-   **Payment Management**: Track who owes whom and manage payment settlements.
-   **User Authentication**: Secure user accounts with login and registration.
-   **Notifications**: Receive reminders for payments and tasks.

## Project Structure

The project follows a modular architecture:

```
Rent-Share/
├── app/               
├── adapters/               
├── fragments/             
├── model/      
├── activities/                
├── build.gradle.kts    
└── ...
```

## Dependencies

Key dependencies used in this project:

-   Kotlin Coroutines: For asynchronous programming.
-   Android Jetpack Components:
    -   ViewModel: For managing UI-related data in a lifecycle-conscious way.
    -   LiveData: For building data objects that notify views when the underlying data changes.
    -   Navigation: For handling in-app navigation.
-   Firebase: For authentication of users.
-   Firestore Database: For database management.
-   Glide: For image loading and caching.

## Installation

1.  Clone the repository:

    ```bash
    git clone https://github.com/gourav-gothwal/Rent-Share.git
    ```

2.  Open the project in Android Studio.

3.  Ensure you have the latest Android SDK and build tools installed.

4.  Build the project using Gradle:

    ```bash
    ./gradlew build
    ```

## Usage

1.  Run the application on an Android emulator or a physical device.

2.  Register a new account or log in with existing credentials.

3.  Navigate through the app to:

    -   Add and track expenses.
    -   Manage payments and settlements.
    -   Assign and track household tasks.

## Contributing

Contributions are welcome! Here's how you can contribute:

1.  Fork the repository.

2.  Create a new branch for your feature or bug fix:

    ```bash
    git checkout -b feature/your-feature-name
    ```

3.  Make your changes and commit them with descriptive messages:

    ```bash
    git commit -m "Add: Description of your changes"
    ```

4.  Push your changes to your forked repository:

    ```bash
    git push origin feature/your-feature-name
    ```

5.  Submit a pull request to the main repository.

### Contribution Guidelines

-   Follow the project's coding style and conventions.
-   Write clear and concise commit messages.
-   Ensure your code is well-tested.
-   Provide documentation for new features.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
