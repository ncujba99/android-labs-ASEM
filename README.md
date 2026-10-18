# Android Labs — ASEM

Android lab projects built with Kotlin and Jetpack Compose.

- **Units 1–8** are the codelab projects from Google's [Android Basics with Compose](https://developer.android.com/courses/android-basics-compose/course) course, based on the official solutions from [google-developer-training](https://github.com/google-developer-training).
- **Unit 9** holds custom labs that are not part of the course.

Each folder under `UnitN_*/` is a standalone Android Studio project with its own Gradle wrapper.

## Requirements

- Android Studio (Ladybug or newer), or JDK 17+ for command-line builds
- Android SDK Platform 35
- An emulator or a device running Android 7.0 (API 24) or newer

## Build and run

Open one project folder (for example `Unit2_Building_App_UI/DiceRoller`) in Android Studio, not the repository root, and press **Run**.

From the command line:

```bash
cd Unit9_Custom_Labs/BuildTheHouse
./gradlew assembleDebug          # APK in app/build/outputs/apk/debug/
./gradlew installDebug           # install on a running emulator/device
./gradlew testDebugUnitTest      # run unit tests, where a project has them
```

If Gradle cannot find the SDK, create `local.properties` in the project folder with `sdk.dir=/path/to/Android/sdk`. Git ignores this file.

## Projects

### Unit 1: Your first Android app

| Project | Upstream repository |
|---|---|
| [BirthdayCard](Unit1_Your_First_Android_App/BirthdayCard) | [birthday-card-app](https://github.com/google-developer-training/basic-android-kotlin-compose-birthday-card-app) |

The Unit 1 practice tasks (Task Manager, Compose Article, Business Card) start from an empty activity and have no official repository.

### Unit 2: Building app UI

| Project | Upstream repository |
|---|---|
| [TipTime](Unit2_Building_App_UI/TipTime) | [training-tip-calculator](https://github.com/google-developer-training/basic-android-kotlin-compose-training-tip-calculator) |
| [Lemonade](Unit2_Building_App_UI/Lemonade) | [training-lemonade](https://github.com/google-developer-training/basic-android-kotlin-compose-training-lemonade) |
| [DiceRoller](Unit2_Building_App_UI/DiceRoller) | [training-dice-roller](https://github.com/google-developer-training/basic-android-kotlin-compose-training-dice-roller) |

### Unit 3: Display lists and use Material Design

| Project | Upstream repository |
|---|---|
| [Affirmations](Unit3_Lists_and_Material_Design/Affirmations) | [training-affirmations](https://github.com/google-developer-training/basic-android-kotlin-compose-training-affirmations) |
| [Courses](Unit3_Lists_and_Material_Design/Courses) | [training-courses](https://github.com/google-developer-training/basic-android-kotlin-compose-training-courses) |
| [Superheroes](Unit3_Lists_and_Material_Design/Superheroes) | [training-superheroes](https://github.com/google-developer-training/basic-android-kotlin-compose-training-superheroes) |

### Unit 4: Navigation and app architecture

| Project | Upstream repository |
|---|---|
| [DessertClicker](Unit4_Navigation_and_Architecture/DessertClicker) | [training-dessert-clicker](https://github.com/google-developer-training/basic-android-kotlin-compose-training-dessert-clicker) |
| [Unscramble](Unit4_Navigation_and_Architecture/Unscramble) | [training-unscramble](https://github.com/google-developer-training/basic-android-kotlin-compose-training-unscramble) |
| [Cupcake](Unit4_Navigation_and_Architecture/Cupcake) | [training-cupcake](https://github.com/google-developer-training/basic-android-kotlin-compose-training-cupcake) |
| [Sports](Unit4_Navigation_and_Architecture/Sports) | [training-sports](https://github.com/google-developer-training/basic-android-kotlin-compose-training-sports) |

### Unit 5: Connect to the internet

| Project | Upstream repository |
|---|---|
| [MarsPhotos](Unit5_Connect_to_the_Internet/MarsPhotos) | [training-mars-photos](https://github.com/google-developer-training/basic-android-kotlin-compose-training-mars-photos) |
| [Amphibians](Unit5_Connect_to_the_Internet/Amphibians) | [training-amphibians](https://github.com/google-developer-training/basic-android-kotlin-compose-training-amphibians) |

### Unit 6: Data persistence

| Project | Upstream repository |
|---|---|
| [Inventory](Unit6_Data_Persistence/Inventory) | [training-inventory-app](https://github.com/google-developer-training/basic-android-kotlin-compose-training-inventory-app) |
| [DessertRelease](Unit6_Data_Persistence/DessertRelease) | [training-dessert-release](https://github.com/google-developer-training/basic-android-kotlin-compose-training-dessert-release) |
| [FlightSearch](Unit6_Data_Persistence/FlightSearch) | [training-flight-search](https://github.com/google-developer-training/basic-android-kotlin-compose-training-flight-search) |

### Units 7 & 8: WorkManager and Views

| Project | Upstream repository |
|---|---|
| [WaterMe](Unit7_8_WorkManager_and_Views/WaterMe) | [training-water-me](https://github.com/google-developer-training/basic-android-kotlin-compose-training-water-me) |
| [RaceTracker](Unit7_8_WorkManager_and_Views/RaceTracker) | [training-race-tracker](https://github.com/google-developer-training/basic-android-kotlin-compose-training-race-tracker) |
| [JuiceTracker](Unit7_8_WorkManager_and_Views/JuiceTracker) | [training-juice-tracker](https://github.com/google-developer-training/basic-android-kotlin-compose-training-juice-tracker) |

### Unit 9: Custom labs

These labs are not part of Android Basics with Compose.

| Project | Lab | Topics |
|---|---|---|
| [BuildTheHouse](Unit9_Custom_Labs/BuildTheHouse) | Lab 1, "Construiește casa" ([spec](Unit9_Custom_Labs/BuildTheHouse/lab-casa.pdf)) | State, state hoisting, `rememberSaveable` with a custom `Saver`, `Scaffold`, FAB, `ModalBottomSheet`, drawing with `Box` + `offset`/`align`, `GenericShape` |

In **BuildTheHouse**, the user builds a house in six ordered steps from a bottom sheet. Only the next step can be checked and only the last checked step can be unchecked. The house redraws from a single `List<BuildStep>` state, and only the number of completed steps is saved across rotation. The unit tests in `app/src/test` cover the order rule.

## Repository notes

- The root `.gitignore` excludes build output, `.gradle/`, `.idea/` and `local.properties` in every project.
- The Units 1–8 projects keep their upstream Apache 2.0 `LICENSE` files.
