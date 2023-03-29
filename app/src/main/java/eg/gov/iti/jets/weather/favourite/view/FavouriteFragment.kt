package eg.gov.iti.jets.weather.favourite.view

import android.content.Context
import android.content.SharedPreferences
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.android.material.snackbar.Snackbar
import eg.gov.iti.jets.weather.Constants
import eg.gov.iti.jets.weather.R
import eg.gov.iti.jets.weather.databinding.FragmentFavouriteBinding
import eg.gov.iti.jets.weather.db.ConcreteLocalSource
import eg.gov.iti.jets.weather.favourite.viewModel.FavouriteViewModel
import eg.gov.iti.jets.weather.favourite.viewModel.FavouriteViewModelFactory
import eg.gov.iti.jets.weather.home.view.HomeFragment
import eg.gov.iti.jets.weather.model.FavoriteLocation
import eg.gov.iti.jets.weather.model.Repository
import eg.gov.iti.jets.weather.network.API_Service
import eg.gov.iti.jets.weather.network.WeatherClient

class FavouriteFragment : Fragment() {

    private lateinit var binding: FragmentFavouriteBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var geoCoder: Geocoder
    private lateinit var favouriteViewModel: FavouriteViewModel
    private lateinit var favouriteViewModelFactory: FavouriteViewModelFactory
    private lateinit var favouriteAdapter: FavouriteAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = requireContext().getSharedPreferences(Constants.locationPreferences, Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()

        val lat = sharedPreferences.getString("lat", "none")
        val lon = sharedPreferences.getString("lon", "none")

        favouriteViewModelFactory = FavouriteViewModelFactory(Repository.getInstance(WeatherClient.getInstance(), ConcreteLocalSource(requireContext().applicationContext)))
        favouriteViewModel = ViewModelProvider(this, favouriteViewModelFactory).get(FavouriteViewModel::class.java)

        if(lat != "none" && lon != "none")
        {
            geoCoder = Geocoder(requireContext())
            val location = geoCoder.getFromLocation(lat!!.toDouble(), lon!!.toDouble(), 1) as MutableList<Address>
            val loc = location[0].adminArea.toString()+"/"+location[0].countryName.toString()
            favouriteViewModel.insertFavouriteLocation(FavoriteLocation(loc, lat, lon))
            editor.putString("lat", "none")
            editor.putString("lon", "none")
            editor.commit()
        }
        favouriteViewModel.favourite.observe(viewLifecycleOwner){ favoriteLocations ->
            favouriteAdapter = FavouriteAdapter(favoriteLocations,
                {
                    favouriteViewModel.deleteFavouriteLocation(it)
                },
                {
                    if(Constants.checkForInternet(requireContext().applicationContext)){
                        sharedPreferences = requireContext().getSharedPreferences(Constants.FavPreferences, Context.MODE_PRIVATE)
                        editor = sharedPreferences.edit()
                        editor.putString("lat",it.lat)
                        editor.putString("lon", it.lon)
                        editor.putString("source", "fav")
                        editor.commit()
                        Navigation.findNavController(view).navigate(R.id.home)
                    }
                    else
                        Snackbar.make(view,"Check your internet connection", Snackbar.LENGTH_LONG)
                            .setBackgroundTint(resources.getColor(R.color.purple_700)).show()
                }
            )
            binding.favouriteRecyclerView.adapter = favouriteAdapter
            favouriteAdapter.notifyDataSetChanged()
        }

        binding.favouriteFloatingActionButton.setOnClickListener {
            if(Constants.checkForInternet(requireContext().applicationContext))
                Navigation.findNavController(it).navigate(R.id.mapFragment)
            else
                Snackbar.make(view,"Check your internet connection", Snackbar.LENGTH_LONG)
                    .setBackgroundTint(resources.getColor(R.color.purple_700)).show()
        }
    }
}