# Umain Mobile WorkTest

## Project Overview

This mobile application for Munchies Restaurant Service allows users to browse restaurants, view details,
and filter restaurants by categories.
The app fetches data from the provided API to display restaurant information and filter options.

## Technologies Used

- Kotlin
- Jetpack Compose
- Retrofit for API calls
- Coil for image loading

## Project Structure

The project follows a simple MVVM architecture with clear separation between UI components,
ViewModels for state management, and Repositories for data operations.
The app uses Retrofit to communicate with the backend API.

## Features Implemented

- Restaurant list view showing all available restaurants
- Detail view displaying restaurant information and open/closed status
- Horizontal filter list with images fetched from the API
- Multi-select filtering functionality to filter restaurants by tags

## Setup Instructions

This project is available in a public repository for review.

1. Clone the repository:
   ```bash
   git clone https://github.com/paulinpetra/work-test-mobile.git
   cd work-test-mobile
2. Open the project in Android Studio
3. Run the application on an emulator or physical device

Minimum SDK: 24 (Android 7.0)  
Target SDK: 35 (Android 15)  
Compile SDK: 35
