package no.hiof.discgolfapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import no.hiof.discgolfapp.adapter.DiscRecyclerAdapter
import no.hiof.discgolfapp.model.Disc

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

    }

}