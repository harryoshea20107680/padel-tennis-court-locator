# Padel Tennis Court Locator

An Android app built with **Kotlin** to record, browse, search, edit and delete padel tennis courts. Developed as a Mobile App Development project.

## Project Overview

The current app supports:

- **Add courts** with a name, address, optional website, indoor/outdoor setting, and number of courts (1–10).
- **View courts** in a scrollable RecyclerView with a saved court count.
- **Search** saved courts by name, with live filtering.
- **Edit** a court by tapping its row.
- **Delete** a court by long pressing its row, then confirming deletion.
- **Save data locally** in a JSON file that persists across app restarts.


## Architecture Diagram

[![Architecture diagram](https://gitdiagram.com/diagram-badge.svg)](https://gitdiagram.com/harryoshea20107680/padel-tennis-court-locator?utm_source=readme&utm_medium=badge)


## Project Structure

The following directory tree shows the key Kotlin source files, XML layouts and Gradle configuration files.

```text
padel-tennis-court-locator/
├── app/
│   ├── build.gradle.kts
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── java/org/setu/padelcourtlocator/
│       │   ├── MainApp.kt
│       │   ├── MainActivity.kt
│       │   ├── AddCourtActivity.kt
│       │   ├── ViewCourtsActivity.kt
│       │   ├── AppData.kt
│       │   ├── PadelCourtAdapter.kt
│       │   ├── OnCourtListener.kt
│       │   ├── helpers/
│       │   │   └── FileHelpers.kt
│       │   └── models/
│       │       ├── PadelCourt.kt
│       │       ├── PadelCourtStore.kt
│       │       ├── PadelCourtMemStore.kt
│       │       └── PadelCourtJSONStore.kt
│       └── res/layout/
│           ├── activity_main.xml
│           ├── activity_add_court.xml
│           ├── activity_view_courts.xml
│           └── item_padel_court.xml
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```
## Running the App

1. Clone this repository:

   ```bash
   git clone https://github.com/harryoshea20107680/padel-tennis-court-locator.git
   ```

2. Open the project in **Android Studio** and sync Gradle.

3. Start an emulator or connect an Android device running **Android 11 (API 30) or newer**.

4. Run the `app` configuration.

The app stores saved courts in local storage.
