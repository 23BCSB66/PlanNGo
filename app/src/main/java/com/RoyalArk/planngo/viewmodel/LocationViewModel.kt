package com.RoyalArk.planngo.viewmodel

import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.LocationServices
import java.util.Locale

class LocationViewModel : ViewModel() {
    private val _location = MutableLiveData<Location?>()
    val location: LiveData<Location?> = _location

    private val _cityName = MutableLiveData<String>()
    val cityName: LiveData<String> = _cityName

    fun fetchLocation(context: Context, onSuccess: (() -> Unit)? = null) {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        if (ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { loc: Location? ->
                    _location.value = loc
                    loc?.let {
                        updateCityName(context, it)
                    }
                    onSuccess?.invoke()
                }
        }
    }

    private fun updateCityName(context: Context, location: Location) {
        try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addressList = geocoder.getFromLocation(location.latitude, location.longitude, 1)
            val city = addressList?.firstOrNull()?.locality ?: "Unknown City"
            _cityName.value = city
        } catch (e: Exception) {
            _cityName.value = "Unknown City"
        }
    }
}
