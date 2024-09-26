package app.bettermetesttask.domainmovies.repository

import app.bettermetesttask.domaincore.utils.Result
import app.bettermetesttask.domainmovies.entries.Movie
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {

    suspend fun loadMovies(): Result<Unit>

    suspend fun getMovie(id: Int): Result<Movie>

    fun observeMovies(): Flow<List<Movie>>

    fun observeLikedMovieIds(): Flow<List<Int>>

    suspend fun addMovieToFavorites(movieId: Int)

    suspend fun removeMovieFromFavorites(movieId: Int)
}
