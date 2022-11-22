package no.hiof.discgolfapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import no.hiof.discgolfapp.R
import no.hiof.discgolfapp.model.Course
import no.hiof.discgolfapp.model.User

class AddFriendsRecyclerAdapter(private val users: List<User>, private val clickListener: View.OnClickListener) : RecyclerView.Adapter<AddFriendsRecyclerAdapter.UsersViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddFriendsRecyclerAdapter.UsersViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.fragment_add_users_item, parent, false)
        return UsersViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AddFriendsRecyclerAdapter.UsersViewHolder, position: Int) {
        val currentUser = users[position]

        holder.bind(currentUser, clickListener)

    }

    override fun getItemCount(): Int {
        return users.size
    }

    class UsersViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val userNameTextView : TextView = view.findViewById(R.id.userNameTextView)
        private val addFriendButton : Button = view.findViewById(R.id.addNewFriendButton)

        fun bind(user: User, clickListener: View.OnClickListener) {
            userNameTextView.text = user.name

            addFriendButton.setOnClickListener {
                Toast.makeText(itemView.context, "sent friend request to ${user.name}", Toast.LENGTH_SHORT).show()
            }

            itemView.setOnClickListener(clickListener)

        }


    }
}