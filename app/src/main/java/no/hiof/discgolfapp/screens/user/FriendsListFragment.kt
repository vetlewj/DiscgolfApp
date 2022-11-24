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
import no.hiof.discgolfapp.adapter.FriendsListRecyclerAdapter
import no.hiof.discgolfapp.databinding.FragmentAcceptFriendBinding
import no.hiof.discgolfapp.databinding.FragmentFriendsListBinding
import no.hiof.discgolfapp.model.Friend
import no.hiof.discgolfapp.model.FriendRequest
import no.hiof.discgolfapp.model.User


class FriendsListFragment : Fragment() {

    private var fragmentBinding: FragmentFriendsListBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_friends_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentFriendsListBinding.bind(view)
        fragmentBinding = binding

        val db = Firebase.firestore
        val currentUser = Firebase.auth.currentUser

        if (currentUser != null) {
            db.collection("users").document(currentUser.uid).collection("friends")
                .get()
                .addOnSuccessListener { documents ->
                    val friends: ArrayList<Friend>  = ArrayList()
                    for (document in documents) {
                        Log.d("GetFriendRequests", "${document.id} => ${document.data}")

                        friends.add(document.toObject())
                    }

                    binding.friendsListRecyclerView.adapter = FriendsListRecyclerAdapter(friends)  {
                        Toast.makeText(context, "you clicked on a friend", Toast.LENGTH_SHORT ).show()
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w("GetUsers", "Error getting documents: ", exception)
                }
        }
        
        
//        binding.friendsListRecyclerView.adapter = FriendsListRecyclerAdapter (User.getUsers())  {
//            Toast.makeText(context, "you clicked on a friend", Toast.LENGTH_SHORT ).show()
//        }

        binding.friendsListRecyclerView.layoutManager = GridLayoutManager(view.context, 1)
    }


}