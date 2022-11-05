package no.hiof.discgolfapp.screens.user

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import no.hiof.discgolfapp.R
import org.checkerframework.checker.units.qual.A




class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = Firebase.auth.currentUser
        user?.let {
            val userFirstName = user.displayName.toString()
            val email = user.email.toString()

            val emailTextView: TextView = view.findViewById(R.id.emailTextView)
            val userNameTextView: TextView = view.findViewById(R.id.userFirstName)

            emailTextView.text = email
            userNameTextView.text = userFirstName

            val signOutBtn: Button = view.findViewById(R.id.signOutButton)

            signOutBtn.setOnClickListener {
                signOut()
//                val navController = this.findNavController()
//                val action =
//                    ProfileFragmentDirections.actionUserFragmentToCoursesOverviewListFragment2()
//                navController.navigate(action)
                activity?.finish()
            }

        }
    }

    private fun signOut(){
        Firebase.auth.signOut()
        Log.d("User signed out", "User: " + Firebase.auth.currentUser)
        Toast.makeText( activity, "Du er nå logget ut", Toast.LENGTH_SHORT).show()

    }


}