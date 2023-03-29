package eg.gov.iti.jets.weather.home.viewModel

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.*
import eg.gov.iti.jets.weather.model.*
import eg.gov.iti.jets.weather.network.ApiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomeViewModel(private var _repo:RepositoryInterface) : ViewModel(){

    private var _weather = MutableStateFlow<ApiState>(ApiState.Loading)
    val weather = _weather.asStateFlow()

    private var _home: MutableLiveData<Root> = MutableLiveData<Root>()
    val home: LiveData<Root> = _home

    fun getHomeLocation(lat:String, lon:String, lang:String){
        viewModelScope.launch (Dispatchers.IO){
            _repo.getLocation(lat, lon, lang)
                .catch {
                    error -> _weather.value = ApiState.Failure(error)
                }
                .collect{
                _weather.value = ApiState.Success(it)
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

    fun insertHomeRootToDB(root: Root){
        viewModelScope.launch(Dispatchers.IO) {
            _repo.insertRepoHomeRoot(root)
        }
    }
}

class HomeViewModelFactory(private var _repo:RepositoryInterface): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(HomeViewModel::class.java)) HomeViewModel(_repo) as T
        else throw IllegalArgumentException("ViewModel not found")
    }
}