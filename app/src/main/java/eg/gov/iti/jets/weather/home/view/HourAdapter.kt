package eg.gov.iti.jets.weather.home.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import eg.gov.iti.jets.weather.Constants
import eg.gov.iti.jets.weather.databinding.HourTemperatureBinding
import eg.gov.iti.jets.weather.model.SpecificTime

class HourAdapter (private var hourList: List<SpecificTime>): RecyclerView.Adapter<HourAdapter.ViewHolder>(){
    private lateinit var binding: HourTemperatureBinding

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
        holder.binding.temperatureTextView.text= hourList[position].temperature.toInt().toString()
        Picasso.get().load(Constants.getImage(hourList[position].img)).into(holder.binding.imageView)
    }
}