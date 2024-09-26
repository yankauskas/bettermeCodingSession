package app.bettermetesttask.domainmovies.interactors

import app.bettermetesttask.domaincore.utils.Result
import app.bettermetesttask.domainmovies.repository.MoviesRepository
import javax.inject.Inject

class LoadMoviesUseCase @Inject constructor(
    private val repository: MoviesRepository
) {
    suspend operator fun invoke(): Result<Unit> = repository.loadMovies()

}