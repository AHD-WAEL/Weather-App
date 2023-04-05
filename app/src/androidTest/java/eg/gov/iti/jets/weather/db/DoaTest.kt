package eg.gov.iti.jets.weather.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.*
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import eg.gov.iti.jets.weather.MainRule
import eg.gov.iti.jets.weather.model.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class DoaTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val main = MainRule()

    lateinit var db:HomeDB

    @Before
    fun createDataBase(){
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            HomeDB::class.java
        ).build()
    }

    @After
    fun closeDataBase() = db.close()

    @Test
    fun getHomeRoot_insertNewRoot_getInsertedRoot() = main.runBlockingTest {
        //given
        val rootData = Root(0, 52.2297, 21.0122, "Europe/Warsaw", 3600,
            Current(1646318698, 1646306882, 1646347929, 282.21, 278.41, 1014, 65, 275.99, 2.55, 40, 10000, 8.75,360, emptyList<Weather>()),
            emptyList<Hourly>(), emptyList<Daily>(), emptyList<Alert>())

        //when
        db.getHomeDB().insertHomeRoot(rootData)

        var result: Root? = null
        val job = launch{
            db.getHomeDB().getHomeRoot().collect{
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
        db.getHomeDB().insertHomeRoot(rootData)

        var result: Root? = null
        val job = launch{
            db.getHomeDB().getHomeRoot().collect{
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
            db.getHomeDB().insertFavoriteLocation(i)

        var result: List<FavoriteLocation>? = null
        val job = launch{
            db.getHomeDB().getFavoriteLocation().collect{
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
        var favoriteLocation = FavoriteLocation("America","1","1")

        //when
        db.getHomeDB().insertFavoriteLocation(favoriteLocation)

        var result: List<FavoriteLocation>? = null
        val job = launch{
            db.getHomeDB().getFavoriteLocation().collect{
                result = it
            }
        }
        job.cancel()

        //then
        assertEquals(favoriteLocation.lat, result!![0].lat)
        assertEquals(favoriteLocation.lon, result!![0].lon)
        assertEquals(favoriteLocation.name, result!![0].name)
        assertEquals(favoriteLocation, result?.get(result!!.size-1))
    }

    @Test
    fun deleteFavoriteLocation_insertNewFavouriteLocation_deleteInsertedFavouriteLocation() = main.runBlockingTest{
        //given
        var favoriteLocation = FavoriteLocation("America","1","1")

        //when
        db.getHomeDB().insertFavoriteLocation(favoriteLocation)
        db.getHomeDB().deleteFavoriteLocation(favoriteLocation)

        var result: List<FavoriteLocation>? = null
        val job = launch{
            db.getHomeDB().getFavoriteLocation().collect{
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
            CurrentAlert(1,"America","1/1/2022\n11:11","1/1/2022\n11:11","alert"),
            CurrentAlert(2,"Poland","2/2/2022\n22:22", "2/2/2022\n22:22", "notify"),
            CurrentAlert(3,"Egypt","3/3/2022\n5:55","3/3/2022\n5:55","alert")
        )

        //when
        for (i in alerts)
            db.getHomeDB().insertAlertLocation(i)

        //then
        var result: List<CurrentAlert>? = null
        val job = launch{
            db.getHomeDB().getAlertLocation().collect{
                result = it
            }
        }
        job.cancel()

        //then
        assertEquals(alerts[0].id, result!![0].id)
        assertEquals(alerts[1].countryName, result!![1].countryName)
        assertEquals(alerts[2].fromDateAndTime, result!![2].fromDateAndTime)
        assertEquals(alerts, result)
    }

    @Test
    fun insertAlertLocation_insertNewAlert_getInsertedAlert() = main.runBlockingTest{
        //given
        var alert = CurrentAlert(1,"America","1/1/2022\n11:11","1/1/2022\n11:11","alert")

        //when
        db.getHomeDB().insertAlertLocation(alert)

        var result: List<CurrentAlert>? = null
        val job = launch{
            db.getHomeDB().getAlertLocation().collect{
                result = it
            }
        }
        job.cancel()

        //then
        assertEquals(alert.id, result!![0].id)
        assertEquals(alert.countryName, result!![0].countryName)
        assertEquals(alert.toDateAndTime, result!![0].toDateAndTime)
        assertEquals(alert, result?.get(result!!.size-1))
    }

    @Test
    fun deleteAlertLocation_insertNewAlert_deleteInsertedAlert() = main.runBlockingTest{
        //given
        var alert = CurrentAlert(1,"America","1/1/2022\n11:11","1/1/2022\n11:11","alert")

        //when
        db.getHomeDB().insertAlertLocation(alert)
        db.getHomeDB().deleteAlertLocation(alert)

        var result: List<CurrentAlert>? = null
        val job = launch{
            db.getHomeDB().getAlertLocation().collect{
                result = it
            }
        }
        job.cancel()

        //then
        assertEquals(-1, result!!.indexOf(alert))
    }
}