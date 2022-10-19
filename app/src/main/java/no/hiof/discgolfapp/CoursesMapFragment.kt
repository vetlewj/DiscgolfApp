package no.hiof.discgolfapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import no.hiof.discgolfapp.databinding.FragmentCoursesMapBinding
import no.hiof.discgolfapp.databinding.FragmentCoursesOverviewListBinding


class CoursesMapFragment : Fragment() {

    private var fragmentBinding: FragmentCoursesMapBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_courses_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentCoursesMapBinding.bind(view)
        fragmentBinding = binding

        binding.mapsToCoursesListSwitch.setOnClickListener({
            findNavController().navigate(R.id.action_coursesMapFragment_to_coursesOverviewListFragment)
        })


    }
}