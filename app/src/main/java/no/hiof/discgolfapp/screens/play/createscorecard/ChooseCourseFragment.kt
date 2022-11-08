package no.hiof.discgolfapp.screens.play.createscorecard

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import no.hiof.discgolfapp.R
import no.hiof.discgolfapp.adapter.ChooseCourseRecyclerAdapter
import no.hiof.discgolfapp.databinding.FragmentChooseCourseBinding
import no.hiof.discgolfapp.model.Course
import no.hiof.discgolfapp.services.SharedRepository
import no.hiof.discgolfapp.services.SharedViewModel

class ChooseCourseFragment : Fragment() {

    private var sharedViewModel = SharedViewModel()

    private var _binding: FragmentChooseCourseBinding? = null
    private val binding get() = _binding!!

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private val defaultLocation = Location("").apply {
        latitude = 59.12810601681866
        longitude = 11.402435302734375
    }
    private var currentLocation: Location? = defaultLocation


    @RequiresApi(Build.VERSION_CODES.N)
    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                // Precise location access granted.
                Log.d("ChooseCourseFragment", "Precise location access granted")
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                // Only approximate location access granted.
                Log.d("ChooseCourseFragment", "Only approximate location access granted")
            }
            else -> {
                // No location access granted.
                Log.d("ChooseCourseFragment", "No location access granted")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_choose_course, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentChooseCourseBinding.bind(view)

        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
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
                // TODO: get current location from user from a single source, not copy from maps fragment

                sharedViewModel.getSortedCoursesByDistance(
                    currentLocation?.latitude ?: defaultLocation.latitude,
                    currentLocation?.longitude ?: defaultLocation.longitude,
                    viewLifecycleOwner
                )

                sharedViewModel.sortedCourseList.observe(viewLifecycleOwner) { courses ->
                    if (courses == null) {
                        Log.w("ChooseCourseFragment", "courses is null")
                        return@observe
                    }
                    // TODO: Courses should be sorted by distance from user
                    // TODO: update recycler adapter implementation
                    binding.chooseCourseRecyclerView.adapter =
                        ChooseCourseRecyclerAdapter(courses) { clickedItem ->

                            val position =
                                binding.chooseCourseRecyclerView.getChildAdapterPosition(clickedItem)
                            val course = courses[position]

                            Log.d("ChooseCourseFragment", "Course clicked: ${course.name}")

                            // TODO: Use ID instead of name when navigating to CreateScoreCardFragment
                            val action =
                                ChooseCourseFragmentDirections.actionChooseCourseFragmentToCreateScoreCardFragment(
                                    course.uid
                                )

                            findNavController().navigate(action)
                        }
                    binding.chooseCourseRecyclerView.layoutManager = GridLayoutManager(context, 1)
                }
            }
        }


    }

}