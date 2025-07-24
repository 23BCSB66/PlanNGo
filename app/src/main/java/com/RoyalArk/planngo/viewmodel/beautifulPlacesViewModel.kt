package com.RoyalArk.planngo.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.RoyalArk.planngo.data.model.UnsplashImage
import com.RoyalArk.planngo.data.network.UnsplashRetrofitInstance
import kotlinx.coroutines.launch

class BeautifulPlacesViewModel : ViewModel() {

    private val _places = MutableLiveData<List<UnsplashImage>>()
    val places: LiveData<List<UnsplashImage>> = _places

    private val unsplashAccessKey = "AYmK2cluE7pNWvZoTwmE8NTH5qfOhHWBdOxbTWsFzG4"

    fun fetchBeautifulPlaces(query: String = "beautiful travel places") {
        viewModelScope.launch {
            try {
                val response = UnsplashRetrofitInstance.api.searchPhotos(query, unsplashAccessKey)
                _places.value = response.results
            } catch (e: Exception) {
                Log.e("BeautifulPlacesVM", "Failed: ${e.message}")
            }
        }
    }
}
