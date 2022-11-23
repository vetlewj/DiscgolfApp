package no.hiof.discgolfapp.screens.play.takescore

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.MetadataChanges
import com.google.firebase.firestore.ktx.toObjects
import no.hiof.discgolfapp.model.ScoreCard

class TakeScoreViewModel : ViewModel() {
    var scoreCard : ScoreCard? = null
    var score = 0
    var par = 0
    var holeNumber = 0
    var distance = 0
    var totalScore = 0
    var totalPar = 0

    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private var _scorecard : MutableLiveData<ScoreCard> = MutableLiveData()
    val storedScoreCard: LiveData<ScoreCard> = _scorecard

    init {
        Log.i("TakeScoreViewModel", "TakeScoreViewModel created")
    }

    fun fetchScoreCard(scorecardId: String, context: Context){
        val storedCard = firestore.collection("scorecards")
            .whereEqualTo("playerId", firebaseAuth.currentUser?.uid)
            .whereEqualTo("id", scorecardId)
        storedCard.addSnapshotListener(MetadataChanges.INCLUDE) {value, error ->
            if (error != null){
                Log.d("TakeScore", "Listen failed, could not get scorecard")
                Toast.makeText(context, "Kunne ikke hente poengkort",Toast.LENGTH_SHORT
                ).show()
            }
            if (value != null){
                val firestoreCards = value.toObjects<ScoreCard>()
                _scorecard.postValue(firestoreCards[0])
            }
        }
    }

}