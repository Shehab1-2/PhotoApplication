# JavaFX Photo Application

## Overview

This photo application is a comprehensive solution designed using **JavaFX**, featuring the **MVC (Model-View-Controller)** design pattern. It offers a robust platform for users to store, move, log in, search, sign up, copy, and filter photos. With its **Singleton** design pattern, the application ensures efficient and consistent data storage and retrieval across different user sessions.

## Features

- **User Authentication**: Secure sign-up and log-in functionalities to manage user access.
- **Photo Management**: Users can easily store, move, and copy photos within the application.
- **Search and Filter**: Advanced search and filtering options to quickly find specific photos.
- **Data Storage**: Utilizes a **Singleton** design pattern for consistent and reliable data handling across different user sessions.
- **User-Friendly Interface**: Built with JavaFX, the application provides a seamless and intuitive user experience.

## Getting Started

### Prerequisites

Before running the project, ensure you have the following installed:

- **JavaFX SDK**
- **JDK 11 or later**

### Installation

1. **Clone the repository**:
    ```bash
    git clone https://github.com/yourusername/photo-application.git
    ```

2. Follow the instructions in the project to configure the JavaFX libraries with your IDE (e.g., IntelliJ, Eclipse) or build tool (e.g., Maven, Gradle).

### Usage

Once the application is set up:

1. Launch the application.
2. **Create an account** or **log in** to manage your photos.
3. You can perform the following operations:
   - **Upload**: Add new photos to the application.
   - **Move**: Organize your photos by moving them between folders.
   - **Copy**: Duplicate photos for backup or organizational purposes.
   - **Search**: Use advanced search functionality to quickly find photos.
   - **Filter**: Apply filters to your photo library for efficient navigation.

## Design Patterns

- **MVC Design Pattern**: The application is separated into three interconnected components:
  - **Model**: Manages the data and business logic.
  - **View**: Handles the presentation and user interface.
  - **Controller**: Acts as an intermediary between the Model and View, processing user input and updating the view accordingly.

- **Singleton Design Pattern**: Ensures that only one instance of the data storage class is created, allowing a global point of access to the data throughout the application. This ensures consistent data handling across different user sessions.

## Author

- **Sheab Ahamed**

## License

This project is licensed under the MIT License. See the [LICENSE.md](LICENSE.md) file for details.
