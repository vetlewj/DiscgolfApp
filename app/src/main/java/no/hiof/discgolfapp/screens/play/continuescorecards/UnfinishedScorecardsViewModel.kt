package no.hiof.discgolfapp.screens.play.continuescorecards

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.MetadataChanges
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObjects
import no.hiof.discgolfapp.model.ScoreCard

class UnfinishedScorecardsViewModel : ViewModel() {
    private var _scorecards : MutableLiveData<List<ScoreCard>> = MutableLiveData()
    val scoreCards: LiveData<List<ScoreCard>> = _scorecards
    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    init {
        Log.i("UnfinishedScorecards", "UnfinishedScorecardsViewModel created")
    }

    fun fetchUnfinishedScorecardsForCurrentPlayer(){
        val storedCards = firestore.collection("scorecards")
            .whereEqualTo("playerId", firebaseAuth.currentUser?.uid)
            .whereEqualTo("finished", false)
            .orderBy("date", Query.Direction.DESCENDING)
        storedCards.addSnapshotListener(MetadataChanges.INCLUDE) { value, error ->
            if (error != null){
                Log.d("UnfinishedScorecards", "Listen failed, could not get scorecards")
            }
            if (value != null){
                val firestoreCards = value.toObjects<ScoreCard>()
                _scorecards.postValue(firestoreCards)
            }
        }
    }
}