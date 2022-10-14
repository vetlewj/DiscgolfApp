package no.hiof.discgolfapp.screens.takescore

import android.util.Log
import androidx.lifecycle.ViewModel
import no.hiof.discgolfapp.model.ScoreCard

class TakeScoreViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    var scoreCard : ScoreCard? = null
    var score = 0
    var par = 0
    var holeNumber = 0

    init {
        Log.i("TakeScoreViewModel", "TakeScoreViewModel created")
    }
}