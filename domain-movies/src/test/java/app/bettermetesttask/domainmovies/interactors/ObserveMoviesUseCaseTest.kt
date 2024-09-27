package app.bettermetesttask.domainmovies.interactors

import app.bettermetesttask.domainmovies.entries.Movie
import app.bettermetesttask.domainmovies.repository.MoviesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension

@ExperimentalCoroutinesApi
@ExtendWith(MockitoExtension::class)
internal class ObserveMoviesUseCaseTest {

    @Mock
    private lateinit var repository: MoviesRepository

    @Test
    fun `invoke should combine movies and liked movie ids`() = runBlockingTest {
        // Arrange
        val movies = listOf(
            Movie(id = 1, title = "Movie 1", description = "Description 1", posterPath = "Path 1", liked = false),
            Movie(id = 2, title = "Movie 2", description = "Description 2", posterPath = "Path 2", liked = false)
        )
        val likedMovieIds = listOf(1)

        `when`(repository.observeMovies()).thenReturn(flowOf(movies))
        `when`(repository.observeLikedMovieIds()).thenReturn(flowOf(likedMovieIds))

        val useCase = ObserveMoviesUseCase(repository)

        // Act
        val result: Flow<List<Movie>> = useCase.invoke()
        val resultList = result.toList()

        // Assert
        val expected = listOf(
            listOf(
                Movie(id = 1, title = "Movie 1", description = "Description 1", posterPath = "Path 1", liked = true),
                Movie(id = 2, title = "Movie 2", description = "Description 2", posterPath = "Path 2", liked = false)
            )
        )
        assertEquals(expected, resultList)
    }
}