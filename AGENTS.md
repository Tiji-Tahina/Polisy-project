# AGENTS.md

## Project Overview

Single-module Android app (Kotlin + Jetpack Compose). Package: `com.example.polisy_project`.

- **minSdk**: 24, **targetSdk**: 36, **compileSdk**: 36
- **Gradle**: 9.4.1, **AGP**: 9.2.1, **Kotlin**: 2.2.10
- **Compose BOM**: 2026.02.01 (Material 3)
- Java 11 source/target compatibility
- Configuration cache enabled (`gradle.properties`)
- Dynamic color theming on Android 12+ (see `Theme.kt`)

## Build & Run

```bash
# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Run unit tests
./gradlew test

# Run instrumented tests (requires device/emulator)
./gradlew connectedAndroidTest

# Clean build
./gradlew clean assembleDebug
```

**Note**: First build may take several minutes to download dependencies. No CI pipelines configured.

## Project Structure

```
app/
├── src/main/java/com/example/polisy_project/
│   ├── MainActivity.kt          # Single activity, entry point
│   └── ui/theme/                # Compose theme (Color, Theme, Type)
├── src/test/                    # Unit tests (JUnit 4)
└── src/androidTest/             # Instrumented tests (Espresso + Compose testing)
```

**Single-activity architecture**: All UI is in `MainActivity.kt` using Compose.

## Key Conventions

- Uses Gradle version catalog (`gradle/libs.versions.toml`) for dependency management
- All dependencies referenced via `libs.*` aliases (e.g., `libs.androidx.compose.material3`)
- No custom Gradle plugins or build logic
- No ProGuard/R8 (release optimization disabled)
- Edge-to-edge display enabled by default

## Adding Dependencies

Edit `gradle/libs.versions.toml` to add versions, libraries, and plugins. Reference them in `app/build.gradle.kts` via `libs.*` aliases.

## Testing

- **Unit tests**: `app/src/test/` - Run with `./gradlew test`
- **Instrumented tests**: `app/src/androidTest/` - Run with `./gradlew connectedAndroidTest`
- Test runner: `androidx.test.runner.AndroidJUnitRunner`
- Compose testing: `androidx.compose.ui.test.junit4`

## Common Pitfalls

- No `local.properties` in version control (contains SDK path)
- Ensure `ANDROID_HOME` or `ANDROID_SDK_ROOT` is set
- Gradle wrapper (`gradlew`) must be executable: `chmod +x gradlew` if needed

# CODING AGENT PROTOCOL

1. Micro‑Step Focus
   - Work only on one atomic unit at a time (file, class, method, parameter).
   - Never bundle tasks unless explicitly told to “increase speed.”

2. Explain → Pause
   - Before any action, state in one sentence what you plan to do.
   - Stop and wait for explicit user approval before proceeding.

3. Ultra‑Concise Output
   - Use minimal words, short code blocks, and terse explanations.
   - No filler, no repetition, no commentary beyond the step.

4. Iterative Loop
   - After each step, propose exactly 2–3 next micro‑steps.
   - User selects the direction; agent executes only that.

5. Adaptive Velocity
   - Default: micro‑steps.
   - If user requests “increase speed” or “take bigger steps,” gradually expand scope (e.g., multiple methods, whole class).
   - Otherwise, remain in strict micro‑step mode.

