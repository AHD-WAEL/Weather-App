package eg.gov.iti.jets.weather.home.viewModel

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.*
import eg.gov.iti.jets.weather.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomeViewModel(private var _repo:RepositoryInterface) : ViewModel(){

    private var _weather: MutableLiveData<Root> = MutableLiveData<Root>()
    val weather: LiveData<Root> = _weather

    private var _home: MutableLiveData<HomeRoot> = MutableLiveData<HomeRoot>()
    val home: LiveData<HomeRoot> = _home

    private var _day: MutableLiveData<List<SpecificDay>> = MutableLiveData<List<SpecificDay>>()
    val day: LiveData<List<SpecificDay>> = _day

    private var _hour: MutableLiveData<List<SpecificTime>> = MutableLiveData<List<SpecificTime>>()
    val hour: LiveData<List<SpecificTime>> = _hour

    fun getHomeLocation(lat:String, lon:String, lang:String){
        viewModelScope.launch (Dispatchers.IO){
            _repo.getLocation(lat, lon, lang).collect{
                _weather.postValue(it)
            }
        }
    }

    fun getHomeRootFromDB(){
        viewModelScope.launch(Dispatchers.IO) {
            _repo.getRepoHomeRoot().collect{
                _home.postValue(it)
            }
        }
    }

    fun insertHomeRootToDB(homeRoot: HomeRoot){
        viewModelScope.launch(Dispatchers.IO) {
            _repo.insertRepoHomeRoot(homeRoot)
        }
    }

    fun getDayFromDB(){
        viewModelScope.launch(Dispatchers.IO) {
            _repo.getRepoSpecificDay().collect{
                _day.postValue(it)
            }
        }
    }

    fun insertDayToDB(specificDay: SpecificDay){
        viewModelScope.launch(Dispatchers.IO) {
            _repo.insertRepoSpecificDay(specificDay)
        }
    }

    fun getHourFromDB(){
        viewModelScope.launch(Dispatchers.IO) {
            _repo.getRepoSpecificTime().collect{
                _hour.postValue(it)
            }
        }
    }

    fun insertHourToDB(specificTime: SpecificTime){
        viewModelScope.launch(Dispatchers.IO) {
            _repo.insertRepoSpecificTime(specificTime)
        }
    }
}

class HomeViewModelFactory(private var _repo:RepositoryInterface): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(HomeViewModel::class.java)) HomeViewModel(_repo) as T
        else throw IllegalArgumentException("ViewModel not found")
    }
}