package no.hiof.discgolfapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import no.hiof.discgolfapp.R
import no.hiof.discgolfapp.databinding.FragmentCoursesOverviewListBinding
import no.hiof.discgolfapp.screens.courses.CourseInfoFragmentArgs


class AddFriendsFragment : Fragment() {

    private val args: CourseInfoFragmentArgs by navArgs()
    private var fragmentBinding: FragmentCoursesOverviewListBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_friends, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentCoursesOverviewListBinding.bind(view)
        fragmentBinding = binding


    }


}