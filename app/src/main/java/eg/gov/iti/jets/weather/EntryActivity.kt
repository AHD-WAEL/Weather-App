package eg.gov.iti.jets.weather

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.navigation.Navigation
import com.google.android.gms.location.*
import eg.gov.iti.jets.weather.databinding.EnteryDialogCardBinding

class EntryActivity : AppCompatActivity() {

    lateinit var fusedLocation: FusedLocationProviderClient
    lateinit var geoCoder: Geocoder
    lateinit var mLocation: Location
    lateinit var currentLocation: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entry)

        fusedLocation = LocationServices.getFusedLocationProviderClient(this)
        geoCoder = Geocoder(this)

        currentLocation = this.getSharedPreferences(Constants.currentLocation, Context.MODE_PRIVATE)
        editor = currentLocation.edit()

        dialogCard()
        Thread.sleep(2000)
        var location = currentLocation.getString("location", "N/A")
        println("*********************"+location)
        Thread.sleep(2000)
        if (location.equals("gps"))
        {
            println("++++++++++++++++++++++++++++gps")
            getLocation()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        else if (location.equals("map")){
            println("--------------------------map")
            supportFragmentManager.beginTransaction().replace(R.id.entryLayout,MapFragment())
        }
    }

    /*override fun onResume() {
        super.onResume()
        getLocation()
    }*/

    private fun dialogCard(){
        val dialogCard: EnteryDialogCardBinding = EnteryDialogCardBinding.inflate(layoutInflater)
        //val dialogCard = layoutInflater.inflate(R.layout.entery_dialog_card, null)
        val dialog = Dialog(this)
        dialog.setContentView(dialogCard.root)
        dialog.setCancelable(true)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
        dialogCard.initializationBtn.setOnClickListener {
            when (dialogCard.locationInitializationRadioGroup.checkedRadioButtonId) {
                dialogCard.gpsInitializationRadioButton.id -> {
                    editor.putString("location","gps")
                    editor.commit()
                    println("++++++++++++++++++++++++++++")
                }
                dialogCard.mapInitializationRadioButton.id -> {
                    editor.putString("location","map")
                    editor.commit()
                }
                else -> {
                    Toast.makeText(this, "Nothing is checked", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun getLocation() {
        if(checkPermissions()){
            if(isLocationEnabled())
            {
                requestNewLocationData()
            }
            else{
                Toast.makeText(this,"Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        }
        else{
            requestPermissions()
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        val locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        fusedLocation = LocationServices.getFusedLocationProviderClient(this)
        fusedLocation.requestLocationUpdates(
            locationRequest, mLocationCallback, Looper.myLooper()
        )
    }

    private val mLocationCallback: LocationCallback = object : LocationCallback(){
        override fun onLocationResult(p0: LocationResult) {
            mLocation= p0.lastLocation
            editor.putString("lat", mLocation.latitude.toString())
            editor.putString("lon", mLocation.longitude.toString())
            editor.commit()
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER)
    }

    private fun requestPermissions(){
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_ID
        )
    }

    private fun checkPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }
}