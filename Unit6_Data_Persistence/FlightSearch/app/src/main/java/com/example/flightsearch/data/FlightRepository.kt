package com.example.flightsearch.data

import kotlinx.coroutines.flow.Flow

/**
 * Repository that provides access to airports and favorite routes.
 */
class FlightRepository(private val flightDao: FlightDao) {
    fun searchAirports(query: String): Flow<List<Airport>> = flightDao.searchAirports(query)

    fun getAllAirports(): Flow<List<Airport>> = flightDao.getAllAirports()

    fun getAirport(code: String): Flow<Airport?> = flightDao.getAirport(code)

    fun getDestinations(departureCode: String): Flow<List<Airport>> =
        flightDao.getDestinations(departureCode)

    fun getFavorites(): Flow<List<Favorite>> = flightDao.getFavorites()

    suspend fun addFavorite(departureCode: String, destinationCode: String) =
        flightDao.insertFavorite(Favorite(departureCode = departureCode, destinationCode = destinationCode))

    suspend fun removeFavorite(departureCode: String, destinationCode: String) =
        flightDao.deleteFavorite(departureCode, destinationCode)
}
