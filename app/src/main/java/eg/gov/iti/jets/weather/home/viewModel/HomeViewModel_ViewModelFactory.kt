package eg.gov.iti.jets.weather.home.viewModel

import androidx.lifecycle.*
import eg.gov.iti.jets.weather.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomeViewModel(private var _repo:RepositoryInterface, lat:String, lon:String, lang:String, var connection: Boolean) : ViewModel(){

    init {
        if(connection)
        {
            getHomeLocation(lat, lon, lang)
        }
        else
        {
            getHomeRootFromDB()
            getDayFromDB()
            getHourFromDB()
        }
    }

    private var _weather: MutableLiveData<Root> = MutableLiveData<Root>()
    val weather: LiveData<Root> = _weather

    private var _home: MutableLiveData<HomeRoot> = MutableLiveData<HomeRoot>()
    val home: LiveData<HomeRoot> = _home

    private var _day: MutableLiveData<List<SpecificDay>> = MutableLiveData<List<SpecificDay>>()
    val day: LiveData<List<SpecificDay>> = _day

    private var _hour: MutableLiveData<List<SpecificTime>> = MutableLiveData<List<SpecificTime>>()
    val hour: LiveData<List<SpecificTime>> = _hour

    private fun getHomeLocation(lat:String, lon:String, lang:String){
        viewModelScope.launch (Dispatchers.IO){
            _repo.getLocation(lat, lon, lang).collect{
                _weather.postValue(it)
            }
        }
    }

    private fun getHomeRootFromDB(){
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

    private fun getDayFromDB(){
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

    private fun getHourFromDB(){
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

class HomeViewModelFactory(private var _repo:RepositoryInterface, private var lat:String, private var lon:String, private var lang:String, private var connection: Boolean): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(HomeViewModel::class.java)) HomeViewModel(_repo, lat, lon, lang, connection) as T
        else throw IllegalArgumentException("ViewModel not found")
    }
}