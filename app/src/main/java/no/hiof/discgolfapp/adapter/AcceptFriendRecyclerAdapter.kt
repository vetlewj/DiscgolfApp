package no.hiof.discgolfapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import no.hiof.discgolfapp.R
import no.hiof.discgolfapp.model.Course
import no.hiof.discgolfapp.model.FriendRequest
import no.hiof.discgolfapp.model.User

class AcceptFriendRecyclerAdapter(private val friendRequest: List<FriendRequest>, private val clickListener: View.OnClickListener) : RecyclerView.Adapter<AcceptFriendRecyclerAdapter.AcceptFriendViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AcceptFriendViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.course_list_item, parent, false)
        return AcceptFriendViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AcceptFriendViewHolder, position: Int) {
        val currentFriendRequest = friendRequest[position]

        holder.bind(currentFriendRequest, clickListener)

    }

    override fun getItemCount(): Int {
        return friendRequest.size
    }

    class AcceptFriendViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(friendRequest: FriendRequest, clickListener: View.OnClickListener) {


            itemView.setOnClickListener(clickListener)

        }

    }


}