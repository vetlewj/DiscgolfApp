package no.hiof.discgolfapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import no.hiof.discgolfapp.R
import no.hiof.discgolfapp.model.Course
import no.hiof.discgolfapp.model.User

class AddFriendsRecyclerAdapter(private val users: ArrayList<User>, private val clickListener: View.OnClickListener) : RecyclerView.Adapter<AddFriendsRecyclerAdapter.UsersViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddFriendsRecyclerAdapter.UsersViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.fragment_add_users_item, parent, false)
        return UsersViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AddFriendsRecyclerAdapter.UsersViewHolder, position: Int) {
        val currentCourse = users[position]

        holder.bind(currentCourse, clickListener)

    }

    override fun getItemCount(): Int {
        return users.size
    }

    class UsersViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //private val courseNameTextView : TextView = view.findViewById(R.id.courseNameTextView)
        //private val courseAreaTextView : TextView = view.findViewById(R.id.courseAreaTextView)

        fun bind(user: User, clickListener: View.OnClickListener) {
//            courseNameTextView.text = course.name
//            courseAreaTextView.text = course.area
//
//            itemView.setOnClickListener(clickListener)

        }


}