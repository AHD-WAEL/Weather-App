package eg.gov.iti.jets.weather.home.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import eg.gov.iti.jets.weather.MainRule
import eg.gov.iti.jets.weather.getOrAwaitValue
import eg.gov.iti.jets.weather.model.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


@RunWith(JUnit4::class)
class HomeViewModelTest{
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainRule = MainRule()

    lateinit var viewModel: HomeViewModel
    lateinit var repo: FakeRepository

    @Before
    fun setup()
    {
        repo = FakeRepository()
        viewModel = HomeViewModel(repo)
    }

    @Test
    fun getHomeRootFromDB_insertNewRoot_getInsertedRoot(){
        //given
        val rootData = Root(0, 52.2297, 21.0122, "Europe/Warsaw", 3600,
            Current(1646318698, 1646306882, 1646347929, 282.21, 278.41, 1014, 65, 275.99, 2.55, 40, 10000, 8.75,360, emptyList<Weather>()),
            emptyList<Hourly>(), emptyList<Daily>(), emptyList<Alert>())

        //when
        viewModel.insertHomeRootToDB(rootData)

        //then
        viewModel.getHomeRootFromDB()
        val result = viewModel.home.getOrAwaitValue {  }
        assertEquals(rootData, result)
    }
    @Test
    fun insertHomeRootToDB_insertNewRoot_getInsertedRoot(){
        //given
        val rootData = Root(0, 52.2297, 21.0122, "Europe/Warsaw", 3600,
        Current(1646318698, 1646306882, 1646347929, 282.21, 278.41, 1014, 65, 275.99, 2.55, 40, 10000, 8.75,360, emptyList<Weather>()),
            emptyList<Hourly>(), emptyList<Daily>(), emptyList<Alert>())

        //when
        viewModel.insertHomeRootToDB(rootData)

        //then
        viewModel.getHomeRootFromDB()
        val result = viewModel.home.getOrAwaitValue {  }
        assertEquals(rootData, result)
    }
}