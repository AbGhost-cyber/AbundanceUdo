package com.example.abundanceudo.featureBmi.presentation

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
import com.example.abundanceudo.featureBmi.presentation.addBmi.viewmodel.AddBmiViewModel
import com.google.android.gms.ads.MobileAds
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private var appBarConfig: AppBarConfiguration? = null
    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<AddBmiViewModel>()

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
            setupActionBarWithNavController(navController, appBarConfig!!)
        }

        // use content provider for ads
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfig!!) || super.onSupportNavigateUp()
    }
}
