package com.example.examen2.Others

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.examen2.FavoriteApplication
import com.example.examen2.data.database.FavoriteShow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//Data class representing the status
data class Status(
    val shows: List<Show> = emptyList(),
    val selectedShow: Show? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val favoritesShow: List<FavoriteShow> = emptyList(),
)

//Status class for managing show data
class ViewStatus(private val showsRepository: ShowsRepository) : ViewModel() {
    val status = mutableStateOf(Status())

    //Function to set the loading state
    private fun setLoading(isLoading: Boolean) {
        status.value = status.value.copy(isLoading = isLoading)
    }

    //Function to set the error state
    private fun setError(e: Exception) {
        status.value = status.value.copy(error = e.message)
    }

    //Helper function to manage data loading with error handling
    private fun launchDataLoad(block: suspend () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            setLoading(true)
            try {
                block()
            } catch (e: Exception) {
                setError(e)
            } finally {
                setLoading(false)
            }
        }
    }

    //Function to get shows with a limit (default 50)
    fun getShows(limit: Int = 50) = launchDataLoad {
        status.value = status.value.copy(shows = showsRepository.getShows().take(limit))
    }

    //Function to get a show by its ID
    fun getShowById(id: Int) = launchDataLoad {
        status.value = status.value.copy(selectedShow = showsRepository.getShowById(id))
    }

    //Function to get shows by name
    fun getShowsByName(name: String) = launchDataLoad {
        status.value = status.value.copy(shows = showsRepository.getShowsByName(name))
    }

    //Function to get all favorite shows
    fun getAllFavoriteShows() = viewModelScope.launch(Dispatchers.IO) {
        status.value = status.value.copy(favoritesShow = showsRepository.getAllFavoriteShows())
    }

    //Function to add a show to favorites
    fun addFavoriteShow(favoriteShow: FavoriteShow) = viewModelScope.launch(Dispatchers.IO) {
        showsRepository.addFavoriteShow(favoriteShow)
        getAllFavoriteShows()
        Log.d("TAG", "Todos los favoritos: ${status.value.favoritesShow}")
    }

    //Function to delete a show from favorites
    fun deleteFavoriteShow(favoriteShow: FavoriteShow) = viewModelScope.launch(Dispatchers.IO) {
        showsRepository.removeFavoriteShow(favoriteShow)
        getAllFavoriteShows()
    }

    //Function to check if a show is a favorite
    fun isFavoriteShow(id: Int, onResult: (Boolean) -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        onResult(showsRepository.isFavoriteShow(id))
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                //Retrieve application and repository instances
                val application = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as FavoriteApplication
                val showsRepository = application.container.showsRepository
                ViewStatus(showsRepository)
            }
        }
    }
}
