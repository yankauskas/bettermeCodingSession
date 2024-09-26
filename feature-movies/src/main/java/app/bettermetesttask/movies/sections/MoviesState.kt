package app.bettermetesttask.movies.sections

import app.bettermetesttask.domainmovies.entries.Movie

data class MoviesState(
    val movies: List<Movie> = emptyList(),
    val loading: MoviesLoadingState = MoviesLoadingState.Idle
)