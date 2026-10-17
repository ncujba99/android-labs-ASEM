Flight Search App
=================

Solution for the "Project: Create a Flight Search app" in Unit 6 of
Android Basics with Compose.

Features
--------
* Prepopulated Room database (`app/src/main/assets/database/flight_search.db`)
  with an `airport` table and a `favorite` table.
* Autocomplete: typing in the search field lists airports whose IATA code or
  name matches the text, ordered by passenger count.
* Selecting an airport lists every flight from it to all other airports.
* Tapping the star saves or removes a route in the `favorite` table.
* With an empty search field, the saved favorite routes are shown.
* The search text is saved with Preferences DataStore and restored when the
  app is reopened.

Project structure
-----------------
* `data/` - `Airport` and `Favorite` entities, `FlightDao`, `FlightSearchDatabase`,
  `FlightRepository`, `UserPreferencesRepository` (DataStore) and `AppContainer`.
* `ui/FlightSearchViewModel.kt` - combines the search text, selected airport and
  favorites into the screen content.
* `ui/FlightSearchScreen.kt` - Compose UI (search field, suggestions, flight cards).

Build
-----
Open the `FlightSearch` folder in Android Studio and run the `app` configuration,
or run `./gradlew assembleDebug`.
