package eg.gov.iti.jets.weather.favourite.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import eg.gov.iti.jets.weather.MainRule
import eg.gov.iti.jets.weather.getOrAwaitValue
import eg.gov.iti.jets.weather.model.FakeRepository
import eg.gov.iti.jets.weather.model.FavoriteLocation
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class FavouriteViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainRule = MainRule()

    lateinit var viewModel: FavouriteViewModel
    lateinit var repo: FakeRepository

    @Before
    fun setup() {
        repo = FakeRepository()
        viewModel = FavouriteViewModel(repo)
    }

    @Test
    fun getFavouriteLocation_listOfFavouriteLocation_getListOfInsertedFavouriteLocation(){
        //given
        var favoriteLocation = listOf<FavoriteLocation>(
                FavoriteLocation("America","1","1"),
                FavoriteLocation("Poland","2","2"),
                FavoriteLocation("Egypt","3","3")
           )

        //when
        for (i in favoriteLocation)
            viewModel.insertFavouriteLocation(i)

        //then
        val result = viewModel.favourite.getOrAwaitValue {  }
        assertEquals(favoriteLocation, result)
    }

    @Test
    fun insertFavouriteLocation_insertNewFavouriteLocation_getInsertedFavouriteLocation(){
        //given
        var favoriteLocation = FavoriteLocation("Europe/Warsaw","52.2297", "21.0122")
        //when
        viewModel.insertFavouriteLocation(favoriteLocation)

        //then
        val result = viewModel.favourite.getOrAwaitValue {  }
        assertEquals(favoriteLocation, result[result.size-1])
    }

    @Test
    fun deleteFavouriteLocation_insertNewFavouriteLocation_deleteInsertedFavouriteLocation(){
        //given
        var favoriteLocation = FavoriteLocation("Europe/Warsaw","52.2297", "21.0122")
        viewModel.insertFavouriteLocation(favoriteLocation)

        //when
        viewModel.deleteFavouriteLocation(favoriteLocation)

        //then
        val result = viewModel.favourite.getOrAwaitValue {  }
        assertEquals(-1, result.indexOf(favoriteLocation))
    }
}