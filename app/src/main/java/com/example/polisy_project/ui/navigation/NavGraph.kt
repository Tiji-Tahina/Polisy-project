package com.example.polisy_project.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.polisy_project.ui.screens.LoginScreen
import com.example.polisy_project.ui.screens.PVScreen
import com.example.polisy_project.ui.screens.SearchScreen
import com.example.polisy_project.ui.viewmodel.LoginViewModel
import com.example.polisy_project.ui.viewmodel.PVViewModel
import com.example.polisy_project.ui.viewmodel.SearchViewModel
import kotlinx.coroutines.launch

object Routes {
    const val LOGIN = "login"
    const val SEARCH = "search"
    const val PV = "pv"
}

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    val loginViewModel: LoginViewModel = viewModel()
    val searchViewModel: SearchViewModel = viewModel()
    val pvViewModel: PVViewModel = viewModel()
    val scope = rememberCoroutineScope()

    NavHost(navController = navController, startDestination = Routes.LOGIN) {
        composable(Routes.LOGIN) {
            LoginScreen(
                viewModel = loginViewModel,
                onLoginSuccess = {
                    navController.navigate(Routes.SEARCH) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.SEARCH) {
            SearchScreen(
                viewModel = searchViewModel,
                onDriverFound = {},
                onCreatePV = { navController.navigate(Routes.PV) },
                onLogout = {
                    loginViewModel.logout()
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.PV) {
            PVScreen(
                viewModel = pvViewModel,
                officerId = 0,
                onPVCreated = {
                    navController.popBackStack()
                }
            )
        }
    }
}
