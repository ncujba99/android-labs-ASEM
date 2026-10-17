package com.example.flightsearch.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FlightDao {

    /**
     * Airports whose IATA code or name contains [query], most popular first.
     */
    @Query(
        """
        SELECT * FROM airport
        WHERE iata_code LIKE '%' || :query || '%' OR name LIKE '%' || :query || '%'
        ORDER BY passengers DESC
        """
    )
    fun searchAirports(query: String): Flow<List<Airport>>

    @Query("SELECT * FROM airport ORDER BY passengers DESC")
    fun getAllAirports(): Flow<List<Airport>>

    @Query("SELECT * FROM airport WHERE iata_code = :code")
    fun getAirport(code: String): Flow<Airport?>

    /**
     * Every airport except [departureCode] - the possible destinations of a flight.
     */
    @Query("SELECT * FROM airport WHERE iata_code != :departureCode ORDER BY passengers DESC")
    fun getDestinations(departureCode: String): Flow<List<Airport>>

    @Query("SELECT * FROM favorite ORDER BY id")
    fun getFavorites(): Flow<List<Favorite>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavorite(favorite: Favorite)

    @Query("DELETE FROM favorite WHERE departure_code = :departureCode AND destination_code = :destinationCode")
    suspend fun deleteFavorite(departureCode: String, destinationCode: String)
}
