package com.RoyalArk.planngo.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.RoyalArk.planngo.data.model.LocationSuggestion
import com.RoyalArk.planngo.data.network.NominatimRetrofitInstance.api
import kotlinx.coroutines.launch

class LocationSuggestionViewModel : ViewModel() {
    var suggestions by mutableStateOf<List<LocationSuggestion>>(emptyList())
        private set

    fun fetchSuggestions(query: String) {
        viewModelScope.launch {
            if (query.length < 3) {
                suggestions = emptyList()
                return@launch
            }

            try {
                val results = api.getSuggestions(query)
                suggestions = results
            } catch (e: Exception) {
                suggestions = emptyList()
            }
        }
    }

    fun clearSuggestions() {
        suggestions = emptyList()
    }
}