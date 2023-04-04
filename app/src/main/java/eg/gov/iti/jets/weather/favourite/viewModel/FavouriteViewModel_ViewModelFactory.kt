package eg.gov.iti.jets.weather.favourite.viewModel

import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.CreationExtras
import eg.gov.iti.jets.weather.model.FavoriteLocation
import eg.gov.iti.jets.weather.model.RepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavouriteViewModel(private var _repo :RepositoryInterface):ViewModel(){

    private var _favourite: MutableLiveData<List<FavoriteLocation>> = MutableLiveData<List<FavoriteLocation>>()
    val favourite:LiveData<List<FavoriteLocation>> = _favourite

    init {
        getFavouriteLocation()
    }

    private fun getFavouriteLocation() {
        viewModelScope.launch(Dispatchers.IO) {
            _repo.getRepoFavoriteLocation().collect{
                _favourite.postValue(it)
            }
        }
    }

    fun insertFavouriteLocation(favoriteLocation: FavoriteLocation){
        viewModelScope.launch(Dispatchers.IO) {
            _repo.insertRepoFavoriteLocation(favoriteLocation)
            getFavouriteLocation()
        }
    }

    fun deleteFavouriteLocation(favoriteLocation: FavoriteLocation){
        viewModelScope.launch(Dispatchers.IO) {
            _repo.deleteRepoFavoriteLocation(favoriteLocation)
            getFavouriteLocation()
        }
    }
}

class FavouriteViewModelFactory(private var _repo: RepositoryInterface): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return if(modelClass.isAssignableFrom(FavouriteViewModel::class.java)) FavouriteViewModel(_repo) as T
        else throw IllegalArgumentException("ViewModel not found")
    }
}