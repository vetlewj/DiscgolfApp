package no.hiof.discgolfapp.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import no.hiof.discgolfapp.R
import no.hiof.discgolfapp.model.Course

class ChooseCourseRecyclerAdapter(
    private val courses: ArrayList<Course>,
    private val clickListener: View.OnClickListener
) : RecyclerView.Adapter<ChooseCourseRecyclerAdapter.ChooseCourseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooseCourseViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.choose_course_list_item, parent, false)

        return ChooseCourseViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ChooseCourseViewHolder, position: Int) {
        val currentCourse = courses[position]

        Log.d("ChooseCourseRecycler", "onBindViewHolder: ${currentCourse.name}")

        holder.bind(currentCourse, clickListener)
    }

    override fun getItemCount(): Int {
        return courses.size
    }

    class ChooseCourseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val chooseCourseBtn: TextView = view.findViewById(R.id.chooseCourseBtn)

        fun bind(course: Course, clickListener: View.OnClickListener) {
            chooseCourseBtn.text = course.name
            itemView.setOnClickListener(clickListener)
        }

    }
}