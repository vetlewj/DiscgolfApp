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
import no.hiof.discgolfapp.model.Course
import no.hiof.discgolfapp.model.FriendRequest
import no.hiof.discgolfapp.model.User
import java.util.*

class AcceptFriendRecyclerAdapter(private val friendRequest: List<FriendRequest>, private val clickListener: View.OnClickListener) : RecyclerView.Adapter<AcceptFriendRecyclerAdapter.AcceptFriendViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AcceptFriendViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.accept_friend_list_item, parent, false)
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
        private val userName : TextView = view.findViewById(R.id.userNameTextView)
        private val acceptFriendButton : Button = view.findViewById(R.id.accceptNewFriendButton)
        private val declineFriendButton : Button = view.findViewById(R.id.declineFriendButton)

        fun bind(friendRequest: FriendRequest, clickListener: View.OnClickListener) {
            userName.text = friendRequest.name
            val currentuUser = Firebase.auth.currentUser
            val db = Firebase.firestore


            declineFriendButton.setOnClickListener {
                //delete request
                db.collection("friend-request")
                    .whereEqualTo("receiverUid", friendRequest.receiverUid.toString())
                    .whereEqualTo("senderUid", friendRequest.senderUid.toString())
                    .get()
                    .addOnSuccessListener { documents ->
                        Log.d("GetFriendRequest", "DocumentSnapshot successfully deleted!")

                        for (document in documents) {
                            db.collection("friend-request").document(document.id)
                                .delete()
                                .addOnSuccessListener { documentReference ->
                                    Log.d("DeleteRequest", "DocumentSnapshot added with ID: ${documentReference}")
                                    Toast.makeText(itemView.context, "Du har nå avslått venneforespørsel fra ${friendRequest.name}", Toast.LENGTH_LONG).show()
                                }
                                .addOnFailureListener { e ->
                                    Log.w(ContentValues.TAG, "Error adding document", e)
                                }
                        }

                    }
                    .addOnFailureListener { e -> Log.w("DeleteRequest", "Error deleting document", e) }
            }

            acceptFriendButton.setOnClickListener {
                // add friends in both friends collection
                val receivedFriendRequestRef = db.collection("users").document(friendRequest.receiverUid.toString()).collection("friends").document()
                val senderFriendRequestRef = db.collection("users").document(friendRequest.senderUid.toString()).collection("friends").document()

                try {

                    val newFriendReciver = hashMapOf(
                        "name" to friendRequest.name,
                        "friendAuthUid" to friendRequest.senderUid,
                        "date" to Date(),
                        "profilePicture" to friendRequest.pictureUrl
                    )
                    val newFriendSender = hashMapOf(
                        "name" to "${currentuUser?.displayName}",
                        "friendAuthUid" to friendRequest.senderUid,
                        "date" to Date(),
                        "profilePicture" to "${currentuUser?.photoUrl}"
                    )

                    db.runBatch { batch ->
                        batch.set(receivedFriendRequestRef, newFriendReciver )
                        batch.set(senderFriendRequestRef, newFriendSender)

                    }.addOnSuccessListener { documentReference ->
                        Log.d("FriendRequestCollection", "DocumentSnapshot added with ID: $documentReference")
                        Toast.makeText(itemView.context, "Du har nå blitt venn med ${friendRequest.name}", Toast.LENGTH_LONG).show()
                        db.collection("friend-request")
                            .whereEqualTo("receiverUid", friendRequest.receiverUid.toString())
                            .whereEqualTo("senderUid", friendRequest.senderUid.toString())
                            .get()
                            .addOnSuccessListener { documents ->
                                Log.d("GetFriendRequest", "DocumentSnapshot successfully deleted!")
                                for (document in documents) {
                                    db.collection("friend-request").document(document.id)
                                        .delete()
                                        .addOnSuccessListener { documentReference ->
                                            Log.d("DeleteRequest", "DocumentSnapshot added with ID: ${documentReference}")
                                        }
                                        .addOnFailureListener { e ->
                                            Log.w(ContentValues.TAG, "Error adding document", e)
                                        }
                                }

                            }
                            .addOnFailureListener { e -> Log.w("DeleteRequest", "Error deleting document", e) }
                    }
                    .addOnFailureListener { e ->
                            Log.w("FriendRequestCollection", "Error adding document", e)
                        }

                } catch (_: java.lang.NullPointerException) {
                    Toast.makeText(itemView.context, "Something went wrong when tring to send request, please try again", Toast.LENGTH_SHORT).show()
                }

            }

            itemView.setOnClickListener(clickListener)

        }

    }


}