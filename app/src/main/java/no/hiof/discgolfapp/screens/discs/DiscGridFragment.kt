package no.hiof.discgolfapp.screens.discs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import no.hiof.discgolfapp.databinding.FragmentDiscGridBinding

class DiscGridFragment : Fragment()  {


    private var _binding: FragmentDiscGridBinding? = null
    private lateinit var binding: FragmentDiscGridBinding



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentDiscGridBinding.inflate(layoutInflater)
        return binding.root
    }


}