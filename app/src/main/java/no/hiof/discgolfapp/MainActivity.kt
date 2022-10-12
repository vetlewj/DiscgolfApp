package no.hiof.discgolfapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import no.hiof.discgolfapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController
        val drawerLayout = binding.drawerLayout

        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)

        val topAppBar = binding.topAppBar
        topAppBar.setNavigationOnClickListener {
            Log.d("MainActivity", "Navigation icon clicked")
            drawerLayout.open()

        }

        binding.bottomNavView.setupWithNavController(navController)
    }


}