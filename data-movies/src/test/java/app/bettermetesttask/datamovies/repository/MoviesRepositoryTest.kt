package app.bettermetesttask.datamovies.repository

import app.bettermetesttask.datamovies.database.entities.MovieEntity
import app.bettermetesttask.datamovies.repository.stores.MoviesLocalStore
import app.bettermetesttask.datamovies.repository.stores.MoviesMapper
import app.bettermetesttask.datamovies.repository.stores.MoviesRestStore
import app.bettermetesttask.domaincore.utils.Result
import app.bettermetesttask.domainmovies.entries.Movie
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class MoviesRepositoryTest {
    @Mock
    private lateinit var localStore: MoviesLocalStore

    @Mock
    private lateinit var mapper: MoviesMapper

    @Mock
    private lateinit var restStore: MoviesRestStore

    @InjectMocks
    private lateinit var moviesRepository: MoviesRepositoryImpl

    private val movie = Movie(id = 1, title = "Test Movie", description = "Test Description", posterPath = "Test Path")
    private val movieEntity = MovieEntity(id = 1, title = "Test Movie", description = "Test Description", posterPath = "Test Path")

    @BeforeEach
    fun setUp() {
    }

    @Test
    fun `loadMovies should save movies to local store`() {
        runBlocking {
            // Arrange
            val moviesList = listOf(movie)
            `when`(restStore.getMovies()).thenReturn(moviesList)
            `when`(mapper.mapToLocal(movie)).thenReturn(movieEntity)

            // Act
            val result = moviesRepository.loadMovies()

            // Assert
            assertTrue(result is Result.Success)
            verify(localStore).saveMovies(listOf(movieEntity))
        }
    }
}