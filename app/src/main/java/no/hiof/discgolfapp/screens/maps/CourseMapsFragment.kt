package no.hiof.discgolfapp.screens.maps

import android.Manifest
import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.switchmaterial.SwitchMaterial
import no.hiof.discgolfapp.R
import no.hiof.discgolfapp.model.Course

class CourseMapsFragment : Fragment() {
    private val TAG = "CourseMapsFragment"
    private lateinit var map: GoogleMap

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private var currentLocation: Location? = null


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
            }
            else -> {
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

        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )

        addCourseMarkers()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        locationRequest =
            LocationRequest.Builder(600).setPriority(Priority.PRIORITY_HIGH_ACCURACY).build()
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                currentLocation = locationResult.lastLocation
            }
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                currentLocation = location
                Log.d(
                    TAG,
                    "Current Location: ${currentLocation?.latitude}, ${currentLocation?.longitude}"
                )
                val currentLatLng = LatLng(currentLocation!!.latitude, currentLocation!!.longitude)
                map.addMarker(MarkerOptions().position(currentLatLng).title("Current Location"))
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
            }
        }


        // TODO: Add markers for each course

    }

    private fun addCourseMarkers() {
        val courses = Course.getCourses();

        for (course in courses) {
            val courseLatLng =
                LatLng(course.latitude?.toDouble() ?: 0.0, course.longitude?.toDouble() ?: 0.0)
            map.addMarker(
                MarkerOptions()
                    .position(courseLatLng)
                    .title(course.name)
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_course_maps, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapSwitch = view.findViewById<SwitchMaterial>(R.id.mapsToCoursesListSwitch)
        mapSwitch?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Log.d(TAG, "Switch clicked")
                val action =
                    CourseMapsFragmentDirections.actionCourseMapsFragmentToCoursesOverviewListFragment()
                view.findNavController().navigate(action)
            }
        }

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)


    }
}