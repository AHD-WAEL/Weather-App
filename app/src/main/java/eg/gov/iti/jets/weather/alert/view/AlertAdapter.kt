package eg.gov.iti.jets.weather.alert.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import eg.gov.iti.jets.weather.databinding.AlertListBinding
import eg.gov.iti.jets.weather.model.CurrentAlert

class AlertAdapter (private var alertLocation: List<CurrentAlert>, var deleteFromAlert:(CurrentAlert) -> Unit): RecyclerView.Adapter<AlertAdapter.ViewHolder>() {

    private lateinit var binding: AlertListBinding
    class ViewHolder(var binding: AlertListBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = AlertListBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return alertLocation.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.alertCountryTextView.text = alertLocation[position].countryName
        holder.binding.alertDateAndTimeFromTextView.text = alertLocation[position].fromDateAndTime
        holder.binding.alertDateAndTimeToTextView.text = alertLocation[position].toDateAndTime
        holder.binding.deleteImageView.setOnClickListener {
            deleteFromAlert(alertLocation[position])
        }
    }

}