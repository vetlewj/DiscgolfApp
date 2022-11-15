package no.hiof.discgolfapp.screens.maps

import android.Manifest
import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.switchmaterial.SwitchMaterial
import no.hiof.discgolfapp.R
import no.hiof.discgolfapp.helper.CourseType
import no.hiof.discgolfapp.services.SharedViewModel
import no.hiof.discgolfapp.services.location.SharedLocationViewModel

class CourseMapsFragment : Fragment() {
    private val TAG = "CourseMapsFragment"
    private lateinit var map: GoogleMap

    private var locationViewModel = SharedLocationViewModel()
    private var sharedViewModel: SharedViewModel = SharedViewModel()


    @RequiresApi(Build.VERSION_CODES.N)
    val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                Log.d(TAG, "Precise location access granted")
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                Log.d(TAG, "Only approximate location access granted")
            }
            else -> {
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

        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        locationViewModel.setCurrentLocation(fusedLocationClient)
        locationViewModel.currentLocation.observe(viewLifecycleOwner) { currentLocation ->
            Log.d(
                TAG,
                "Current Location: ${currentLocation?.latitude}, ${currentLocation?.longitude}"
            )
            val currentLatLng = LatLng(currentLocation.latitude, currentLocation.longitude)
            map.addMarker(MarkerOptions().position(currentLatLng).title("Current Location"))
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 10f))
            locationViewModel.currentLocation.removeObservers(viewLifecycleOwner)
        }
    }

    private fun addCourseMarkers() {
        sharedViewModel.fetchCourses("NO", CourseType.TYPE1_AND_TYPE2_WITH_NO_PARENT)
        sharedViewModel.coursesByCountryCodeLiveData.observe(viewLifecycleOwner) { courses ->
            if (courses == null) {
                Log.w("ChooseCourseFragment", "courses is null")
                return@observe
            }

            // TODO: Change drawable resource to discgolf
            val drawable = ResourcesCompat.getDrawable(resources, R.drawable.ic_golf_course, null)
            val bitmap = drawable?.toBitmap().let {
                Bitmap.createScaledBitmap(it!!, 150, 150, false)
            }
            for (course in courses) {
                val lat = course.latitude?.toDouble() ?: 0.0
                val lon = course.longitude?.toDouble() ?: 0.0
                if (lat != 0.0 && lon != 0.0) {
                    val courseLatLng =
                        LatLng(
                            lat,
                            lon
                        )
                    map.addMarker(
                        MarkerOptions()
                            .position(courseLatLng)
                            .title(course.name)
                            .icon(bitmap?.let { BitmapDescriptorFactory.fromBitmap(it) })
                    )
                    map.setOnMarkerClickListener { marker ->
                        val action =
                            CourseMapsFragmentDirections.actionCourseMapsFragmentToCourseInfoFragment()
                        action.uid = course.uid
                        action.longitude = course.longitude ?: 0.0F
                        action.latitude = course.latitude ?: 0.0F
                        action.type = course.type!!

                        view?.findNavController()?.navigate(action)
                        true
                    }
                }
            }
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