package no.hiof.discgolfapp.screens.user

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import no.hiof.discgolfapp.R
import no.hiof.discgolfapp.databinding.FragmentProfileBinding


class ProfileFragment : Fragment() {

    private var fragmentBinding: FragmentProfileBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentProfileBinding.bind(view)
        fragmentBinding = binding

        val user = Firebase.auth.currentUser
        user?.let {
            val userFirstName = user.displayName.toString()
            val email = user.email.toString()

            binding.userEmailTextView.text = email
            binding.userName.text = userFirstName

            binding.signOutButton.setOnClickListener {
                signOut()
                val navController = this.findNavController()
                val action =
                    ProfileFragmentDirections.actionUserFragmentToCoursesOverviewListFragment2()
                navController.navigate(action)
//                activity?.finish()
            }

            binding.addFriendButton.setOnClickListener {
                findNavController().navigate(R.id.action_userFragment_to_addFriendsFragment)
            }

            binding.friendRequestsButton.setOnClickListener {
                findNavController().navigate(R.id.action_userFragment_to_acceptFriendFragment)
            }
            binding.friendListButton.setOnClickListener {
                findNavController().navigate(R.id.action_userFragment_to_friendsListFragment)
            }


        }
    }

    private fun signOut(){
        Firebase.auth.signOut()
        Log.d("User signed out", "User: " + Firebase.auth.currentUser)
        Toast.makeText( activity, "Du er nå logget ut", Toast.LENGTH_SHORT).show()

    }


}