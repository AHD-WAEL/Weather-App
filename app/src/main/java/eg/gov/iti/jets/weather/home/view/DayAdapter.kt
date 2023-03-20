package eg.gov.iti.jets.weather.home.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import eg.gov.iti.jets.weather.Constants
import eg.gov.iti.jets.weather.databinding.DayTemperatureBinding
import eg.gov.iti.jets.weather.model.SpecificDay

class DayAdapter(private var dayList: List<SpecificDay>): RecyclerView.Adapter<DayAdapter.ViewHolder>() {
    private lateinit var binding: DayTemperatureBinding

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
        holder.binding.smallTemperatureTextView.text = dayList[position].lowest.toInt().toString()
        holder.binding.largeTemperatureTextView.text = dayList[position].highest.toInt().toString()
        Picasso.get().load(Constants.getImage(dayList[position].img)).into(holder.binding.imageView)
    }
}