package no.hiof.discgolfapp.services.location

import android.annotation.SuppressLint
import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.*

class SharedLocationViewModel : ViewModel() {

    private lateinit var _fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private val _currentLocation = MutableLiveData<Location>()
    var currentLocation: LiveData<Location> = _currentLocation

    private val TAG = "SharedLocationViewModel"

    @SuppressLint("MissingPermission")
    fun setCurrentLocation(fusedLocationClient: FusedLocationProviderClient) {
        _fusedLocationClient = fusedLocationClient
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                _currentLocation.value = locationResult.lastLocation
            }

        }
        locationRequest =
            LocationRequest.Builder(600).setPriority(Priority.PRIORITY_HIGH_ACCURACY).build()
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                _currentLocation.value = location
                Log.d(
                    TAG,
                    "Current Location: ${currentLocation.value?.latitude}, ${currentLocation.value?.longitude}"
                )
            }
        }
    }


}