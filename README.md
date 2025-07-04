# Daily Do - Productivity & Task Management App

<img src="app/src/main/res/mipmap-xxxhdpi/ic_launcher_round.png" width="100" alt="Daily Do Logo">

## Overview

Daily Do is a sleek, minimalist productivity app designed to help users track daily tasks, monitor
efficiency, and stay motivated through inspirational stories. With its clean black and white
interface and robust feature set, Daily Do makes it easy to maintain focus on what matters most.

## Features

### Task Management

- Create, edit and track daily tasks
- Set percentage-based progress for nuanced task completion
- Prioritize tasks to focus on what's most important
- Visual progress indicators with custom battery-like design

### Efficiency Reports

- View detailed efficiency metrics (daily, weekly, monthly, yearly)
- Interactive charts to track performance trends
- Actionable statistics to improve productivity
- Smart efficiency calculation based on task completion rates

### Motivational Stories

- Browse and read inspirational content
- Add your own motivational stories
- Categorized story library for easy discovery
- Share stories with others

### Personalization

- Light/dark theme with smooth transitions
- Custom notification preferences
- User profile settings
- App behavior customization

## Screenshots

*(Screenshots would be added here)*

## Technical Details

### Architecture

Daily Do follows Clean Architecture principles with MVVM pattern:

- **UI Layer**: Jetpack Compose UI components and ViewModels
- **Domain Layer**: Business logic and use cases
- **Data Layer**: Repositories and data sources

### Key Technologies

- **UI**: [Jetpack Compose](https://developer.android.com/jetpack/compose) with Material Design 3
- **Navigation
  **: [Jetpack Navigation for Compose](https://developer.android.com/jetpack/compose/navigation)
  with type-safe routing
- **Local Database**: [Room](https://developer.android.com/training/data-storage/room) for
  persistent storage
- **Dependency Injection
  **: [Hilt](https://developer.android.com/training/dependency-injection/hilt-android)
- **Concurrency**: Kotlin Coroutines and Flow
- **Charts**: [MPAndroidChart](https://github.com/PhilJay/MPAndroidChart) for performance
  visualization
- **Date/Time**: ThreeTenABP for improved date handling
- **Animations**: Compose animation system for fluid transitions

### Project Structure

```
app/
├── src/main/java/com/sultonuzdev/dailydo/
│   ├── ui/
│   │   ├── components/           # Reusable UI components
│   │   ├── screens/              # App screens
│   │   │   ├── tasks/           # Task-related screens
│   │   │   ├── reports/         # Statistics screens
│   │   │   ├── stories/         # Story screens
│   │   │   └── settings/        # Settings screens
│   │   ├── theme/               # Theme configuration
│   │   └── navigation/          # Navigation setup
│   ├── data/
│   │   ├── repository/          # Data repositories
│   │   ├── local/               # Local database
│   │   └── remote/              # Firebase integration
│   ├── domain/
│   │   ├── model/               # Data models
│   │   ├── usecase/             # Business logic
│   │   └── repository/          # Repository interfaces
│   ├── di/                      # Dependency injection
│   └── utils/                   # Utility functions
```

## Getting Started

### Prerequisites

- Android Studio Arctic Fox or later
- Kotlin 2.0.0 or later
- Android 6.0+ (API level 24+)

### Installation

1. Clone this repository

```bash
git clone https://github.com/sultonuzdev/daily-do.git
```

2. Open the project in Android Studio

3. Sync project with Gradle files

4. Run the app on an emulator or physical device

## Efficiency Calculation Logic

### Daily Efficiency

```
Daily Efficiency = (Sum of all tasks' done percentages) / (Sum of all tasks' given percentages) * 100
```

### Weekly Efficiency

```
Weekly Efficiency = (Sum of daily efficiencies for the week) / 7
```

### Monthly Efficiency

```
Monthly Efficiency = (Sum of daily efficiencies for the month) / (Number of days in month)
```

### Yearly Efficiency

```
Yearly Efficiency = (Sum of monthly efficiencies) / 12
```

## Design Principles

- **Minimalist Design**: Clean black and white color scheme
- **Modern Interface**: Material Design 3 components
- **Visual Hierarchy**: 60/30/10 color rule (60% background, 30% content, 10% accent)
- **Fluid Animations**: Smooth transitions and micro-interactions
- **Accessibility**: High contrast and clear typography

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgments

- [Material Design](https://material.io/design) for design inspiration
- All open-source libraries that made this project possible

---

Developed with ❤️ by [SultonuzDev](https://github.com/sultonuzdev)