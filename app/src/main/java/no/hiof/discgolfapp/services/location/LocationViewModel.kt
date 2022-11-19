package no.hiof.discgolfapp.services.location

import android.annotation.SuppressLint
import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.*
import kotlin.time.Duration.Companion.minutes

class SharedLocationViewModel : ViewModel() {

    private lateinit var _fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private val _currentLocation = MutableLiveData<Location>()
    var currentLocation: LiveData<Location> = _currentLocation

    // default location as HiO if no location is found
    private val defaultLocation = Location("default").apply {
        latitude = 59.1293
        longitude = 11.3528
    }

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
            LocationRequest.Builder(10.minutes.inWholeMilliseconds)
                .setPriority(Priority.PRIORITY_HIGH_ACCURACY).build()
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
            .addOnSuccessListener { currentLocation: Location? ->
                if (currentLocation != null) {
                    _currentLocation.value = currentLocation
                    Log.d(
                        TAG,
                        "Current Location: ${this.currentLocation.value?.latitude}, ${this.currentLocation.value?.longitude}"
                    )
                } else {
                    fusedLocationClient.lastLocation
                        .addOnSuccessListener { lastLocation ->
                            if (lastLocation != null) {
                                _currentLocation.value = lastLocation
                                Log.d(
                                    TAG,
                                    "last location set as current location: ${this.currentLocation.value?.latitude}, ${this.currentLocation.value?.longitude}"
                                )
                            } else {
                                setCurrentLocationAsDefaultLocation()
                            }
                        }
                        .addOnFailureListener {
                            setCurrentLocationAsDefaultLocation()
                        }
                }
            }
            .addOnFailureListener {
                setCurrentLocationAsDefaultLocation()
            }
    }

    fun setCurrentLocationAsDefaultLocation() {
        Log.d(
            TAG,
            "Failed to get current location, setting location as default location: ${defaultLocation.latitude}, ${defaultLocation.longitude}"
        )
        _currentLocation.value = defaultLocation
    }
}