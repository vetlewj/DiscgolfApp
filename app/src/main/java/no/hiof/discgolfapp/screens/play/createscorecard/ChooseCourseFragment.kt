package no.hiof.discgolfapp.screens.play.createscorecard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_choose_course, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentChooseCourseBinding.bind(view)

        sharedViewModel.fetchCourses("NO")
        sharedViewModel.coursesByCountryCodeLiveData.observe(viewLifecycleOwner) { courses ->
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