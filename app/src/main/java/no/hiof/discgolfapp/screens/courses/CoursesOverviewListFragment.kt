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
import no.hiof.discgolfapp.helper.data.GetListOfCoursesByCountryCodeResponse
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

        val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://discgolfmetrix.com/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        val courseService: CourseService = retrofit.create(CourseService::class.java)

        courseService.getCoursesByCountryCode("NO").enqueue(object : Callback<GetListOfCoursesByCountryCodeResponse> {
            override fun onResponse(call: Call<GetListOfCoursesByCountryCodeResponse>, response: Response<GetListOfCoursesByCountryCodeResponse>) {
                Log.i("CourseOverviewListFrag", response.toString())

                if (!response.isSuccessful) {
                    Toast.makeText(view.context, "network call was unnsuccessful", Toast.LENGTH_SHORT).show()
                    return
                }
                val body = response.body()!!
                val courses = body.courses

                binding.courseRecyclerView.adapter = CourseRecyclerAdapter(courses.toList(), View.OnClickListener {
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

            override fun onFailure(call: Call<GetListOfCoursesByCountryCodeResponse>, t: Throwable) {
                Log.i("CourseOverviewListFrag", t.message ?: "Null message")
            }
        })

        binding.coursesOverviewListToMapSwitch.setOnCheckedChangeListener {
                compoundButton, b ->

            findNavController().navigate(R.id.action_coursesOverviewListFragment_to_courseMapsFragment)
        }

    }

}