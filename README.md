# MyRecipeBook 📱🍳

A modern Android application for discovering, managing, and organizing your favorite recipes with a clean, intuitive interface.

## Key Features

- **Recipe Discovery**: Browse through a vast collection of recipes from various cuisines
- **Detailed Recipe View**: Access comprehensive recipe information including ingredients, instructions, and nutritional data
- **Modern UI/UX**: Clean, Material Design-based interface with smooth navigation
- **Offline Support**: Cached data for better performance and offline access
- **Search & Filter**: Find recipes by name, ingredients, or cuisine type
- **Responsive Design**: Optimized for different screen sizes and orientations

## Architecture

Clean, testable, and scalable Android architecture:

- **MVVM Pattern**: Model-View-ViewModel architecture with clear separation of concerns
- **Repository Pattern**: Centralized data access layer with caching strategies  
- **Reactive Programming**: RxJava3 for asynchronous operations and data streams
- **Data Binding**: Two-way data binding for efficient UI updates
- **View Binding**: Type-safe view references eliminating findViewById calls
- **Lifecycle-Aware Components**: ViewModels and LiveData for lifecycle management

## Tech Stack

- **Language**: Kotlin 2.2.20
- **UI Framework**: Android Views with Material Design Components
- **Architecture**: MVVM + Repository Pattern
- **Networking**: Retrofit 3.0.0 + OkHttp 5.1.0
- **Reactive Programming**: RxJava3 + RxAndroid + RxKotlin
- **Serialization**: Kotlinx Serialization 1.9.0
- **Image Loading**: Coil 2.7.0
- **Lifecycle**: Android Architecture Components (ViewModel, LiveData)
- **UI Components**: RecyclerView, Fragments, ConstraintLayout
- **Testing**: JUnit, Espresso, MockWebServer

## Project Structure

```
app/src/main/java/com/example/myrecipebook/
├── common/          # Shared utilities and extensions
├── rest/           # Network layer (API services, models, repositories)
│   └── api/        # Retrofit API interfaces and DTOs
└── ui/             # Presentation layer (Activities, Fragments, ViewModels)
    └── main/       # Main activity and navigation
```

## Getting Started

### Prerequisites

- **Android Studio**: Narwhal (2025.1.3)
- **JDK**: 17 or higher
- **Android SDK**: API level 27+ (Android 8.1) minimum
- **Gradle**: Managed by wrapper (8.13.0)

### Clone and Build

1. **Clone the repository**
   ```bash
   git clone https://github.com/CassianoPresoto/MyRecipeBook.git
   cd MyRecipeBook
   ```

2. **Open in Android Studio**
   - Open Android Studio
   - Select "Open an existing project"
   - Navigate to the cloned directory

3. **Sync and Build**
   - Let Android Studio sync the Gradle files
   - Build the project using `Build > Make Project`

### Run the App

- **Debug Build**: Click the "Run" button in Android Studio or use `./gradlew installDebug`
- **Release Build**: Generate signed APK via `Build > Generate Signed Bundle/APK`

### Gradle Tasks

```bash
# Build debug APK
./gradlew assembleDebug

# Run unit tests
./gradlew test

# Run instrumented tests
./gradlew connectedAndroidTest

# Clean build
./gradlew clean build
```

## API Integration

The app integrates with [DummyJSON](https://dummyjson.com/) API for recipe data:

- **Base URL**: `https://dummyjson.com/`
- **Endpoints**: Recipes, search, categories
- **Response Format**: JSON with Kotlinx Serialization

## Testing

- **Unit Tests**: Domain logic and ViewModels testing with JUnit
- **Integration Tests**: Repository and API layer testing with MockWebServer
- **UI Tests**: User interface testing with Espresso
- **Test Coverage**: Focus on business logic and critical user flows

## Code Style & Quality

- **Kotlin Official Style Guide**: Consistent code formatting
- **Android Best Practices**: Following Android development guidelines
- **Modular Architecture**: Clear separation of concerns and dependencies
- **Reactive Patterns**: Proper RxJava usage and error handling

## Version Information

- **Current Version**: 2.0.0-dev
- **Min SDK**: 27 (Android 8.1)
- **Target SDK**: 36 (Android 14)
- **Compile SDK**: 36
---

**Built with ❤️ using modern Android development practices**
