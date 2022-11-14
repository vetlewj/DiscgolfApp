package no.hiof.discgolfapp.screens.discs

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import no.hiof.discgolfapp.R
import no.hiof.discgolfapp.databinding.FragmentCreateDiscBinding
import no.hiof.discgolfapp.model.Disc

class CreateDiscsFragment : Fragment() {

    private var _binding: FragmentCreateDiscBinding? = null
//    private var binding get() = _binding!!
    private lateinit var binding: FragmentCreateDiscBinding
    private var firebaseAuth = FirebaseAuth.getInstance()
    private var firestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        _binding = FragmentCreateDiscBinding.inflate(inflater, container, false)
//        return inflater.inflate(R.layout.fragment_create_disc, container, false)
        binding = FragmentCreateDiscBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCreateDiscBinding.bind(view)


        val discName = binding.DiscNameEditText.text

        val saveBtn : Button = view.findViewById(R.id.saveDiscBtn)




        saveBtn.setOnClickListener {
            Log.d("Button pressed", "Save disc button pressede")
            val disc = Disc(
                "Test",
                discName.toString(),
                2,
                3,
                1,
                4,
                Disc.DiscType.DISTANCE_DRIVER,
                "gummi",
                175,
                "Red"
            )

            firestore.collection("discs").document("test1").set(disc)
            Toast.makeText(activity, "Button Saved pressed " + discName , Toast.LENGTH_LONG).show()

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}