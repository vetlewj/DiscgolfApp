package no.hiof.discgolfapp.screens.user

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import no.hiof.discgolfapp.R
import no.hiof.discgolfapp.adapter.AcceptFriendRecyclerAdapter
import no.hiof.discgolfapp.adapter.AddFriendsRecyclerAdapter
import no.hiof.discgolfapp.databinding.FragmentAcceptFriendBinding
import no.hiof.discgolfapp.databinding.FragmentAddFriendsBinding
import no.hiof.discgolfapp.model.FriendRequest
import no.hiof.discgolfapp.model.User


class AcceptFriendFragment : Fragment() {

    private var fragmentBinding: FragmentAcceptFriendBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_accept_friend, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentAcceptFriendBinding.bind(view)
        fragmentBinding = binding

        val db = Firebase.firestore
        val currentUser = Firebase.auth.currentUser

        db.collection("friend-request")
            .whereEqualTo("receiverUid","${currentUser?.uid}" )
            .get()
            .addOnSuccessListener { documents ->
                val friendRequest: ArrayList<FriendRequest>  = ArrayList()
                for (document in documents) {
                    Log.d("GetFriendRequests", "${document.id} => ${document.data}")

                    friendRequest.add(document.toObject())
                }

                binding.acceptFriendsRecyclerView.adapter = AcceptFriendRecyclerAdapter(
                    friendRequest
                ) {
                    Toast.makeText(view.context, "You have clicked on a user", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Log.w("GetUsers", "Error getting documents: ", exception)
            }


        binding.acceptFriendsRecyclerView.layoutManager = GridLayoutManager(context, 1)
    }

}