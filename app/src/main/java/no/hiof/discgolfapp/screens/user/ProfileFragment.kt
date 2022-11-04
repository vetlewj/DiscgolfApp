package no.hiof.discgolfapp.screens.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import no.hiof.discgolfapp.R

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
        }
    }
}