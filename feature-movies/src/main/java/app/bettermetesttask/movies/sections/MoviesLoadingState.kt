package app.bettermetesttask.movies.sections

sealed class MoviesLoadingState {
    object Idle : MoviesLoadingState()

    object Loading : MoviesLoadingState()

    object Loaded : MoviesLoadingState()
}