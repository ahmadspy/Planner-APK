# PlanGym

PlanGym is a comprehensive Android application designed to help users manage their daily tasks, build lasting habits, and track their workout routines. Built with modern Android development tools and best practices.

## Features

- **Planner**: Manage projects and tasks with priorities and subtasks.
- **Habits**: Track daily habits with streak counters and daily logs.
- **Workout**: Create workout routines, log exercises, and track sets/reps/weights. Includes a built-in workout timer.
- **Home Dashboard**: Get inspired with daily motivational quotes and a quick overview of your daily progress.
- **Reminders**: Stay on track with automated reminders for tasks and habits.

## Tech Stack

- **Kotlin**: Primary programming language.
- **Jetpack Compose**: Modern UI toolkit for building declarative UIs.
- **Room Database**: Local data storage with reactive flows.
- **Hilt**: Dependency injection for a clean and testable architecture.
- **WorkManager**: Background task scheduling for reminders.
- **ViewModel & StateFlow**: Reactive state management following MVI/MVVM patterns.
- **Material 3**: Modern design system for a beautiful and accessible UI.

## Architecture

The project follows the **Clean Architecture** principles and **SOLID** patterns:
- **UI Layer**: Composable screens and ViewModels.
- **Domain Layer**: Repository interfaces and entities (business logic).
- **Data Layer**: Room DAOs, Database implementation, and Repository implementations.

## Getting Started

1. Clone the repository.
2. Open in Android Studio (Ladybug or newer recommended).
3. Sync Project with Gradle Files.
4. Run on an emulator or physical device (API 24+).

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
