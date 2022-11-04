package no.hiof.discgolfapp.screens.courses

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import no.hiof.discgolfapp.R
import no.hiof.discgolfapp.adapter.CourseRecyclerAdapter
import no.hiof.discgolfapp.databinding.FragmentCoursesOverviewListBinding
import no.hiof.discgolfapp.helper.data.CourseDataService
import no.hiof.discgolfapp.model.Course
import no.hiof.discgolfapp.services.CourseService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


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

        val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://discgolfmetrix.com/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        val courseService: CourseService = retrofit.create(CourseService::class.java)

        courseService.getCoursesByCountryCode().enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                Log.i("CourseOverviewListFrag", response.toString())
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                Log.i("CourseOverviewListFrag", t.message ?: "Null message")
            }
        })

        courseDataService.getListOfCourses("NO", object: CourseDataService.ListOfCoursesByCountryCodeResponse {
            override fun onError(message: String?) {
                Toast.makeText(view.context, message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(courseModels: ArrayList<Course?>) {
                binding.courseRecyclerView.adapter = CourseRecyclerAdapter(Course.getAllCourses(), View.OnClickListener {
                    val position = binding.courseRecyclerView.getChildAdapterPosition(it)

                    val selectedCourse = Course.getAllCourses()[position]

                    val action =
                        CoursesOverviewListFragmentDirections.actionCoursesOverviewListFragmentToCourseInfoFragment()
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