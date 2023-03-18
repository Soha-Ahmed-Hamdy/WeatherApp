package com.example.weatherapp

import android.app.Application
import android.os.Bundle
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.weatherapp.data.repository.Repository
import com.example.weatherapp.data.utils.*
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.ui.alert.AlertManager
import com.example.weatherapp.ui.alert.alertViewModel.AlertViewModel
import com.example.weatherapp.ui.alert.alertViewModel.FactoryAlert
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    lateinit var alertViewModel: AlertViewModel
    lateinit var repository: Repository
    lateinit var fact: FactoryAlert


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        LocaleManager.setLocale(this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        if(SharedPrefData.theme==Utility.dark){
            binding.backgroundd.setImageResource(R.drawable.img_4)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        }
        else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        }

        setSupportActionBar(binding.appBarMain.toolbar)

        repository = Repository.getRepositoryInstance(this.application)
        fact = FactoryAlert(repository)

        alertViewModel =
            ViewModelProvider(this, fact)[AlertViewModel::class.java]
        activateAlerts(this)


        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_favourite, R.id.nav_alert, R.id.nav_setting
            ), drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun activateAlerts(activity: AppCompatActivity) {
        val alertsManager = AlertManager(activity)
        alertViewModel = AlertViewModel(repository)
        lifecycleScope.launch {
            alertViewModel.alert.collectLatest {
                when (it) {
                    is AlertState.Loading -> {

                    }
                    is AlertState.Success -> {

                        it.alertsData.forEach {
                            alertsManager.createAlert(it)
                        }
                    }
                    is AlertState.Failure -> {

                    }
                }
            }

        }
    }
}