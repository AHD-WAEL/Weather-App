package eg.gov.iti.jets.weather.home.view

import android.content.Context
import android.content.SharedPreferences
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.squareup.picasso.Picasso
import eg.gov.iti.jets.weather.Constants
import eg.gov.iti.jets.weather.R
import eg.gov.iti.jets.weather.databinding.FragmentHomeBinding
import eg.gov.iti.jets.weather.db.ConcreteLocalSource
import eg.gov.iti.jets.weather.home.viewModel.HomeViewModel
import eg.gov.iti.jets.weather.home.viewModel.HomeViewModelFactory
import eg.gov.iti.jets.weather.model.*
import eg.gov.iti.jets.weather.network.ApiState
import eg.gov.iti.jets.weather.network.WeatherClient
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.util.*


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var homeViewModelFactory: HomeViewModelFactory
    private lateinit var hourAdapter: HourAdapter
    private lateinit var dayAdapter: DayAdapter
    private lateinit var geoCoder: Geocoder
    private lateinit var currentLocation: SharedPreferences
    lateinit var lat: String
    lateinit var lon: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val setting: SharedPreferences = requireContext().getSharedPreferences(Constants.settingPreferences, Context.MODE_PRIVATE)
        val wind = setting.getString("wind", "N/A")
        val temperature = setting.getString("temperature", "N/A")
        val lang = setting.getString("lang", "N/A")
        if(lang.equals("ar")) Constants.setLanguage(requireContext(), "ar")
        else Constants.setLanguage(requireContext(), "en")
        geoCoder = Geocoder(requireContext())
        currentLocation = requireContext().getSharedPreferences(Constants.FavPreferences, Context.MODE_PRIVATE)
        if (currentLocation.getString("source", "none") == "fav") {
            lat = currentLocation.getString("lat", "33.44").toString()
            lon = currentLocation.getString("lon", "-94.04").toString()
            val editor = currentLocation.edit()
            editor.putString("source", "none")
            editor.commit()
            binding.homeTextView.text = requireContext().getString(R.string.favourite)
        } else {
            currentLocation = requireContext().getSharedPreferences(Constants.settingPreferences, Context.MODE_PRIVATE)
            val location = currentLocation.getString("location", "none")
            if(location.equals("gps"))
            {
                currentLocation = requireContext().getSharedPreferences(Constants.settingPreferences, Context.MODE_PRIVATE)
                lat = currentLocation.getString("lat", "33.44").toString()
                lon = currentLocation.getString("lon", "-94.04").toString()
            }
            else if(location.equals("map"))
            {
                currentLocation = requireContext().getSharedPreferences(Constants.mapPreferences, Context.MODE_PRIVATE)
                lat = currentLocation.getString("lat", "33.44").toString()
                lon = currentLocation.getString("lon", "-94.04").toString()
            }
        }

        homeViewModelFactory = HomeViewModelFactory(
            Repository.getInstance(
                WeatherClient.getInstance(),
                ConcreteLocalSource(requireContext())
            )
        )
        homeViewModel = ViewModelProvider(this, homeViewModelFactory)[HomeViewModel::class.java]
        if (!Constants.checkForInternet(requireContext().applicationContext)) {
            homeViewModel.getHomeRootFromDB()
            homeViewModel.home.observe(viewLifecycleOwner) {
                initializeUI(it, temperature.toString(), wind.toString())
                dayAdapter = DayAdapter(SpecificDay.getSpecificDay(it), requireContext().applicationContext)
                binding.dayHomeRecyclerView.adapter = dayAdapter
                dayAdapter.notifyDataSetChanged()
                hourAdapter = HourAdapter(SpecificTime.getSpecificTime(it), requireContext().applicationContext)
                binding.hourHomeRecyclerView.adapter = hourAdapter
                hourAdapter.notifyDataSetChanged()
            }
        } else {
            homeViewModel.getHomeLocation(lat, lon, lang.toString())
            currentLocation = requireContext().getSharedPreferences(Constants.currentLocation, Context.MODE_PRIVATE)

            lifecycleScope.launch{
                homeViewModel.weather.collectLatest{weather ->
                    when(weather){
                        is ApiState.Loading ->{
                            binding.progressBar.visibility = View.VISIBLE
                            binding.constrainLayout.visibility = View.GONE
                        }
                        is ApiState.Success ->{
                            binding.progressBar.visibility = View.GONE
                            binding.constrainLayout.visibility = View.VISIBLE

                            val currentLoc = String.format(Locale.US, "%.4f", currentLocation.getString("lat", "33.44")!!.toFloat())
                            val retrofitLoc = String.format(Locale.US, "%.4f", weather.data.lat.toFloat())

                            if ( currentLoc == retrofitLoc) {
                                println("+++++++++++++++++++++++++++++++++++++")
                                if(weather.data.alerts == null)
                                {
                                    weather.data.alerts = emptyList()
                                }
                                homeViewModel.insertHomeRootToDB(weather.data)
                            }
                            hourAdapter = HourAdapter(
                                SpecificTime.getSpecificTime(weather.data),
                                requireContext().applicationContext
                            )
                            binding.hourHomeRecyclerView.adapter = hourAdapter
                            hourAdapter.notifyDataSetChanged()
                            dayAdapter =
                                DayAdapter(SpecificDay.getSpecificDay(weather.data), requireContext().applicationContext)
                            binding.dayHomeRecyclerView.adapter = dayAdapter
                            hourAdapter.notifyDataSetChanged()
                            initializeUI(weather.data, temperature.toString(), wind.toString())
                        }
                        else ->{
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(requireContext().applicationContext,"check connection", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }

    private fun initializeUI(root: Root, temperature: String, wind: String) {
        val location = geoCoder.getFromLocation(root.lat, root.lon, 1) as MutableList<Address>
        val loc = location[0].adminArea.toString() + "/" + location[0].countryName.toString()
        binding.cityTextView.text = loc
        val fullDate = root.current.dt.toLong() * 1000 + root.timezone_offset - 7200
        val date = Date(fullDate).toString().split(" ")
        val dateString =
            date[0] + ", " + date[1] + " " + date[2] + ", " + date[3].split(":")[0] + ":" + date[3].split(
                ":"
            )[1]
        binding.dateTextView.text = dateString
        Picasso.get().load(Constants.iconImage(root.current.weather[0].icon)).into(binding.weatherImageView)
        if (temperature.equals("celsius")) {
            binding.temperatureTextView.text = root.current.temp.toInt().toString()
            binding.unitTextView.text = "C"
        } else if (temperature.equals("fahrenheit")) {
            binding.temperatureTextView.text = Constants.fromCtoF(root.current.temp).toInt().toString()
            binding.unitTextView.text = "F"
        } else {
            binding.temperatureTextView.text = Constants.fromCtoK(root.current.temp).toInt().toString()
            binding.unitTextView.text = "K"
        }
        binding.currentDescriptionTextView.text = root.current.weather[0].description
        binding.pressureNumberTextView.text = root.current.pressure.toString()
        binding.humidityNumberTextView.text = root.current.humidity.toString()
        binding.cloudNumberTextView.text = root.current.clouds.toString()
        if (wind.equals("ms")) {
            binding.windNumberTextView.text = root.current.wind_speed.toString()
            binding.windUnitTextView.text = "m/s"
        } else {
            binding.windNumberTextView.text =
                DecimalFormat("###.##").format(Constants.fromMStoMH(root.current.wind_speed)).toString()
            binding.windUnitTextView.text = "m/h"
        }
        binding.ultraVioletNumberTextView.text = root.current.uvi.toString()
        binding.visibilityNumberTextView.text = root.current.visibility.toString()
    }
}