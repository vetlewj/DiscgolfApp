package no.hiof.discgolfapp.screens.discs

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import no.hiof.discgolfapp.R
import no.hiof.discgolfapp.adapter.DiscRecyclerAdapter
import no.hiof.discgolfapp.model.Disc
import no.hiof.discgolfapp.screens.play.createscorecard.ChooseCourseFragment
import no.hiof.discgolfapp.screens.play.createscorecard.ChooseCourseFragmentDirections

class MyDiscsFragment : Fragment() {

    private var firebaseAuth = FirebaseAuth.getInstance()
    private var firestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fetchDiscsFromFireStore2()

        return inflater.inflate(R.layout.fragment_my_discs, container, false)
    }

    @SuppressLint("SuspiciousIndentation")
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

    fun fetchDiscsFromFireStore(){
        val discs = firestore.collection("discs")
            .whereEqualTo("playerId", firebaseAuth.currentUser?.uid)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d("Fetch Disc", "${document.data}")
//                    document.toObject<Disc>()
                }
            }
            .addOnFailureListener{ exception ->
                Log.w("Fetch disc", "Error fetching discs from Firestore: ", exception)
            }
    }

    fun fetchDiscsFromFireStore2(){
        val discs = firestore.collection("discs")
        discs.whereEqualTo("playerId", firebaseAuth.currentUser?.uid)
        .get().addOnCompleteListener { task ->
            if (task.isSuccessful){
                Log.d("FetchDisc", "Successful")
                val discsSnapshot = task.result!!
                val disc = discsSnapshot.toObjects(Disc::class.java)!!


                Log.d("Discs v2", "${disc}")

        }

        }

    }

}


