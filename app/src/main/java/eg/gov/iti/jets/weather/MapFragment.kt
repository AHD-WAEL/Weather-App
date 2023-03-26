package eg.gov.iti.jets.weather

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.navigation.Navigation
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.plugin.gestures.addOnMapLongClickListener
import eg.gov.iti.jets.weather.databinding.FragmentMapBinding

class MapFragment : Fragment() {
    private lateinit var binding: FragmentMapBinding
    private lateinit var specificPoint: Point

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapbox = binding.mapView.getMapboxMap()
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

        binding.mapFloatingActionButton.setOnClickListener {
            val location: SharedPreferences = requireContext().getSharedPreferences(
                Constants.locationPreferences,
                Context.MODE_PRIVATE
            )
            val loc: SharedPreferences.Editor = location.edit()
            loc.putString("lat", specificPoint.latitude().toString())
            loc.putString("lon", specificPoint.longitude().toString())
            loc.commit()
            Navigation.findNavController(it).navigate(R.id.favourite)
        }
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