package eg.gov.iti.jets.weather.home.view

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import eg.gov.iti.jets.weather.Constants
import eg.gov.iti.jets.weather.databinding.DayTemperatureBinding
import eg.gov.iti.jets.weather.model.SpecificDay

class DayAdapter(private var dayList: List<SpecificDay>,context: Context): RecyclerView.Adapter<DayAdapter.ViewHolder>() {
    private lateinit var binding: DayTemperatureBinding
    private val setting: SharedPreferences = context.getSharedPreferences(Constants.settingPreferences, Context.MODE_PRIVATE)
    val temperature = setting.getString("temperature", "N/A")
    class ViewHolder(var binding: DayTemperatureBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = DayTemperatureBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dayList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.dayTextView.text = dayList[position].day
        holder.binding.descriptionTextView.text = dayList[position].description
        Picasso.get().load(Constants.getImage(dayList[position].img)).into(holder.binding.imageView)

        if(temperature.equals("celsius"))
        {
            binding.smallTemperatureTextView.text = dayList[position].lowest.toInt().toString()
            binding.smallUnitTextView.text = "C"
            binding.largeTemperatureTextView.text = dayList[position].highest.toInt().toString()
            binding.largeUnitTextView.text = "C"
        }
        else if(temperature.equals("fahrenheit"))
        {
            binding.smallTemperatureTextView.text = Constants.fromCtoF(dayList[position].lowest).toInt().toString()
            binding.smallUnitTextView.text = "F"
            binding.largeTemperatureTextView.text = Constants.fromCtoF(dayList[position].highest).toInt().toString()
            binding.largeUnitTextView.text = "F"
        }
        else
        {
            binding.smallTemperatureTextView.text = Constants.fromCtoK(dayList[position].lowest).toInt().toString()
            binding.smallUnitTextView.text = "K"
            binding.largeTemperatureTextView.text = Constants.fromCtoK(dayList[position].highest).toInt().toString()
            binding.largeUnitTextView.text = "K"
        }
    }
}