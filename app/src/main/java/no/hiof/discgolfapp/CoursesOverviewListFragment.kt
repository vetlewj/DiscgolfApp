package no.hiof.discgolfapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import no.hiof.discgolfapp.adapter.CourseRecyclerAdapter
import no.hiof.discgolfapp.databinding.FragmentCoursesOverviewListBinding
import no.hiof.discgolfapp.model.Course


class CoursesOverviewListFragment : Fragment() {

    private var fragmentBinding: FragmentCoursesOverviewListBinding? = null

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

        val courseDataService = CourseDataService(view.context)

        courseDataService.getListOfCourses("NO", object: CourseDataService.ListOfCoursesByCountryCodeResponse {
            override fun onError(message: String?) {
                Toast.makeText(view.context, message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(courseModels: ArrayList<Course?>) {
                binding.courseRecyclerView.adapter = CourseRecyclerAdapter(Course.getAllCourses(), View.OnClickListener {
                    val position = binding.courseRecyclerView.getChildAdapterPosition(it)

                    val selectedCourse = Course.getAllCourses()[position]

                    val action = CoursesOverviewListFragmentDirections.actionCoursesOverviewListFragmentToCourseInfoFragment()
                    action.uid = selectedCourse!!.uid
                    action.courseName = selectedCourse.name
                    // TODO: Temporary solution, adjust for a better one
                    action.latitude = try {selectedCourse.latitude!!} catch (e: NullPointerException) {1000F}
                    action.longitude = try {selectedCourse.longitude!!} catch (e: NullPointerException) {1000F}


                    findNavController().navigate(action)
                })
                binding.courseRecyclerView.layoutManager = GridLayoutManager(context, 1)
            }

        })

        binding.coursesOverviewListToMapSwitch.setOnCheckedChangeListener {
                compoundButton, b ->

            findNavController().navigate(R.id.action_coursesOverviewListFragment_to_courseMapsFragment)
        }

    }

}