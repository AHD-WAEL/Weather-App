package eg.gov.iti.jets.weather.alert.viewModel

import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.CreationExtras
import eg.gov.iti.jets.weather.model.CurrentAlert
import eg.gov.iti.jets.weather.model.RepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlertViewModel(private var _repo:RepositoryInterface):ViewModel(){

    private var _alertList: MutableLiveData<List<CurrentAlert>> = MutableLiveData<List<CurrentAlert>>()
    val alertList: LiveData<List<CurrentAlert>> = _alertList

    init {
        getAlert()
    }

    private fun getAlert() {
        viewModelScope.launch(Dispatchers.IO) {
            _repo.getRepoAlertLocation().collect{
                _alertList.postValue(it)
            }
        }
    }

    fun insertAlert(currentAlert: CurrentAlert){
        viewModelScope.launch(Dispatchers.IO) {
            _repo.insertRepoAlertLocation(currentAlert)
        }
    }

    fun deleteAlert(currentAlert: CurrentAlert){
        viewModelScope.launch(Dispatchers.IO) {
            _repo.deleteRepoAlertLocation(currentAlert)
        }
    }
}

class AlertViewModelFactory(private var _repo: RepositoryInterface):ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return if(modelClass.isAssignableFrom(AlertViewModel::class.java)) AlertViewModel(_repo) as T
        else throw IllegalArgumentException("View model not found")
    }
}