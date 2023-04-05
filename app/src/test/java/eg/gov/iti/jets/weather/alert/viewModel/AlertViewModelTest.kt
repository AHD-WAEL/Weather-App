package eg.gov.iti.jets.weather.alert.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import eg.gov.iti.jets.weather.MainRule
import eg.gov.iti.jets.weather.getOrAwaitValue
import eg.gov.iti.jets.weather.home.viewModel.HomeViewModel
import eg.gov.iti.jets.weather.model.CurrentAlert
import eg.gov.iti.jets.weather.model.FakeRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


@RunWith(JUnit4::class)
class AlertViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainRule = MainRule()

    lateinit var viewModel: AlertViewModel
    lateinit var repo: FakeRepository

    @Before
    fun setup() {
        repo = FakeRepository()
        viewModel = AlertViewModel(repo)
    }

    @Test
    fun getAlert_listOfAlerts_getListOfInsertedAlerts(){
        //given
        var alerts = listOf<CurrentAlert>(
            CurrentAlert(1,"America","1/1/2022\n11:11","1/1/2022\n11:11","alert"),
            CurrentAlert(2,"Poland","2/2/2022\n22:22", "2/2/2022\n22:22", "notify"),
            CurrentAlert(3,"Egypt","3/3/2022\n5:55","3/3/2022\n5:55","alert")
        )

        //when
        for (i in alerts)
            viewModel.insertAlert(i)

        //then
        val result = viewModel.alertList.getOrAwaitValue {  }
        assertEquals(alerts, result)
    }

    @Test
    fun insertAlert_insertNewAlert_getInsertedAlert(){
        //given
        var alert = CurrentAlert(1,"America","1/1/2022\n11:11","1/1/2022\n11:11","alert")
        //when
        viewModel.insertAlert(alert)

        //then
        val result = viewModel.alertList.getOrAwaitValue {  }
        assertEquals(alert, result[result.size-1])
    }

    @Test
    fun deleteAlert_insertNewAlert_deleteInsertedAlert(){
        //given
        var alert = CurrentAlert(1,"America","1/1/2022\n11:11","1/1/2022\n11:11","alert")
        viewModel.insertAlert(alert)

        //when
        viewModel.deleteAlert(alert)

        //then
        val result = viewModel.alertList.getOrAwaitValue {  }
        assertEquals(-1, result.indexOf(alert))
    }
}