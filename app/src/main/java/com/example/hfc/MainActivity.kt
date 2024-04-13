package com.example.hfc

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.hfc.models.MainModel
import com.example.hfc.service.CalculateTimeCppService
import com.example.hfc.service.CalculateTimeKotlinService
import com.example.hfc.ui.theme.HFCTheme
import com.example.hfc.ui_screens.main.MainScreen
import com.example.hfc.ui_screens.main.MainViewModel
import com.example.hfc.ui_screens.SplashScreen
import com.example.hfc.utils.Constanst.MAIN_DESTINATION
import com.example.hfc.utils.Constanst.SPLASH_DESTINATION

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by lazy { MainViewModel(MainModel()) }
    private var navController: NavHostController? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        setContent {
            HFCTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavigationApp()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        bindService(
            Intent(this, CalculateTimeKotlinService::class.java),
            viewModel.getKotlinServiceConnection(),
            Context.BIND_AUTO_CREATE
        )
        bindService(
            Intent(this, CalculateTimeCppService::class.java),
            viewModel.getCppServiceConnection(),
            Context.BIND_AUTO_CREATE
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(viewModel.getKotlinServiceConnection())
        unbindService(viewModel.getCppServiceConnection())
        navController = null
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val currentDestination = navController?.currentBackStackEntry?.destination?.route
        when(currentDestination) {
            MAIN_DESTINATION -> this.finishAffinity()
            else -> super.onBackPressed()
        }
    }


    @Composable
    private fun NavigationApp() {
        navController = rememberNavController()
        NavHost(navController = navController!!, startDestination = SPLASH_DESTINATION) {
            composable(SPLASH_DESTINATION) { SplashScreen(navController = navController!!) }
            composable(MAIN_DESTINATION) { MainScreen(viewModel) }
        }
    }
}