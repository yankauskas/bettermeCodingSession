package app.bettermetesttask.movies.sections

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.bettermetesttask.domaincore.utils.Result
import app.bettermetesttask.domainmovies.entries.Movie
import app.bettermetesttask.domainmovies.interactors.AddMovieToFavoritesUseCase
import app.bettermetesttask.domainmovies.interactors.LoadMoviesUseCase
import app.bettermetesttask.domainmovies.interactors.ObserveMoviesUseCase
import app.bettermetesttask.domainmovies.interactors.RemoveMovieFromFavoritesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MoviesViewModel @Inject constructor(
    private val observeMoviesUseCase: ObserveMoviesUseCase,
    private val loadMoviesUseCase: LoadMoviesUseCase,
    private val likeMovieUseCase: AddMovieToFavoritesUseCase,
    private val dislikeMovieUseCase: RemoveMovieFromFavoritesUseCase
) : ViewModel() {

    private val _moviesStateFlow = MutableStateFlow(MoviesState())
    private val _moviesLoadingStateFlow = MutableStateFlow<MoviesLoadingState>(MoviesLoadingState.Idle)

    val moviesStateFlow: StateFlow<MoviesState> = _moviesStateFlow.asStateFlow()

    private val _loadingErrorFlow = MutableSharedFlow<MoviesLoadingError>()
    val loadingErrorFlow: SharedFlow<MoviesLoadingError> = _loadingErrorFlow.asSharedFlow()

    private var loadMoviesJob: Job? = null

    init {
        viewModelScope.launch(Dispatchers.Main) {
            observeMoviesUseCase.invoke().combine(_moviesLoadingStateFlow) { movies, state ->
                _moviesStateFlow.emit(MoviesState(movies, state))
            }.collect()
        }

        loadMovies()
    }

    fun loadMovies() {
        if (loadMoviesJob != null) return
        loadMoviesJob = viewModelScope.launch(Dispatchers.IO) {
            _moviesLoadingStateFlow.emit(MoviesLoadingState.Loading)
            val result = loadMoviesUseCase.invoke()
            withContext(Dispatchers.Main) {
                when (result) {
                    is Result.Success -> {
                        _moviesLoadingStateFlow.emit(MoviesLoadingState.Loaded)
                    }
                    is Result.Error -> {
                        _moviesLoadingStateFlow.emit(MoviesLoadingState.Loaded)
                        _loadingErrorFlow.emit(MoviesLoadingError(result.error.message))
                    }
                }
            }

            loadMoviesJob = null
        }
    }

    fun likeMovie(movie: Movie) {
        viewModelScope.launch(Dispatchers.IO) {
            if (movie.liked) {
                dislikeMovieUseCase(movie.id)
            } else {
                likeMovieUseCase(movie.id)
            }
        }
    }

    fun openMovieDetails(movie: Movie) {
        // TODO: todo todo todo todo
    }
}