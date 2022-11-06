package no.hiof.discgolfapp.screens.courses

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import no.hiof.discgolfapp.R
import no.hiof.discgolfapp.databinding.FragmentCourseResultsBinding

class CourseResultsFragment : Fragment() {
    private var _binding: FragmentCourseResultsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCourseResultsBinding.inflate(inflater, container, false)

        return binding.root
    }

}