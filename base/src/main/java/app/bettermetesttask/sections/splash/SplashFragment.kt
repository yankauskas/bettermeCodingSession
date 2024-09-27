package app.bettermetesttask.sections.splash

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import app.bettermetesttask.R
import app.bettermetesttask.featurecommon.injection.utils.Injectable
import app.bettermetesttask.featurecommon.injection.viewmodel.SimpleViewModelProviderFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

class SplashFragment : Fragment(R.layout.splash_fragment), Injectable {

    @Inject
    lateinit var viewModelProvider: Provider<SplashViewModel>

    private val viewModel by viewModels<SplashViewModel> { SimpleViewModelProviderFactory(viewModelProvider) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch(Dispatchers.Main) {
            if (viewModel.isFirstLaunch()) delay(3000L)
            viewModel.handleAppLaunch()
        }
    }
}