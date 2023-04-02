package eg.gov.iti.jets.weather.setting.view

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.plugin.gestures.addOnMapLongClickListener
import eg.gov.iti.jets.weather.Constants
import eg.gov.iti.jets.weather.R
import eg.gov.iti.jets.weather.databinding.FragmentSettingBinding
import java.util.Locale

class SettingFragment : Fragment() {

    private lateinit var binding: FragmentSettingBinding
    private lateinit var specificPoint: Point

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mapCardView.visibility = View.GONE

        val setting: SharedPreferences = requireContext().getSharedPreferences(Constants.settingPreferences, Context.MODE_PRIVATE)
        val settings: SharedPreferences.Editor = setting.edit()

        binding.gpsRadioButton.setOnClickListener {
            settings.putString("location", "gps")
            settings.commit()
        }

        binding.mapRadioButton.setOnClickListener {
            settings.putString("location", "map")
            settings.commit()

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

        binding.mapFloatingActionButton.setOnClickListener {
            binding.mapCardView.visibility = View.GONE
            val mapPreferences: SharedPreferences = requireContext().getSharedPreferences(Constants.mapPreferences, Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor = mapPreferences.edit()
            editor.putString("lat", specificPoint.latitude().toString())
            editor.putString("lon", specificPoint.longitude().toString())
            editor.commit()

            println(specificPoint.latitude().toString()+"++++++++++++"+specificPoint.longitude().toString())
        }

        binding.arabicRadioButton.setOnClickListener {
            settings.putString("lang", "ar")
            settings.commit()
        }

        binding.englishRadioButton.setOnClickListener {
            settings.putString("lang", "en")
            settings.commit()
        }

        binding.msRadioButton.setOnClickListener {
            settings.putString("wind", "ms")
            settings.commit()
        }

        binding.mhRadioButton.setOnClickListener {
            settings.putString("wind", "mh")
            settings.commit()
        }

        binding.notificationRadioButton.setOnClickListener {
            settings.putString("notification", "notify")
            settings.commit()
        }

        binding.alertRadioButton.setOnClickListener {
            settings.putString("notification", "alert")
            settings.commit()
        }

        binding.celsiusRadioButton.setOnClickListener {
            settings.putString("temperature", "celsius")
            settings.commit()
        }

        binding.fahrenheitRadioButton.setOnClickListener {
            settings.putString("temperature", "fahrenheit")
            settings.commit()
        }

        binding.kelvinRadioButton.setOnClickListener {
            settings.putString("temperature", "kelvin")
            settings.commit()
        }

        val location = setting.getString("location", "N/A")
        val lang = setting.getString("lang", "N/A")
        val wind = setting.getString("wind", "N/A")
        val notification = setting.getString("notification", "N/A")
        val temperature = setting.getString("temperature", "N/A")

        if(location.equals("gps")) binding.gpsRadioButton.isChecked = true
        else binding.mapRadioButton.isChecked = true

        if(lang.equals("ar"))
        {
            Constants.setLanguage(requireContext(), "ar")
            binding.arabicRadioButton.isChecked = true
        }
        else
        {
            Constants.setLanguage(requireContext(), "en")
            binding.englishRadioButton.isChecked = true
        }

        if(wind.equals("ms")) binding.msRadioButton.isChecked = true
        else binding.mhRadioButton.isChecked = true

        if(notification.equals("notify")) binding.notificationRadioButton.isChecked = true
        else binding.alertRadioButton.isChecked = true

        if(temperature.equals("celsius")) binding.celsiusRadioButton.isChecked = true
        else if(temperature.equals("fahrenheit")) binding.fahrenheitRadioButton.isChecked = true
        else binding.kelvinRadioButton.isChecked = true

        println("$location $lang $wind $notification $temperature")
    }

    private fun addAnnotationToMap(point: Point) {
        bitmapFromDrawableRes(
            requireContext().applicationContext,
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