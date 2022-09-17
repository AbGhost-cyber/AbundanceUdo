package com.example.abundanceudo.feature_bmi.presentation

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.abundanceudo.R
import com.example.abundanceudo.databinding.ActivityMainBinding
import com.example.abundanceudo.feature_bmi.presentation.shared_viewmodels.BmiSharedViewModel
import com.google.android.gms.ads.MobileAds
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private var appBarConfig: AppBarConfiguration? = null
    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<BmiSharedViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition { viewModel.isLoading.value }
        }
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        MobileAds.initialize(this)

        setSupportActionBar(binding.layoutToolBar)
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
        appBarConfig = AppBarConfiguration(navController.graph)
        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
            setupActionBarWithNavController(navController, appBarConfig!!)
        }
        // default title
        binding.tvTitleBar.text = navController.currentDestination?.label

        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.tvTitleBar.text = destination.label
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfig!!) || super.onSupportNavigateUp()
    }
}
