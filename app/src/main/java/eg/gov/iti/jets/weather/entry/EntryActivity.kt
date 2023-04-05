package eg.gov.iti.jets.weather.entry

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.plugin.gestures.addOnMapLongClickListener
import eg.gov.iti.jets.weather.Constants
import eg.gov.iti.jets.weather.MainActivity
import eg.gov.iti.jets.weather.PERMISSION_ID
import eg.gov.iti.jets.weather.R
import eg.gov.iti.jets.weather.databinding.ActivityEntryBinding
import eg.gov.iti.jets.weather.databinding.EnteryDialogCardBinding

class EntryActivity : AppCompatActivity() {

    private lateinit var fusedLocation: FusedLocationProviderClient
    private lateinit var geoCoder: Geocoder
    lateinit var mLocation: Location
    private lateinit var currentLocation: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    private lateinit var specificPoint: Point
    private lateinit var binding:ActivityEntryBinding
    private lateinit var dialogCard: EnteryDialogCardBinding
    private lateinit var dialog: Dialog
    private lateinit var firstTimeSharedPreferences: SharedPreferences
    private lateinit var firstTimeEditor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEntryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.mapCardView.visibility = View.GONE

        fusedLocation = LocationServices.getFusedLocationProviderClient(this)
        geoCoder = Geocoder(this)

        currentLocation = this.getSharedPreferences(Constants.settingPreferences, Context.MODE_PRIVATE)
        editor = currentLocation.edit()

        firstTimeSharedPreferences = this.getSharedPreferences(Constants.firstTimePreferences, Context.MODE_PRIVATE)
        val firstTime = firstTimeSharedPreferences.getString("firstTime", "no")

        if (firstTime.equals("no")){
            dialogCard()
            firstTimeEditor = firstTimeSharedPreferences.edit()
            firstTimeEditor.putString("firstTime", "yes")
            firstTimeEditor.commit()
        }
        else {
            val intent = Intent(this@EntryActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }
    override fun onResume() {
        super.onResume()
        getLocation()
    }
    private fun dialogCard(){
        dialogCard = EnteryDialogCardBinding.inflate(layoutInflater)
        dialog = Dialog(this)
        dialog.setContentView(dialogCard.root)
        dialog.setCancelable(true)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
        dialogCard.initializationBtn.setOnClickListener {
            when (dialogCard.locationInitializationRadioGroup.checkedRadioButtonId) {
                dialogCard.gpsInitializationRadioButton.id -> {
                    editor.putString("location","gps")
                    editor.commit()
                    currentLocation = this.getSharedPreferences(Constants.currentLocation, Context.MODE_PRIVATE)
                    editor = currentLocation.edit()
                    getLocation()
                    //if(checkPermissions()){
                        val intent = Intent(this@EntryActivity, MainActivity::class.java)
                        startActivity(intent)
                        println("++++++++++++++++++++++++++++")
                    /*}
                    else{
                        val parentLayout = findViewById<View>(android.R.id.content)
                        Snackbar.make(parentLayout,"Give me permissions please", Snackbar.LENGTH_LONG)
                            .setBackgroundTint(resources.getColor(R.color.purple_700)).show()
                        getLocation()
                    }*/
                }
                dialogCard.mapInitializationRadioButton.id -> {
                    editor.putString("location","map")
                    editor.commit()

                    dialog.hide()

                    val mapbox = binding.mapView.getMapboxMap()
                    binding.mapCardView.visibility = View.VISIBLE
                    binding.backMap.visibility = View.VISIBLE
                    binding.mapFloatingActionButton.visibility = View.GONE
                    binding.mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS)
                    val cameraLens = CameraOptions.Builder().center(Point.fromLngLat(26.8206, 30.8025))
                        .zoom(4.0)
                        .build()
                    mapbox.setCamera(cameraLens)
                    mapbox.addOnMapLongClickListener { point ->
                        specificPoint = point
                        binding.mapView.annotations.cleanup()
                        addAnnotationToMap(point)
                        binding.mapFloatingActionButton.visibility = View.VISIBLE
                        true
                    }
                }
                else -> {
                    Toast.makeText(this, "Nothing is checked", Toast.LENGTH_LONG).show()
                }
            }
        }
        binding.mapFloatingActionButton.setOnClickListener {
            binding.mapCardView.visibility = View.GONE
            val mapPreferences: SharedPreferences = this.getSharedPreferences(Constants.mapPreferences, Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor = mapPreferences.edit()
            editor.putString("lat", specificPoint.latitude().toString())
            editor.putString("lon", specificPoint.longitude().toString())
            editor.commit()

            val intent = Intent(this@EntryActivity, MainActivity::class.java)
            startActivity(intent)

            println(specificPoint.latitude().toString()+"++++++++++++"+specificPoint.longitude().toString())
        }

        binding.backMap.setOnClickListener {
            binding.mapCardView.visibility = View.GONE
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


    private fun addAnnotationToMap(point: Point) {
        bitmapFromDrawableRes(
            this,
            R.drawable.red_marker
        )?.let {
            val annotationApi = binding.mapView.annotations
            val pointAnnotationManager = annotationApi.createPointAnnotationManager(binding.mapView)
            val pointAnnotationOptions: PointAnnotationOptions = PointAnnotationOptions()
                .withPoint(point)
                .withIconImage(it)
            pointAnnotationManager.create(pointAnnotationOptions)
        }
    }

    private fun bitmapFromDrawableRes(context: Context, @DrawableRes resourceId: Int) =
        convertDrawableToBitmap(AppCompatResources.getDrawable(context, resourceId))

    private fun convertDrawableToBitmap(sourceDrawable: Drawable?): Bitmap? {
        if (sourceDrawable == null) {
            return null
        }
        return if (sourceDrawable is BitmapDrawable) {
            sourceDrawable.bitmap
        } else {
            val constantState = sourceDrawable.constantState ?: return null
            val drawable = constantState.newDrawable().mutate()
            val bitmap: Bitmap = Bitmap.createBitmap(
                drawable.intrinsicWidth, drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            bitmap
        }
    }
}