package eg.gov.iti.jets.weather.home.viewModel

import androidx.lifecycle.*
import eg.gov.iti.jets.weather.model.RepositoryInterface
import eg.gov.iti.jets.weather.model.Root
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(private var _repo:RepositoryInterface, lat:String, lon:String) : ViewModel(){

    init {
        getHomeLocation(lat, lon)
    }

    private var _weather: MutableLiveData<Root> = MutableLiveData<Root>()
    val weather: LiveData<Root> = _weather

    private fun getHomeLocation(lat:String, lon:String){
        viewModelScope.launch (Dispatchers.IO){
            _repo.getLocation(lat, lon).collect{
                _weather.postValue(it)
            }
        }
    }
}

class HomeViewModelFactory(private var _repo:RepositoryInterface, private var lat:String, private var lon:String): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(HomeViewModel::class.java)) HomeViewModel(_repo, lat, lon) as T
        else throw IllegalArgumentException("ViewModel not found")
    }
}