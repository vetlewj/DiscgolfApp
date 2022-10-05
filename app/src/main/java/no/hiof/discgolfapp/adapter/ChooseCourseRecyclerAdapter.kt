package no.hiof.discgolfapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import no.hiof.discgolfapp.R
import no.hiof.discgolfapp.model.Course

class ChooseCourseRecyclerAdapter(private val courses: List<Course>) : RecyclerView.Adapter<ChooseCourseRecyclerAdapter.ChooseCourseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooseCourseViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.choose_course_list_item, parent, false)
        return ChooseCourseViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ChooseCourseViewHolder, position: Int) {
        val currentCourse = courses[position]

        holder.bind(currentCourse)
    }

    override fun getItemCount(): Int {
        return courses.size
    }

    class ChooseCourseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val chooseCourseBtn : Button = view.findViewById(R.id.chooseCourseBtn)

        fun bind(course: Course) {
            chooseCourseBtn.text = course.name
        }

    }
}