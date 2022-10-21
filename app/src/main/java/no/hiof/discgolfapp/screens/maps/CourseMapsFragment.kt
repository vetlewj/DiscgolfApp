package no.hiof.discgolfapp.screens.maps

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import no.hiof.discgolfapp.R

class CourseMapsFragment : Fragment() {
    private val TAG = "CourseMapsFragment"
    private lateinit var map: GoogleMap

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    // TODO: Add navigation back to overview when clicking on switch

    @RequiresApi(Build.VERSION_CODES.N)
    val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                // Precise location access granted.
                Log.d(TAG, "Precise location access granted")
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                // Only approximate location access granted.
                Log.d(TAG, "Only approximate location access granted")
            } else -> {
            // No location access granted.
                Log.d(TAG, "No location access granted")
        }
        }
    }


    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->

        map = googleMap
        map.mapType = GoogleMap.MAP_TYPE_HYBRID
        map.uiSettings.isZoomControlsEnabled = true

        locationPermissionRequest.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION))

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        fusedLocationClient.lastLocation.addOnSuccessListener { location : Location? ->
            Log.d(TAG, "Location: $location")
            // TODO: Fix location is null here
            if (location != null) {
                val currentLocation = LatLng(location.latitude, location.longitude)
                map.addMarker(MarkerOptions().position(currentLocation).title("Marker in current location"))
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15f))
            }
        }
        // TODO: Get Current location and set as start location

        // TODO: Add markers for each course

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_course_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}