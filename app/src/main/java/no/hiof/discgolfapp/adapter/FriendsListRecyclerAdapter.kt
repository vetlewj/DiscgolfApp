package no.hiof.discgolfapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import no.hiof.discgolfapp.R
import no.hiof.discgolfapp.model.Friend
import no.hiof.discgolfapp.model.FriendRequest
import no.hiof.discgolfapp.model.User

class FriendsListRecyclerAdapter(private val friends: List<Friend>, private val clickListener: View.OnClickListener) : RecyclerView.Adapter<FriendsListRecyclerAdapter.FriendsListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendsListViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.friend_list_item, parent, false)
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
        private val userName : TextView = view.findViewById(R.id.userNameTextView)
        private val profilePicture: ImageView = view.findViewById(R.id.profilePictureInAddFriendsListImageView)

        fun bind(friend: Friend, clickListener: View.OnClickListener) {

            userName.text = friend.name
            //profilePicture.setImageResource(R.drawable)


            itemView.setOnClickListener(clickListener)

        }

    }
}