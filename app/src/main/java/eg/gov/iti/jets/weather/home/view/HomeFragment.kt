package eg.gov.iti.jets.weather.home.view

import android.content.Context
import android.content.SharedPreferences
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import eg.gov.iti.jets.weather.Constants
import eg.gov.iti.jets.weather.R
import eg.gov.iti.jets.weather.databinding.FragmentHomeBinding
import eg.gov.iti.jets.weather.db.ConcreteLocalSource
import eg.gov.iti.jets.weather.home.viewModel.HomeViewModel
import eg.gov.iti.jets.weather.home.viewModel.HomeViewModelFactory
import eg.gov.iti.jets.weather.model.*
import eg.gov.iti.jets.weather.network.WeatherClient
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
    private var connection: Boolean = true

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
            currentLocation = requireContext().getSharedPreferences(Constants.currentLocation, Context.MODE_PRIVATE)
            lat = currentLocation.getString("lat", "33.44").toString()
            lon = currentLocation.getString("lon", "-94.04").toString()
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
            homeViewModel.getDayFromDB()
            homeViewModel.getHourFromDB()
            homeViewModel.home.observe(viewLifecycleOwner) {
                initializeUI(it, temperature.toString(), wind.toString())
            }
            homeViewModel.day.observe(viewLifecycleOwner) {
                dayAdapter = DayAdapter(it, requireContext().applicationContext)
                binding.dayHomeRecyclerView.adapter = dayAdapter
                dayAdapter.notifyDataSetChanged()
            }
            homeViewModel.hour.observe(viewLifecycleOwner) {
                hourAdapter = HourAdapter(it, requireContext().applicationContext)
                binding.hourHomeRecyclerView.adapter = hourAdapter
                hourAdapter.notifyDataSetChanged()
            }
        } else {
            homeViewModel.getHomeLocation(lat, lon, lang.toString())
            currentLocation = requireContext().getSharedPreferences(Constants.currentLocation, Context.MODE_PRIVATE)
            homeViewModel.weather.observe(viewLifecycleOwner) {
                val homeRoot = HomeRoot.getHomeRootFromRoot(it)
                println(currentLocation.getString("lat", "33.44")+"++++++++++++++++++"+currentLocation.getString("lon", "-94.04"))
                println(homeRoot.lat.toString()+"++++++++++++++++++"+homeRoot.lon.toString())

                val currentLoc = String.format(Locale.US, "%.4f", currentLocation.getString("lat", "33.44")!!.toFloat())
                val retrofitLoc = String.format(Locale.US, "%.4f", homeRoot.lat!!.toFloat())
                println(currentLoc+"-----------------------"+retrofitLoc)

              //  if ( currentLoc == retrofitLoc) {
                if(currentLocation.getString("lat", "33.44")!!.contains(homeRoot.lat.toString())){
                    println("+++++++++++++++++++++++++++++++++++++")
                    homeViewModel.insertHomeRootToDB(homeRoot)
                    for (i in SpecificDay.getSpecificDay(it))
                        homeViewModel.insertDayToDB(i)
                    for (i in SpecificTime.getSpecificTime(it))
                        homeViewModel.insertHourToDB(i)
                }
                hourAdapter = HourAdapter(
                    SpecificTime.getSpecificTime(it),
                    requireContext().applicationContext
                )
                binding.hourHomeRecyclerView.adapter = hourAdapter
                hourAdapter.notifyDataSetChanged()
                dayAdapter =
                    DayAdapter(SpecificDay.getSpecificDay(it), requireContext().applicationContext)
                binding.dayHomeRecyclerView.adapter = dayAdapter
                hourAdapter.notifyDataSetChanged()
                initializeUI(homeRoot, temperature.toString(), wind.toString())
            }
        }
    }

    private fun initializeUI(homeRoot: HomeRoot, temperature: String, wind: String) {
        val location =
            geoCoder.getFromLocation(homeRoot.lat, homeRoot.lon, 1) as MutableList<Address>
        val loc = location[0].adminArea.toString() + "/" + location[0].countryName.toString()
        binding.cityTextView.text = loc
        val fullDate = homeRoot.dt.toLong() * 1000 + homeRoot.timezone_offset - 7200
        val date = Date(fullDate).toString().split(" ")
        val dateString =
            date[0] + ", " + date[1] + " " + date[2] + ", " + date[3].split(":")[0] + ":" + date[3].split(
                ":"
            )[1]
        binding.dateTextView.text = dateString
        Picasso.get().load(Constants.iconImage(homeRoot.icon)).into(binding.weatherImageView)
        if (temperature.equals("celsius")) {
            binding.temperatureTextView.text = homeRoot.temp.toInt().toString()
            binding.unitTextView.text = "C"
        } else if (temperature.equals("fahrenheit")) {
            binding.temperatureTextView.text = Constants.fromCtoF(homeRoot.temp).toInt().toString()
            binding.unitTextView.text = "F"
        } else {
            binding.temperatureTextView.text = Constants.fromCtoK(homeRoot.temp).toInt().toString()
            binding.unitTextView.text = "K"
        }
        binding.currentDescriptionTextView.text = homeRoot.description
        binding.pressureNumberTextView.text = homeRoot.pressure.toString()
        binding.humidityNumberTextView.text = homeRoot.humidity.toString()
        binding.cloudNumberTextView.text = homeRoot.clouds.toString()
        if (wind.equals("ms")) {
            binding.windNumberTextView.text = homeRoot.wind_speed.toString()
            binding.windUnitTextView.text = "m/s"
        } else {
            binding.windNumberTextView.text =
                DecimalFormat("###.##").format(Constants.fromMStoMH(homeRoot.wind_speed)).toString()
            binding.windUnitTextView.text = "m/h"
        }
        binding.ultraVioletNumberTextView.text = homeRoot.uvi.toString()
        binding.visibilityNumberTextView.text = homeRoot.visibility.toString()
    }
}