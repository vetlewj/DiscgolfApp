package no.hiof.discgolfapp.screens.play.takescore

import android.util.Log
import androidx.lifecycle.ViewModel
import no.hiof.discgolfapp.model.ScoreCard

class TakeScoreViewModel : ViewModel() {
    var scoreCard : ScoreCard? = null
    var score = 0
    var par = 0
    var holeNumber = 0
    var distance = 0
    var totalScore = 0
    var totalPar = 0

    init {
        Log.i("TakeScoreViewModel", "TakeScoreViewModel created")
    }
}