package no.hiof.discgolfapp.screens.play.createscorecard

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.gms.location.*
import no.hiof.discgolfapp.R
import no.hiof.discgolfapp.adapter.ChooseCourseRecyclerAdapter
import no.hiof.discgolfapp.databinding.FragmentChooseCourseBinding
import no.hiof.discgolfapp.services.SharedViewModel
import no.hiof.discgolfapp.services.location.SharedLocationViewModel

class ChooseCourseFragment : Fragment() {
    private val TAG = "ChooseCourseFragment"

    private var sharedViewModel = SharedViewModel()
    private var locationViewModel = SharedLocationViewModel()

    private var _binding: FragmentChooseCourseBinding? = null
    private val binding get() = _binding!!

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true) {
            Log.d(TAG, "Precise location access granted")
        } else if (permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
            Log.d(TAG, "Only approximate location access granted")
        } else {
            Log.d(TAG, "No location access granted")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_choose_course, container, false)
    }

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
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        locationViewModel.setCurrentLocation(fusedLocationClient)
        locationViewModel.currentLocation.observe(viewLifecycleOwner) { currentLocation ->
            sharedViewModel.getSortedCoursesByDistance(
                currentLocation.latitude,
                currentLocation.longitude,
                viewLifecycleOwner
            )

            sharedViewModel.sortedCourseList.observe(viewLifecycleOwner) { courses ->
                binding.chooseCourseRecyclerView.adapter =
                    ChooseCourseRecyclerAdapter(courses) { clickedItem ->
                        val position =
                            binding.chooseCourseRecyclerView.getChildAdapterPosition(clickedItem)
                        val course = courses[position]
                        Log.d("ChooseCourseFragment", "Course clicked: ${course.name}")
                        val action =
                            ChooseCourseFragmentDirections.actionChooseCourseFragmentToCreateScoreCardFragment(
                                course.uid
                            )
                        findNavController().navigate(action)
                    }
                binding.chooseCourseRecyclerView.layoutManager = GridLayoutManager(context, 1)
            }
            locationViewModel.currentLocation.removeObservers(viewLifecycleOwner)
        }
    }
}