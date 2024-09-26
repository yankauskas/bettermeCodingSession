package app.bettermetesttask.movies.sections

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import app.bettermetesttask.featurecommon.injection.utils.Injectable
import app.bettermetesttask.featurecommon.injection.viewmodel.SimpleViewModelProviderFactory
import app.bettermetesttask.featurecommon.utils.views.gone
import app.bettermetesttask.featurecommon.utils.views.visible
import app.bettermetesttask.movies.R
import app.bettermetesttask.movies.databinding.MoviesFragmentBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

class MoviesFragment : Fragment(R.layout.movies_fragment), Injectable {

    @Inject
    lateinit var viewModelProvider: Provider<MoviesViewModel>

    @Inject
    lateinit var adapter: MoviesAdapter

    private lateinit var binding: MoviesFragmentBinding

    private val viewModel by viewModels<MoviesViewModel> { SimpleViewModelProviderFactory(viewModelProvider) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = MoviesFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvList.adapter = adapter

        binding.swipeRefreshLayout.setColorSchemeResources(R.color.pure_red)

        adapter.onItemClicked = { movie ->
            viewModel.openMovieDetails(movie)
        }

        adapter.onItemLiked = { movie ->
            viewModel.likeMovie(movie)
        }

        binding.swipeRefreshLayout.setOnRefreshListener { viewModel.loadMovies() }

        lifecycleScope.launch { viewModel.moviesStateFlow.collect(::renderMoviesState) }
        lifecycleScope.launch { viewModel.loadingErrorFlow.collect(::renderMoviesLoadingError) }
    }

    private fun renderMoviesState(state: MoviesState) {

        adapter.submitList(state.movies)

        when (state.loading) {
            MoviesLoadingState.Loaded -> {
                binding.progressBar.gone()
                binding.swipeRefreshLayout.isEnabled = true
                binding.swipeRefreshLayout.isRefreshing = false
            }

            MoviesLoadingState.Loading -> {
                if (state.movies.isEmpty() && binding.swipeRefreshLayout.isRefreshing.not()) {
                    binding.progressBar.visible()
                    binding.swipeRefreshLayout.isEnabled = false
                }
            }

            else -> {}
        }

    }

    private fun renderMoviesLoadingError(error: MoviesLoadingError) {
        Toast.makeText(
            requireContext(),
            error.message ?: getString(R.string.error_unknown),
            Toast.LENGTH_SHORT
        ).show()
    }
}