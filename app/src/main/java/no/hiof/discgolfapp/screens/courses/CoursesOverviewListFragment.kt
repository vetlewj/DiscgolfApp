package no.hiof.discgolfapp.screens.courses

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import no.hiof.discgolfapp.R
import no.hiof.discgolfapp.adapter.CourseRecyclerAdapter
import no.hiof.discgolfapp.databinding.FragmentCoursesOverviewListBinding
import no.hiof.discgolfapp.helper.data.GetListOfCoursesByCountryCodeResponse
import no.hiof.discgolfapp.services.CoursesService
import no.hiof.discgolfapp.services.NetworkLayer
import no.hiof.discgolfapp.services.SharedViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


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

        viewModel.refreshCourses("NO")
        viewModel.coursesByCountryCodeLiveData.observe(viewLifecycleOwner) { response ->
            if(response == null) {
                Toast.makeText(view.context, "network call was unsuccessful", Toast.LENGTH_SHORT).show()
                return@observe
            }
                val courses = response.courses.toList()

                binding.courseRecyclerView.adapter = CourseRecyclerAdapter(courses, View.OnClickListener {
                    val position = binding.courseRecyclerView.getChildAdapterPosition(it)

                    val selectedCourse = courses[position]

                    val action =
                        CoursesOverviewListFragmentDirections.actionCoursesOverviewListFragmentToCourseInfoFragment()
                    action.let {
                        it.uid = selectedCourse.ID!!.toInt()
                        it.courseName = selectedCourse.Name
                        it.latitude = if (selectedCourse.Y.equals("")) {1000F} else {selectedCourse.Y!!.toFloat()}
                        it.longitude = if (selectedCourse.X.equals("")) {1000F} else {selectedCourse.X!!.toFloat()}
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