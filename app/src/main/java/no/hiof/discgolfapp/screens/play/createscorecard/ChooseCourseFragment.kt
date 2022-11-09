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

    private var sharedViewModel = SharedViewModel()
    private var locationViewModel = SharedLocationViewModel()

    private var _binding: FragmentChooseCourseBinding? = null
    private val binding get() = _binding!!

    @RequiresApi(Build.VERSION_CODES.N)
    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                Log.d("ChooseCourseFragment", "Precise location access granted")
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                Log.d("ChooseCourseFragment", "Only approximate location access granted")
            }
            else -> {
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
        }
    }
}