package no.hiof.discgolfapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import no.hiof.discgolfapp.R
import no.hiof.discgolfapp.model.Course


class CourseRecyclerAdapter(private val courses: ArrayList<Course>, private val clickListener: View.OnClickListener) : RecyclerView.Adapter<CourseRecyclerAdapter.CourseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.course_list_item, parent, false)
        return CourseViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val currentCourse = courses[position]

        holder.bind(currentCourse, clickListener)

    }

    override fun getItemCount(): Int {
        return courses.size
    }

    class CourseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val courseNameTextView : TextView = view.findViewById(R.id.courseNameTextView)
        private val courseAreaTextView : TextView = view.findViewById(R.id.courseAreaTextView)

        fun bind(course: Course, clickListener: View.OnClickListener) {
            courseNameTextView.text = course.name
            courseAreaTextView.text = course.area

            itemView.setOnClickListener(clickListener)

        }

    }


}