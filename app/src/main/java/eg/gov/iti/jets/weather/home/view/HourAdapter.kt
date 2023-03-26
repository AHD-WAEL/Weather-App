package eg.gov.iti.jets.weather.home.view

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import eg.gov.iti.jets.weather.Constants
import eg.gov.iti.jets.weather.databinding.HourTemperatureBinding
import eg.gov.iti.jets.weather.model.SpecificTime

class HourAdapter (private var hourList: List<SpecificTime>, context: Context): RecyclerView.Adapter<HourAdapter.ViewHolder>(){
    private lateinit var binding: HourTemperatureBinding
    private val setting: SharedPreferences = context.getSharedPreferences(Constants.settingPreferences, Context.MODE_PRIVATE)
    val temperature = setting.getString("temperature", "N/A")

    class ViewHolder(var binding: HourTemperatureBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = HourTemperatureBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return hourList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.hourTextView.text = hourList[position].specificTime
        Picasso.get().load(Constants.iconImage(hourList[position].img)).into(holder.binding.imageView)

        if(temperature.equals("celsius"))
        {
            holder.binding.temperatureTextView.text = hourList[position].temperature.toInt().toString()
            binding.unitTextView.text = "C"
        }
        else if(temperature.equals("fahrenheit"))
        {
            binding.temperatureTextView.text = Constants.fromCtoF(hourList[position].temperature).toInt().toString()
            binding.unitTextView.text = "F"
        }
        else
        {
            binding.temperatureTextView.text = Constants.fromCtoK(hourList[position].temperature).toInt().toString()
            binding.unitTextView.text = "K"
        }
    }
}