package app.bettermetesttask.movies.sections

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        adapter.onItemClicked = { movie ->
            viewModel.openMovieDetails(movie)
        }

        adapter.onItemLiked = { movie ->
            viewModel.likeMovie(movie)
        }

        lifecycleScope.launch { viewModel.moviesStateFlow.collect(::renderMoviesState) }

        viewModel.loadMovies()
    }

    private fun renderMoviesState(state: MoviesState) {
        when (state) {
            MoviesState.Loading -> {
                binding.rvList.gone()
                binding.progressBar.visible()
            }

            is MoviesState.Loaded -> {
                binding.progressBar.gone()
                binding.rvList.visible()
                adapter.submitList(state.movies)
            }

            else -> {
                // no op
                binding.progressBar.gone()
                binding.rvList.gone()
            }
        }
    }
}