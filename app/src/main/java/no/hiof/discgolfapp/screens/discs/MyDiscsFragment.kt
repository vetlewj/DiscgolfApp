package no.hiof.discgolfapp.screens.discs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import no.hiof.discgolfapp.R
import no.hiof.discgolfapp.adapter.DiscRecyclerAdapter
import no.hiof.discgolfapp.model.Disc
import no.hiof.discgolfapp.screens.play.createscorecard.ChooseCourseFragment
import no.hiof.discgolfapp.screens.play.createscorecard.ChooseCourseFragmentDirections

class MyDiscsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_discs, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)

        val discRecyclerView = view.findViewById<RecyclerView>(R.id.discRecyclerView)


        discRecyclerView.adapter = DiscRecyclerAdapter(Disc.getDiscs())
        discRecyclerView.layoutManager = GridLayoutManager(context, 1)

        val addBtn: Button = view.findViewById(R.id.addButton)

        addBtn.setOnClickListener {
        val action = MyDiscsFragmentDirections.actionMyDiscsFragmentToCreateDiscsFragment()

            findNavController().navigate(action)

        }


    }

}