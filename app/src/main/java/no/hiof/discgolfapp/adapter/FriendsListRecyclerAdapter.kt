package no.hiof.discgolfapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import no.hiof.discgolfapp.R
import no.hiof.discgolfapp.model.FriendRequest
import no.hiof.discgolfapp.model.User

class FriendsListRecyclerAdapter(private val friends: List<User>, private val clickListener: View.OnClickListener) : RecyclerView.Adapter<FriendsListRecyclerAdapter.FriendsListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendsListViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.course_list_item, parent, false)
        return FriendsListViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FriendsListViewHolder, position: Int) {
        val currentFriend = friends[position]

        holder.bind(currentFriend, clickListener)

    }

    override fun getItemCount(): Int {
        return friends.size
    }

    class FriendsListViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(friend: User, clickListener: View.OnClickListener) {


            itemView.setOnClickListener(clickListener)

        }

    }
}