package com.example.flightsearch.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flightsearch.R
import com.example.flightsearch.data.Airport
import com.example.flightsearch.ui.theme.FlightSearchTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlightSearchApp(
    viewModel: FlightSearchViewModel = viewModel(factory = FlightSearchViewModel.Factory)
) {
    val query by viewModel.query.collectAsStateWithLifecycle()
    val content by viewModel.content.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text(stringResource(R.string.app_name)) })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            SearchField(
                query = query,
                onQueryChange = viewModel::onQueryChange,
                onClear = viewModel::clearSearch,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
            when (val current = content) {
                SearchContent.Loading -> Unit
                is SearchContent.Suggestions -> SuggestionList(
                    airports = current.airports,
                    onAirportClick = {
                        focusManager.clearFocus()
                        viewModel.onAirportSelected(it)
                    }
                )
                is SearchContent.Flights -> RouteList(
                    title = stringResource(R.string.flights_from, current.departure.iataCode),
                    routes = current.routes,
                    emptyText = "",
                    onFavoriteClick = viewModel::toggleFavorite
                )
                is SearchContent.Favorites -> RouteList(
                    title = stringResource(R.string.favorite_routes),
                    routes = current.routes,
                    emptyText = stringResource(R.string.no_favorites),
                    onFavoriteClick = viewModel::toggleFavorite
                )
            }
        }
    }
}

@Composable
fun SearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier,
        singleLine = true,
        placeholder = { Text(stringResource(R.string.search_hint)) },
        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = onClear) {
                    Icon(Icons.Filled.Clear, contentDescription = stringResource(R.string.clear_search))
                }
            }
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(
            onSearch = { focusManager.clearFocus() }
        ),
        shape = MaterialTheme.shapes.extraLarge
    )
}

@Composable
fun SuggestionList(
    airports: List<Airport>,
    onAirportClick: (Airport) -> Unit,
    modifier: Modifier = Modifier
) {
    if (airports.isEmpty()) {
        EmptyText(stringResource(R.string.no_results), modifier)
        return
    }
    LazyColumn(modifier = modifier) {
        items(airports, key = { it.id }) { airport ->
            AirportText(
                airport = airport,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onAirportClick(airport) }
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            )
        }
    }
}

@Composable
fun RouteList(
    title: String,
    routes: List<FlightRoute>,
    emptyText: String,
    onFavoriteClick: (FlightRoute) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
        if (routes.isEmpty() && emptyText.isNotEmpty()) {
            item { EmptyText(emptyText) }
        }
        items(routes, key = { "${it.departure.iataCode}-${it.destination.iataCode}" }) { route ->
            RouteCard(route = route, onFavoriteClick = { onFavoriteClick(route) })
        }
    }
}

@Composable
fun RouteCard(
    route: FlightRoute,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(start = 16.dp, top = 12.dp, bottom = 12.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.depart),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                AirportText(route.departure)
                Spacer(Modifier.padding(top = 8.dp))
                Text(
                    text = stringResource(R.string.arrive),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                AirportText(route.destination)
            }
            IconButton(onClick = onFavoriteClick) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = stringResource(
                        if (route.isFavorite) R.string.remove_favorite else R.string.add_favorite
                    ),
                    tint = if (route.isFavorite) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
            }
        }
    }
}

@Composable
fun AirportText(airport: Airport, modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
        Text(
            text = airport.iataCode,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.width(8.dp))
        Text(text = airport.name, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
private fun EmptyText(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        modifier = modifier.padding(16.dp),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Preview(showBackground = true)
@Composable
fun RouteCardPreview() {
    FlightSearchTheme {
        RouteCard(
            route = FlightRoute(
                departure = Airport(1, "KIV", "Chișinău Eugen Doga International Airport", 5000000),
                destination = Airport(2, "LHR", "London Heathrow Airport", 79200000),
                isFavorite = true
            ),
            onFavoriteClick = {}
        )
    }
}
