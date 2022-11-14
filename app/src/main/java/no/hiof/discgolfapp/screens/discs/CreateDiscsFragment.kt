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
    private lateinit var binding: FragmentCreateDiscBinding
    private var firebaseAuth = FirebaseAuth.getInstance()
    private var firestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentCreateDiscBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCreateDiscBinding.bind(view)


        val discName = binding.DiscNameEditText.text
        val discSpeed = binding.DiscSpeedEditText.text
        val discGlide = binding.DiscGlideEditText.text
        val discTurn = binding.DiscTurnEditText.text
        val discFade = binding.DiscFadeEditText.text
        val discManufacture = binding.DiscManufactureEditText.text
        val discPlastic = binding.DiscPlasticEditText.text
        val discColor = binding.DiscColorEditText.text

        val saveBtn : Button = view.findViewById(R.id.saveDiscBtn)



        saveBtn.setOnClickListener {
            Log.d("Button pressed", "Save disc button pressede")
            val disc = Disc(
                "111",
                discName.toString(),
                discSpeed.toString().toInt(),
                discGlide.toString().toInt(),
                discTurn.toString().toInt(),
                discFade.toString().toInt(),
                Disc.DiscType.DISTANCE_DRIVER,
                discManufacture.toString(),
                discPlastic.toString(),
                175,
                discColor.toString()
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