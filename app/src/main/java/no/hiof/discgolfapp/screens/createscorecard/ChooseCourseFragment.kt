package no.hiof.discgolfapp.screens.createscorecard

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
            // TODO: Get the course clicked and pass it to the next fragment
            val courseNum = 1
            val action = ChooseCourseFragmentDirections.actionChooseCourseFragmentToCreateScoreCardFragment(courseNum)
            action.courseNum = courseNum

            Toast.makeText(context, "Course ${courseNum+1} selected", Toast.LENGTH_SHORT).show()

            findNavController().navigate(action)
        }

        courseRecyclerView.layoutManager = GridLayoutManager(context, 1)

    }
}