# 🎵 MusicApp

A modern, high-performance local music player built for Android using the latest development practices. This app provides a seamless and immersive audio experience with a focus on speed, design, and usability.

![App Screenshot](photo_1_2026-07-19_18-52-37)

## ✨ Features

- **Offline Music Library**: Automatically scans and organizes your local audio files from your device.
- **Smart Categorization**: Easily browse your music by Songs, Albums, and Folders for quick access.
- **Media3 ExoPlayer Integration**: Utilizes the latest Google Media3 library for high-quality audio playback and industry-standard media session support.
- **Background Playback**: Keep the music going even when the app is in the background or the screen is locked, with full notification controls.
- **Real-time Search**: Powerful search functionality with live suggestions to help you find your favorite tracks instantly.
- **Favorites System**: Save your preferred tracks to a dedicated "Favorites" list, persisted using a Room Database.
- **Interactive Waveform Visualizer**: A dynamic and beautiful waveform bar that represents the music progress and allows for intuitive seeking.
- **Modern Material 3 UI**: A stunning user interface built entirely with Jetpack Compose, featuring:
  - *Dynamic Color Support*: Adapts the app's theme to match your system wallpaper.
  - *Glassmorphic Design*: Blurred backgrounds and transparent layers for a premium look.
  - *Smooth Animations*: Fluid transitions between screens.
- **Edge-to-Edge Experience**: Fully immersive layout that utilizes the entire screen real estate.
- **Optimized Performance**: Handles configuration changes (like screen rotation) seamlessly without interrupting playback or losing data.

## 🛠️ Technical Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose (100% Declarative UI)
- **Audio Engine**: Android Media3 (ExoPlayer & MediaSession)
- **Database**: Room Persistence Library (for Favorites)
- **Architecture**: MVVM (Model-View-ViewModel)
- **Concurrency**: Kotlin Coroutines & Flow
- **Image Loading**: Coil (for high-performance album art rendering)
- **Navigation**: Modern Component-based Activity & Compose integration
- **Dependency Injection**: Factory pattern for ViewModel management

## 📸 Core Components

- **Splash Screen**: A welcoming entry point that sets the tone for the app.
- **Main Screen**: The central hub with a drawer menu, search bar, and tabbed navigation for your library.
- **Player Screen**: A feature-rich playback interface with album art blur effects, playback controls (Shuffle, Repeat, Skip), and waveform seeking.
- **Music Service**: A robust foreground service ensuring stable playback across the OS.

## 🚀 Getting Started

1. Clone the repository:
   ```bash
   git clone [https://github.com/x0t68/MusicApp.git](https://github.com/x0t68/MusicApp.git)
