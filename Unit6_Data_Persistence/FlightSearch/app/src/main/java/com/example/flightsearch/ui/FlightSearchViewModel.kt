package com.example.flightsearch.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.flightsearch.FlightSearchApplication
import com.example.flightsearch.data.Airport
import com.example.flightsearch.data.FlightRepository
import com.example.flightsearch.data.UserPreferencesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * A flight from [departure] to [destination].
 */
data class FlightRoute(
    val departure: Airport,
    val destination: Airport,
    val isFavorite: Boolean
)

/**
 * What the screen shows below the search field.
 */
sealed interface SearchContent {
    /** Search field is empty: show the saved favorite routes. */
    data class Favorites(val routes: List<FlightRoute>) : SearchContent

    /** User is typing: show airports that match the text (autocomplete). */
    data class Suggestions(val airports: List<Airport>) : SearchContent

    /** User picked an airport: show every flight that departs from it. */
    data class Flights(val departure: Airport, val routes: List<FlightRoute>) : SearchContent

    data object Loading : SearchContent
}

@OptIn(ExperimentalCoroutinesApi::class)
class FlightSearchViewModel(
    private val flightRepository: FlightRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    /** IATA code of the selected departure airport, or "" when none is selected. */
    private val selectedAirport = MutableStateFlow("")

    init {
        // Restore the last search from DataStore.
        viewModelScope.launch {
            _query.value = userPreferencesRepository.searchQuery.first()
            selectedAirport.value = userPreferencesRepository.selectedAirport.first()
        }
    }

    private val favoriteCodes: Flow<Set<Pair<String, String>>> =
        flightRepository.getFavorites().map { favorites ->
            favorites.map { it.departureCode to it.destinationCode }.toSet()
        }

    private val favoriteRoutes: Flow<List<FlightRoute>> = combine(
        flightRepository.getAllAirports(),
        flightRepository.getFavorites()
    ) { airports, favorites ->
        val byCode = airports.associateBy { it.iataCode }
        favorites.mapNotNull { favorite ->
            val departure = byCode[favorite.departureCode]
            val destination = byCode[favorite.destinationCode]
            if (departure != null && destination != null) {
                FlightRoute(departure, destination, isFavorite = true)
            } else {
                null
            }
        }
    }

    val content: StateFlow<SearchContent> = combine(_query, selectedAirport) { query, selected ->
        query.trim() to selected
    }.flatMapLatest { (query, selected) ->
        when {
            selected.isNotEmpty() -> flightsFrom(selected)
            query.isEmpty() -> favoriteRoutes.map { SearchContent.Favorites(it) }
            else -> flightRepository.searchAirports(query).map { SearchContent.Suggestions(it) }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = SearchContent.Loading
    )

    private fun flightsFrom(code: String): Flow<SearchContent> = combine(
        flightRepository.getAirport(code),
        flightRepository.getDestinations(code),
        favoriteCodes
    ) { departure, destinations, favorites ->
        if (departure == null) {
            SearchContent.Suggestions(emptyList())
        } else {
            SearchContent.Flights(
                departure = departure,
                routes = destinations.map { destination ->
                    FlightRoute(
                        departure = departure,
                        destination = destination,
                        isFavorite = (departure.iataCode to destination.iataCode) in favorites
                    )
                }
            )
        }
    }

    fun onQueryChange(newQuery: String) {
        _query.value = newQuery
        selectedAirport.value = ""
        saveSearch()
    }

    fun onAirportSelected(airport: Airport) {
        _query.value = airport.iataCode
        selectedAirport.value = airport.iataCode
        saveSearch()
    }

    fun clearSearch() = onQueryChange("")

    fun toggleFavorite(route: FlightRoute) {
        viewModelScope.launch {
            val from = route.departure.iataCode
            val to = route.destination.iataCode
            if (route.isFavorite) {
                flightRepository.removeFavorite(from, to)
            } else {
                flightRepository.addFavorite(from, to)
            }
        }
    }

    private fun saveSearch() {
        val query = _query.value
        val selected = selectedAirport.value
        viewModelScope.launch {
            userPreferencesRepository.saveSearch(query, selected)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as FlightSearchApplication
                FlightSearchViewModel(
                    application.container.flightRepository,
                    application.container.userPreferencesRepository
                )
            }
        }
    }
}
