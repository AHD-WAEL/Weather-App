package eg.gov.iti.jets.weather.model

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import eg.gov.iti.jets.weather.MainRule
import eg.gov.iti.jets.weather.db.FakeLocalSource
import eg.gov.iti.jets.weather.network.WeatherClient
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class RepositoryTest{
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val main = MainRule()

    lateinit var localSource: FakeLocalSource
    lateinit var repo: Repository

    @Before
    fun setup(){
        localSource = FakeLocalSource()
        repo = Repository.getInstance(WeatherClient.getInstance(), localSource)
    }

    @Test
    fun getHomeRoot_insertNewRoot_getInsertedRoot() = main.runBlockingTest {
        //given
        val rootData = Root(0, 52.2297, 21.0122, "Europe/Warsaw", 3600,
            Current(1646318698, 1646306882, 1646347929, 282.21, 278.41, 1014, 65, 275.99, 2.55, 40, 10000, 8.75,360, emptyList<Weather>()),
            emptyList<Hourly>(), emptyList<Daily>(), emptyList<Alert>())

        //when
        repo.insertRepoHomeRoot(rootData)

        var result: Root? = null
        val job = launch{
            repo.getRepoHomeRoot().collect{
                result = it
            }
        }
        job.cancel()

        //then
        assertEquals(rootData.id, result!!.id)
        assertEquals(rootData.lat, result!!.lat,0.0)
        assertEquals(rootData.lon, result!!.lon,0.0)
        assertEquals(rootData, result)
    }

    @Test
    fun insertHomeRoot_insertNewRoot_getInsertedRoot() = main.runBlockingTest{
        //given
        val rootData = Root(0, 52.2297, 21.0122, "Europe/Warsaw", 3600,
            Current(1646318698, 1646306882, 1646347929, 282.21, 278.41, 1014, 65, 275.99, 2.55, 40, 10000, 8.75,360, emptyList<Weather>()),
            emptyList<Hourly>(), emptyList<Daily>(), emptyList<Alert>())

        //when
        repo.insertRepoHomeRoot(rootData)

        var result: Root? = null
        val job = launch{
            repo.getRepoHomeRoot().collect{
                result = it
            }
        }
        job.cancel()

        //then
        assertEquals(rootData.id, result!!.id)
        assertEquals(rootData.lat, result!!.lat,0.0)
        assertEquals(rootData.lon, result!!.lon,0.0)
        assertEquals(rootData, result)
    }

    @Test
    fun getFavoriteLocation_listOfFavouriteLocation_getListOfInsertedFavouriteLocation() = main.runBlockingTest{
        //given
        var favoriteLocation = listOf<FavoriteLocation>(
            FavoriteLocation("America","1","1"),
            FavoriteLocation("Poland","2","2"),
            FavoriteLocation("Egypt","3","3")
        )

        //when
        for (i in favoriteLocation)
            repo.insertRepoFavoriteLocation(i)

        var result: List<FavoriteLocation>? = null
        val job = launch{
            repo.getRepoFavoriteLocation().collect{
                result = it
            }
        }
        job.cancel()

        //then
        assertEquals(favoriteLocation[0].lat, result!![0].lat)
        assertEquals(favoriteLocation[1].lon, result!![1].lon)
        assertEquals(favoriteLocation[2].name, result!![2].name)
        assertEquals(favoriteLocation, result)
    }

    @Test
    fun insertFavoriteLocation_insertNewFavouriteLocation_getInsertedFavouriteLocation() = main.runBlockingTest{
        //given
        var favoriteLocation = FavoriteLocation("Greek","4","4")

        //when
        repo.insertRepoFavoriteLocation(favoriteLocation)

        var result: List<FavoriteLocation>? = null
        val job = launch{
            repo.getRepoFavoriteLocation().collect{
                result = it
            }
        }
        job.cancel()

        //then
        assertEquals(favoriteLocation.lat, result!![result!!.size-1].lat)
        assertEquals(favoriteLocation.lon, result!![result!!.size-1].lon)
        assertEquals(favoriteLocation.name, result!![result!!.size-1].name)
        assertEquals(favoriteLocation, result?.get(result!!.size-1))
    }

    @Test
    fun deleteFavoriteLocation_insertNewFavouriteLocation_deleteInsertedFavouriteLocation() = main.runBlockingTest{
        //given
        var favoriteLocation = FavoriteLocation("America","1","1")

        //when
        repo.insertRepoFavoriteLocation(favoriteLocation)
        repo.deleteRepoFavoriteLocation(favoriteLocation)

        var result: List<FavoriteLocation>? = null
        val job = launch{
            repo.getRepoFavoriteLocation().collect{
                result = it
            }
        }
        job.cancel()

        //then
        assertEquals(-1, result!!.indexOf(favoriteLocation))
    }

    @Test
    fun getAlertLocation_listOfAlerts_getListOfInsertedAlerts() = main.runBlockingTest{
        //given
        var alerts = listOf<CurrentAlert>(
            CurrentAlert(1,"America","1/1/2022\n11:11"),
            CurrentAlert(2,"Poland","2/2/2022\n22:22"),
            CurrentAlert(3,"Egypt","3/3/2022\n5:55")
        )

        //when
        for (i in alerts)
            repo.insertRepoAlertLocation(i)

        //then
        var result: List<CurrentAlert>? = null
        val job = launch{
            repo.getRepoAlertLocation().collect{
                result = it
            }
        }
        job.cancel()

        //then
        assertEquals(alerts[0].id, result!![0].id)
        assertEquals(alerts[1].countryName, result!![1].countryName)
        assertEquals(alerts[2].dateAndTime, result!![2].dateAndTime)
        assertEquals(alerts, result)
    }

    @Test
    fun insertAlertLocation_insertNewAlert_getInsertedAlert() = main.runBlockingTest{
        //given
        var alert = CurrentAlert(1,"America","1/1/2022\n11:11")

        //when
        repo.insertRepoAlertLocation(alert)

        var result: List<CurrentAlert>? = null
        val job = launch{
            repo.getRepoAlertLocation().collect{
                result = it
            }
        }
        job.cancel()

        //then
        assertEquals(alert.id, result!![0].id)
        assertEquals(alert.countryName, result!![0].countryName)
        assertEquals(alert.dateAndTime, result!![0].dateAndTime)
        assertEquals(alert, result?.get(result!!.size-1))
    }

    @Test
    fun deleteAlertLocation_insertNewAlert_deleteInsertedAlert() = main.runBlockingTest{
        //given
        var alert = CurrentAlert(1,"America","1/1/2022\n11:11")

        //when
        repo.insertRepoAlertLocation(alert)
        repo.deleteRepoAlertLocation(alert)

        var result: List<CurrentAlert>? = null
        val job = launch{
            repo.getRepoAlertLocation().collect{
                result = it
            }
        }
        job.cancel()

        //then
        assertEquals(-1, result!!.indexOf(alert))
    }
}