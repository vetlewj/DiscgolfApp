package no.hiof.discgolfapp.screens.courses

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import no.hiof.discgolfapp.R
import no.hiof.discgolfapp.adapter.CourseRecyclerAdapter
import no.hiof.discgolfapp.databinding.FragmentCoursesOverviewListBinding
import no.hiof.discgolfapp.helper.CourseType
import no.hiof.discgolfapp.services.SharedViewModel


class CoursesOverviewListFragment : Fragment() {

    private var fragmentBinding: FragmentCoursesOverviewListBinding? = null

    val viewModel: SharedViewModel by lazy {
        ViewModelProvider(this).get(SharedViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_courses_overview_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentCoursesOverviewListBinding.bind(view)
        fragmentBinding = binding

        viewModel.fetchCourses("NO", CourseType.TYPE1_AND_TYPE2_WITH_NO_PARENT)
        viewModel.coursesByCountryCodeLiveData.observe(viewLifecycleOwner) { listOfCourses ->
            if(listOfCourses == null) {
                Toast.makeText(view.context, "network call was unsuccessful", Toast.LENGTH_SHORT).show()
                return@observe
            }

            binding.courseRecyclerView.adapter =
                    CourseRecyclerAdapter(listOfCourses, View.OnClickListener { it ->
                        val position = binding.courseRecyclerView.getChildAdapterPosition(it)

                        val selectedCourse = listOfCourses[position]

                        val action =
                            CoursesOverviewListFragmentDirections.actionCoursesOverviewListFragmentToCourseInfoFragment()
                        action.let {
                            it.uid = selectedCourse.uid
                            it.type = selectedCourse.type!!
                            it.latitude = try {
                                selectedCourse.latitude!!.toFloat()
                            } catch (e: NullPointerException) {
                                1000F
                            }
                            it.longitude = try {
                                selectedCourse.longitude!!.toFloat()
                            } catch (e: NullPointerException) {
                                1000F
                            }
                        }
                        findNavController().navigate(action)
                    })
                binding.courseRecyclerView.layoutManager = GridLayoutManager(context, 1)

            }

        binding.coursesOverviewListToMapSwitch.setOnCheckedChangeListener {
                compoundButton, b ->

            findNavController().navigate(R.id.action_coursesOverviewListFragment_to_courseMapsFragment)
        }

    }

}