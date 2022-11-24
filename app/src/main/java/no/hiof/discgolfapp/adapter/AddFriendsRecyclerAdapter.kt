package no.hiof.discgolfapp.adapter

import android.content.ContentValues
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import no.hiof.discgolfapp.R
import no.hiof.discgolfapp.model.FriendRequest
import no.hiof.discgolfapp.model.User
import java.util.*

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
            val currentuUser = Firebase.auth.currentUser
            val db = Firebase.firestore

            // lag en fetch her til senere

            addFriendButton.setOnClickListener {
                try {
                    // If friend request exists
                    // with same receiverUID and sender UID
                    // Så den viser, men pending friend request. Få opp en melding om at venneforespørsel allerede er sendt
                    // og når man har trykket på knappen så skifter den til pendning og sier at man har sendt en forespørsel

                    // Hvordan løse, først sjekke om den eksisterer, om den gjør det endre knapp til pending friend request.
                    val newFriendRequest =
                            FriendRequest(
                                date = Date(),
                                senderUid = currentuUser!!.uid,
                                receiverUid = user.authUid,
                                name = currentuUser.displayName!!,
                                pictureUrl = currentuUser.photoUrl,
                                acceptRequest = null
                            )

                    db.collection("friend-request")
                        .whereEqualTo("senderUid", currentuUser.uid)
                        .whereEqualTo("receiverUid", user.authUid)
                        .get()
                        .addOnSuccessListener { document ->
                             if(document.isEmpty) {
                                 db.collection("friend-request")
                                     .add(newFriendRequest)
                                     .addOnSuccessListener { documentReference ->
                                         Log.d("FriendRequestCollection", "DocumentSnapshot added with ID: ${documentReference.id}")
                                         addFriendButton.text = "pending response "
                                         Toast.makeText(itemView.context, "sent friend request to ${user.name}", Toast.LENGTH_SHORT).show()
                                     }
                                     .addOnFailureListener { e ->
                                         Log.w(ContentValues.TAG, "Error adding document", e)
                                     }
                             } else {
                                 addFriendButton.text = "pending response "
                             }
                        }


                } catch (_: java.lang.NullPointerException) {
                    Toast.makeText(itemView.context, "Something went wrong when trying to send request, please try again", Toast.LENGTH_SHORT).show()
                }


            }

            itemView.setOnClickListener(clickListener)

        }


    }
}