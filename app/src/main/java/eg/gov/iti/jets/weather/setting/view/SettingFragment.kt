package eg.gov.iti.jets.weather.setting.view

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import eg.gov.iti.jets.weather.Constants
import eg.gov.iti.jets.weather.databinding.FragmentSettingBinding

class SettingFragment : Fragment() {

    private lateinit var binding: FragmentSettingBinding

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

        val setting: SharedPreferences = requireContext().getSharedPreferences(Constants.settingPreferences, Context.MODE_PRIVATE)
        val settings: SharedPreferences.Editor = setting.edit()

        binding.gpsRadioButton.setOnClickListener {
            settings.putString("location", "gps")
            settings.commit()
        }

        binding.mapRadioButton.setOnClickListener {
            settings.putString("location", "map")
            settings.commit()
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

        if(lang.equals("ar")) binding.arabicRadioButton.isChecked = true
        else binding.englishRadioButton.isChecked = true

        if(wind.equals("ms")) binding.msRadioButton.isChecked = true
        else binding.mhRadioButton.isChecked = true

        if(notification.equals("notify")) binding.notificationRadioButton.isChecked = true
        else binding.alertRadioButton.isChecked = true

        if(temperature.equals("celsius")) binding.celsiusRadioButton.isChecked = true
        else if(temperature.equals("fahrenheit")) binding.fahrenheitRadioButton.isChecked = true
        else binding.kelvinRadioButton.isChecked = true

        println("$location $lang $wind $notification $temperature")
    }
}