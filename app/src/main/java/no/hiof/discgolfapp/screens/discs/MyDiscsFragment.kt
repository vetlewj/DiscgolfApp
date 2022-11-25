package no.hiof.discgolfapp.screens.discs

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import no.hiof.discgolfapp.R
import no.hiof.discgolfapp.adapter.DiscRecyclerAdapter
import no.hiof.discgolfapp.databinding.FragmentDiscListItemBinding
import no.hiof.discgolfapp.model.Disc

class MyDiscsFragment : Fragment() {

    private val discList: MutableList<Disc> = mutableListOf()
    private var firebaseAuth = FirebaseAuth.getInstance()
    private var firestore = FirebaseFirestore.getInstance()

    private val discRecyclerAdapter = DiscRecyclerAdapter()

    private var _binding : FragmentDiscListItemBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my_discs, container, false)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)

        val discRecyclerView = view.findViewById<RecyclerView>(R.id.discRecyclerView)

        firestore.collection("discs")
            .whereEqualTo("playerId", firebaseAuth.currentUser?.uid)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d("Fetch Disc", "${document.data}")
                    val discObject = document.toObject(Disc::class.java)!!
                    discList.add(discObject)
                    Log.d("disc list", "list: $discList")
                    discRecyclerView.adapter = discRecyclerAdapter
                    discRecyclerAdapter.submitList(discList)
                    discRecyclerView.layoutManager = GridLayoutManager(context, 1)
                }
            }
            .addOnFailureListener{ exception ->
                Log.w("Fetch disc", "Error fetching discs from Firestore: ", exception)
            }


        discRecyclerView.layoutManager = GridLayoutManager(context, 1)

        val addBtn: Button = view.findViewById(R.id.addButton)
        val gridBtn: Button = view.findViewById(R.id.gridButton)

        addBtn.setOnClickListener {
        val action = MyDiscsFragmentDirections.actionMyDiscsFragmentToCreateDiscsFragment()
            findNavController().navigate(action)
            discList.clear()
        }


        gridBtn.setOnClickListener {
            var discNameArray: MutableList<String> =  ArrayList()
            var discSpeedArray: MutableList<Int> =  ArrayList()
            var discTurnArray: MutableList<Int> =  ArrayList()
            var discFadeArray: MutableList<Int> =  ArrayList()
            var discColorArray: MutableList<String> =  ArrayList()


            var i = 0
            while(i < discList.size) {
                discList[i].name?.let { it1 -> discNameArray.add(it1) }
                discList[i].speed?.let { it1 -> discSpeedArray.add(it1) }
                discList[i].turn?.let { it1 -> discTurnArray.add(it1) }
                discList[i].fade?.let { it1 -> discFadeArray.add(it1) }
                discList[i].color?.let { it1 -> discColorArray.add(it1) }
                i++

            }

            val action = MyDiscsFragmentDirections.actionMyDiscsFragmentToDiscGridFragment(
                discNameArray.toTypedArray(),
                discSpeedArray.toIntArray(),
                discTurnArray.toIntArray(),
                discFadeArray.toIntArray(),
                discColorArray.toTypedArray()
            )
            action.let {
                Log.d("disc send data", "Sending data to GridDiscFragment")
            }
            findNavController().navigate(action)
        }
    }


    override fun onDestroyView() {
        Log.d("Disc onDestroyView", "destroyed")
        super.onDestroyView()
    }

}




