package eg.gov.iti.jets.weather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import eg.gov.iti.jets.weather.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = Navigation.findNavController(this,R.id.containerFrameLayout)
        NavigationUI.setupWithNavController(binding.bottomNav, navController)

        binding.fabBtn.setOnClickListener {
            navController.navigate(R.id.home)
        }

        binding.bottomNav.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.favourite -> navController.navigate(R.id.favourite)
                R.id.alert -> navController.navigate(R.id.alert)
                R.id.setting -> navController.navigate(R.id.setting)
                else ->{}
            }
            true
        }
    }
}