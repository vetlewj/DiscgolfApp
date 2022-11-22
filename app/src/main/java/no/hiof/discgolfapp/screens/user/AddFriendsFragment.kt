package no.hiof.discgolfapp.screens.user

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import no.hiof.discgolfapp.R
import no.hiof.discgolfapp.adapter.AddFriendsRecyclerAdapter
import no.hiof.discgolfapp.databinding.FragmentCoursesOverviewListBinding
import no.hiof.discgolfapp.screens.courses.CourseInfoFragmentArgs
import no.hiof.discgolfapp.databinding.FragmentAddFriendsBinding
import no.hiof.discgolfapp.model.User


class AddFriendsFragment : Fragment() {

    private var fragmentBinding: FragmentAddFriendsBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_friends, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentAddFriendsBinding.bind(view)
        fragmentBinding = binding

        val db = Firebase.firestore

        db.collection("users")
            .whereEqualTo("guest", false)
            .get()
            .addOnSuccessListener { documents ->
                val users: ArrayList<User>  = ArrayList()
                for (document in documents) {
                    Log.d("GetUsers", "${document.id} => ${document.data}")

                    users.add(document.toObject())
                }

                binding.addFriendsRecyclerView.adapter = AddFriendsRecyclerAdapter(
                    users
                ) {
                    Toast.makeText(view.context, "You have clicked on a user", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Log.w("GetUsers", "Error getting documents: ", exception)
            }


        binding.addFriendsRecyclerView.layoutManager = GridLayoutManager(context, 1)



    }


}