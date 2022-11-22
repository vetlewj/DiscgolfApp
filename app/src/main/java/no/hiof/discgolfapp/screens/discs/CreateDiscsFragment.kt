package no.hiof.discgolfapp.screens.discs

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import no.hiof.discgolfapp.R
import no.hiof.discgolfapp.databinding.FragmentCreateDiscBinding
import no.hiof.discgolfapp.model.Disc


class CreateDiscsFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private var _binding: FragmentCreateDiscBinding? = null
    private lateinit var binding: FragmentCreateDiscBinding
    private var firebaseAuth = FirebaseAuth.getInstance()
    private var firestore = FirebaseFirestore.getInstance()
    private var discTypeArrayPos: Int? = null


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

        var discName: Editable?
        var discManufacture: Editable?
        var discPlastic: Editable?
        var discColor: Editable?

        val saveBtn : Button = view.findViewById(R.id.saveDiscBtn)
        val spinner : Spinner = view.findViewById(R.id.discTypeSpinner)
        val discType = resources.getStringArray(R.array.disc_type_array)

        fun nullEditText(){
            discName = null
            discColor = null
            discPlastic = null
            discManufacture = null
        }

        this.context?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.disc_type_array,
                android.R.layout.simple_spinner_item).also{adapter -> adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = adapter}
        }
        spinner.onItemSelectedListener = this


        saveBtn.setOnClickListener {
            Log.d("Button pressed", "Save disc button pressede")

            discName = binding.DiscNameEditText.text
            discManufacture = binding.DiscManufactureEditText.text
            discPlastic = binding.DiscPlasticEditText.text
            discColor = binding.DiscColorEditText.text

            if (discName?.isBlank() == true || discName == null )  {
                Log.d("disc DiscName","Disc name = $discName ")
                Toast.makeText(activity, "Vennligs velg et navn", Toast.LENGTH_LONG).show()
                binding.DiscNameTextInputLayout.isErrorEnabled = true
                binding.DiscNameTextInputLayout.error = "Vennligs velg et navn"


            } else {
                val discSpeed = binding.DiscSpeedEditText.text.toString().toIntOrNull()
                val discGlide = binding.DiscGlideEditText.text.toString().toIntOrNull()
                val discTurn = binding.DiscTurnEditText.text.toString().toIntOrNull()
                val discFade = binding.DiscFadeEditText.text.toString().toIntOrNull()
                val discWeight = binding.DiscWeightEditText.text.toString().toIntOrNull()

                binding.DiscNameTextInputLayout.isErrorEnabled = false
                binding.DiscNameTextInputLayout.error = null
                binding.DiscNameTextInputLayout.refreshErrorIconDrawableState()

                try {
                    val disc = Disc(
                        firebaseAuth.currentUser?.uid,
                        discName.toString(),
                        discSpeed,
                        discGlide,
                        discTurn,
                        discFade,
                        discTypeArrayPos?.let { discType[it] },
                        discManufacture.toString(),
                        discPlastic.toString(),
                        discWeight,
                        discColor.toString()
                    )

                    firestore.collection("discs").document()
                        .set(disc)
                        .addOnSuccessListener {
                            Log.d("Save disc","Disc $discName successfully added to Firestore")
                            Toast.makeText(activity, "Disc saved " + discName, Toast.LENGTH_LONG).show()
                            nullEditText()
                            formReset()
                        }
                        .addOnFailureListener { e ->
                            Log.w("Disc save failure","Failed to save disc to Firestore", e)
                        }
                } catch (e: NumberFormatException) {
                    Log.e("NumberFormatException", "Save disc NumberFormatException: $e")
                    Toast.makeText(activity, "Failed to save disc ", Toast.LENGTH_LONG).show()
                } finally {
                }
            }
        }
    }

    private fun formReset(){
        binding.DiscNameEditText.text = null
        binding.DiscSpeedEditText.text = null
        binding.DiscGlideEditText.text = null
        binding.DiscTurnEditText.text = null
        binding.DiscFadeEditText.text = null
        binding.DiscManufactureEditText.text = null
        binding.DiscPlasticEditText.text = null
        binding.DiscColorEditText.text = null
        binding.DiscWeightEditText.text = null
        Log.d("FormReset", "Create disc form reset")
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        discTypeArrayPos = position
        Log.d("Disc Types", "Type: ${resources.getStringArray(R.array.disc_type_array).get(position)}")
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

}