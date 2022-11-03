package no.hiof.discgolfapp.screens.play.createscorecard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import no.hiof.discgolfapp.R
import no.hiof.discgolfapp.adapter.ChooseCourseRecyclerAdapter
import no.hiof.discgolfapp.model.Course

class ChooseCourseFragment : Fragment() {

    private val courseList : List<Course> = Course.getCourses()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_choose_course, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val courseRecyclerView = view.findViewById<RecyclerView>(R.id.choose_course_recycler_view)

        // TODO: Courses should be sorted by distance from user
        courseRecyclerView.adapter = ChooseCourseRecyclerAdapter(courseList){
            Log.d("ChooseCourseFragment", "Course clicked")

            val position = it as Button
            val courseName = position.text.toString()

            // TODO: Use ID instead of name when navigating to CreateScoreCardFragment
            val clickedCourse = courseList.find { course -> course.name == courseName }

            val action =
                ChooseCourseFragmentDirections.actionChooseCourseFragmentToCreateScoreCardFragment(
                    clickedCourse!!.name
                )

            findNavController().navigate(action)
        }

        courseRecyclerView.layoutManager = GridLayoutManager(context, 1)

    }
}