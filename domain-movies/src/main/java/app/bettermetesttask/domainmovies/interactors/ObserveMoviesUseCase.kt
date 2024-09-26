package app.bettermetesttask.domainmovies.interactors

import app.bettermetesttask.domainmovies.entries.Movie
import app.bettermetesttask.domainmovies.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class ObserveMoviesUseCase @Inject constructor(
    private val repository: MoviesRepository
) {
    operator fun invoke(): Flow<List<Movie>> {
        return repository.observeMovies().combine(repository.observeLikedMovieIds()) { movies, likedMoviesIds ->
            movies.map {
                if (likedMoviesIds.contains(it.id)) {
                    it.copy(liked = true)
                } else {
                    it
                }
            }
        }
    }
}