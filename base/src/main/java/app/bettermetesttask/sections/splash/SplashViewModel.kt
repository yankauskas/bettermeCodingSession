package app.bettermetesttask.sections.splash

import androidx.lifecycle.ViewModel
import app.bettermetesttask.domaincore.utils.preferences.Preferences
import app.bettermetesttask.navigation.HomeCoordinator
import javax.inject.Inject

class SplashViewModel @Inject constructor(
    private val coordinator: HomeCoordinator,
    private val preferences: Preferences
) : ViewModel() {

    fun isFirstLaunch() = preferences.isFirstLaunch()

    fun handleAppLaunch() {
        preferences.markAlreadyLaunched()
        coordinator.start()
    }
}