package eg.gov.iti.jets.weather

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.android.gms.location.*
import eg.gov.iti.jets.weather.databinding.ActivityMainBinding
import eg.gov.iti.jets.weather.databinding.EnteryDialogCardBinding

const val PERMISSION_ID = 44
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    lateinit var fusedLocation: FusedLocationProviderClient
    lateinit var geoCoder: Geocoder
    lateinit var currentLocation: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocation = LocationServices.getFusedLocationProviderClient(this)
        geoCoder = Geocoder(this)

        currentLocation = this.getSharedPreferences(Constants.currentLocation, Context.MODE_PRIVATE)
        editor = currentLocation.edit()

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