package eg.gov.iti.jets.weather.favourite.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import eg.gov.iti.jets.weather.databinding.FavouriteListBinding
import eg.gov.iti.jets.weather.model.FavoriteLocation

class FavouriteAdapter(var favouriteLocation: List<FavoriteLocation>, var deleteFromFav:(FavoriteLocation) -> Unit, var favToHome:(FavoriteLocation) -> Unit): RecyclerView.Adapter<FavouriteAdapter.ViewHolder>() {

    private lateinit var binding:FavouriteListBinding
    class ViewHolder(var binding: FavouriteListBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var inflater: LayoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = FavouriteListBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
       return favouriteLocation.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.favouriteTextView.text = favouriteLocation[position].name
        holder.binding.deleteImageView.setOnClickListener {
            deleteFromFav(favouriteLocation[position])
        }
        holder.binding.favConstraintLayout.setOnClickListener {
            favToHome(favouriteLocation[position])
        }
    }

}