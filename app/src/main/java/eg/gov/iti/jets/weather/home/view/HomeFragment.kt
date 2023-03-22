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
import eg.gov.iti.jets.weather.databinding.FragmentHomeBinding
import eg.gov.iti.jets.weather.home.viewModel.HomeViewModel
import eg.gov.iti.jets.weather.home.viewModel.HomeViewModelFactory
import eg.gov.iti.jets.weather.model.Repository
import eg.gov.iti.jets.weather.model.Root
import eg.gov.iti.jets.weather.model.SpecificDay
import eg.gov.iti.jets.weather.model.SpecificTime
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
        homeViewModelFactory = HomeViewModelFactory(Repository.getInstance(WeatherClient.getInstance()),"33.44","-94.04", lang.toString())
        homeViewModel = ViewModelProvider(this,homeViewModelFactory)[HomeViewModel::class.java]
        homeViewModel.weather.observe(viewLifecycleOwner){
            hourAdapter = HourAdapter(SpecificTime.getSpecificTime(it), requireContext().applicationContext)
            binding.hourHomeRecyclerView.adapter = hourAdapter
            hourAdapter.notifyDataSetChanged()
            dayAdapter = DayAdapter(SpecificDay.getSpecificDay(it), requireContext().applicationContext)
            binding.dayHomeRecyclerView.adapter = dayAdapter
            hourAdapter.notifyDataSetChanged()
            initializeUI(it, temperature.toString(), wind.toString())
        }
    }

    private fun initializeUI(root: Root, temperature:String, wind:String) {
        val location = geoCoder.getFromLocation(root.lat, root.lon, 1) as MutableList<Address>
        val loc = location[0].adminArea.toString()+"/"+location[0].countryName.toString()
        binding.cityTextView.text = loc
        val fullDate = root.current.dt.toLong() * 1000 + root.timezone_offset - 7200
        val date = Date(fullDate).toString().split(" ")
        val dateString = date[0]+ ", "+ date[1]+ " "+ date[2]+ ", "+ date[3].split(":")[0]+":"+date[3].split(":")[1]
        binding.dateTextView.text = dateString
        Picasso.get().load(Constants.getImage(root.current.weather[0].icon)).into(binding.weatherImageView)
        if(temperature.equals("celsius"))
        {
            binding.temperatureTextView.text = root.current.temp.toInt().toString()
            binding.unitTextView.text = "C"
        }
        else if(temperature.equals("fahrenheit"))
        {
            binding.temperatureTextView.text = Constants.fromCtoF(root.current.temp).toInt().toString()
            binding.unitTextView.text = "F"
        }
        else
        {
            binding.temperatureTextView.text = Constants.fromCtoK(root.current.temp).toInt().toString()
            binding.unitTextView.text = "K"
        }
        binding.pressureNumberTextView.text = root.current.pressure.toString()
        binding.humidityNumberTextView.text = root.current.humidity.toString()
        binding.cloudNumberTextView.text = root.current.clouds.toString()
        if(wind.equals("ms"))
        {
            binding.windNumberTextView.text = root.current.wind_speed.toString()
            binding.windUnitTextView.text = "m/s"
        }
        else
        {
            binding.windNumberTextView.text = DecimalFormat("##.##").format(Constants.fromMStoMH(root.current.wind_speed)).toString()
            binding.windUnitTextView.text = "m/h"
        }
        binding.ultraVioletNumberTextView.text = root.current.uvi.toString()
        binding.visibilityNumberTextView.text = root.current.visibility.toString()
    }
}