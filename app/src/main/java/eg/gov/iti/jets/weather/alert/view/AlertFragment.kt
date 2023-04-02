package eg.gov.iti.jets.weather.alert.view

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.plugin.gestures.addOnMapLongClickListener
import eg.gov.iti.jets.weather.R
import eg.gov.iti.jets.weather.alert.viewModel.AlertViewModel
import eg.gov.iti.jets.weather.alert.viewModel.AlertViewModelFactory
import eg.gov.iti.jets.weather.databinding.DeleteDialogBinding
import eg.gov.iti.jets.weather.databinding.FragmentAlertBinding
import eg.gov.iti.jets.weather.db.ConcreteLocalSource
import eg.gov.iti.jets.weather.model.CurrentAlert
import eg.gov.iti.jets.weather.model.Repository
import eg.gov.iti.jets.weather.network.WeatherClient
import java.util.*

class AlertFragment : Fragment() {

    private lateinit var binding: FragmentAlertBinding
    private lateinit var specificPoint: Point
    private lateinit var geoCoder: Geocoder
    private val calendar = Calendar.getInstance()
    private lateinit var alertViewModel: AlertViewModel
    private lateinit var alertViewModelFactory: AlertViewModelFactory
    private lateinit var alertAdapter: AlertAdapter
    private var requestID = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAlertBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        geoCoder = Geocoder(requireContext())
        binding.mapCardView.visibility = View.GONE
        alertViewModelFactory = AlertViewModelFactory(Repository.getInstance(WeatherClient.getInstance(),ConcreteLocalSource(requireContext())))
        alertViewModel = ViewModelProvider(this, alertViewModelFactory).get(AlertViewModel::class.java)
        alertViewModel.alertList.observe(viewLifecycleOwner){ currentAlerts ->
            //requestID = currentAlerts.size
            alertAdapter = AlertAdapter(currentAlerts){
                dialogCard(it)
            }
            binding.alertRecyclerView.adapter = alertAdapter
            alertAdapter.notifyDataSetChanged()

            for (i in currentAlerts){
                if(checkAlert(i))
                {
                    alertViewModel.deleteAlert(i)
                    val alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
                    val intent = Intent(requireContext(), MyReceiver::class.java)
                    val pending = PendingIntent.getBroadcast(requireContext(),i.id.toInt(), intent, PendingIntent.FLAG_IMMUTABLE)
                    alarmManager.cancel(pending)
                    alertAdapter.notifyDataSetChanged()
                }
            }
        }
        binding.alertFloatingActionButton.setOnClickListener {
            DatePickerDialog(requireContext(),{view, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                TimePickerDialog(requireContext(),{view, hourofDay, minute ->
                    calendar.set(Calendar.HOUR_OF_DAY, hourofDay)
                    calendar.set(Calendar.MINUTE, minute)
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
                }, Calendar.getInstance().get(Calendar.HOUR_OF_DAY),Calendar.getInstance().get(Calendar.MINUTE), android.text.format.DateFormat.is24HourFormat(requireContext())).show()
            }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH)).show()
        }

        binding.mapFloatingActionButton.setOnClickListener {
            binding.mapCardView.visibility = View.GONE
            val dateAndTime = "${calendar.get(Calendar.DAY_OF_MONTH)}/${calendar.get(Calendar.MONTH)+1}/${calendar.get(Calendar.YEAR)}\n${calendar.get(Calendar.HOUR)}:${calendar.get(Calendar.MINUTE)}"
            val location = geoCoder.getFromLocation(specificPoint.latitude(), specificPoint.longitude(), 1) as MutableList<Address>
            val loc = location[0].adminArea.toString() + "/" + location[0].countryName.toString()
            requestID++
            var currentAlert = CurrentAlert(requestID.toLong(), loc, dateAndTime)
            alertViewModel.insertAlert(currentAlert)
            val alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val i = Intent(requireContext(), MyReceiver::class.java)
            i.putExtra("lat", specificPoint.latitude().toString())
            i.putExtra("lon", specificPoint.longitude().toString())
            println(specificPoint.latitude().toString()+"--------------"+specificPoint.longitude().toString())
            val pending = PendingIntent.getBroadcast(requireContext(),requestID, i, PendingIntent.FLAG_IMMUTABLE)
            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,calendar.timeInMillis, pending)
            requestID++
        }

        binding.backMap.setOnClickListener {
            binding.mapCardView.visibility = View.GONE
        }
    }

    private fun checkAlert(currentAlert: CurrentAlert):Boolean
    {
        val day = currentAlert.dateAndTime.split("/")[0]
        val month = (currentAlert.dateAndTime.split("/")[1].toInt() - 1).toString()
        val year = currentAlert.dateAndTime.split("/")[2].split(System.lineSeparator())[0]
        val hour = currentAlert.dateAndTime.split(System.lineSeparator())[1].split(":")[0]
        val min = currentAlert.dateAndTime.split(System.lineSeparator())[1].split(":")[1]

        println("$day $month $year $hour $min ***************************")
        println("${Calendar.getInstance().get(Calendar.YEAR)} ")
        print("${Calendar.getInstance().get(Calendar.MONTH)} ")
        print("${Calendar.getInstance().get(Calendar.DAY_OF_MONTH)} ")
        print("${Calendar.getInstance().get(Calendar.HOUR)} ")
        print("${Calendar.getInstance().get(Calendar.MINUTE)} ")

        if(Calendar.getInstance().get(Calendar.YEAR).toString() < year) return false
        else if (Calendar.getInstance().get(Calendar.YEAR).toString() > year) return true
        else
        {
            return if(Calendar.getInstance().get(Calendar.MONTH).toString() < month) false
            else if(Calendar.getInstance().get(Calendar.MONTH).toString() > month) true
            else {
                if(Calendar.getInstance().get(Calendar.DAY_OF_MONTH).toString() < day) false
                else if(Calendar.getInstance().get(Calendar.DAY_OF_MONTH).toString() > day) true
                else {
                    if(Calendar.getInstance().get(Calendar.HOUR).toString() < hour) false
                    else if(Calendar.getInstance().get(Calendar.HOUR).toString() > hour) true
                    else {
                        Calendar.getInstance().get(Calendar.MINUTE).toString() > min
                    }
                }
            }
        }
    }
    private fun dialogCard(currentAlert: CurrentAlert) {
        val dialogCard: DeleteDialogBinding = DeleteDialogBinding.inflate(layoutInflater)
        val dialog = Dialog(requireContext())
        dialog.setContentView(dialogCard.root)
        dialog.setCancelable(true)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
        dialogCard.okBtn.setOnClickListener {
            val alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val i = Intent(requireContext(), MyReceiver::class.java)
            val pending = PendingIntent.getBroadcast(requireContext(),it.id.toInt(), i, PendingIntent.FLAG_IMMUTABLE)
            alarmManager.cancel(pending)
            alertViewModel.deleteAlert(currentAlert)
            alertAdapter.notifyDataSetChanged()
            dialog.hide()
        }
        dialogCard.cancelBtn.setOnClickListener {
            dialog.hide()
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