package no.hiof.discgolfapp.screens.discs

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
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


        val discName = binding.DiscNameEditText.text
        val discSpeed = binding.DiscSpeedEditText.text
        val discGlide = binding.DiscGlideEditText.text
        val discTurn = binding.DiscTurnEditText.text
        val discFade = binding.DiscFadeEditText.text
        val discManufacture = binding.DiscManufactureEditText.text
        val discPlastic = binding.DiscPlasticEditText.text
        val discColor = binding.DiscColorEditText.text
        val discWeight = binding.DiscWeightEditText.text
        val discType = resources.getStringArray(R.array.disc_type_array)

        val saveBtn : Button = view.findViewById(R.id.saveDiscBtn)
        val spinner : Spinner = view.findViewById(R.id.discTypeSpinner)

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
        Log.d("this disctype" ," disktype ${discTypeArrayPos?.let { discType.get(it) }}")
        val disc = Disc(
            firebaseAuth.currentUser?.uid,
            discName.toString(),
            discSpeed.toString().toInt(),
            discGlide.toString().toInt(),
            discTurn.toString().toInt(),
            discFade.toString().toInt(),
            discTypeArrayPos?.let { discType.get(it) },
            discManufacture.toString(),
            discPlastic.toString(),
            discWeight.toString().toInt(),
            discColor.toString()
        )

        firestore.collection("discs").document().set(disc)
        Toast.makeText(activity, "Disc saved " + discName , Toast.LENGTH_LONG).show()
        formReset()
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