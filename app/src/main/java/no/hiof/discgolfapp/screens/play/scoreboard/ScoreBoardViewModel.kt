package no.hiof.discgolfapp.screens.play.scoreboard

import android.util.Log
import androidx.lifecycle.ViewModel
import no.hiof.discgolfapp.model.ScoreCard

class ScoreBoardViewModel : ViewModel(){
    var scoreCard : ScoreCard? = null

    init {
        Log.i("ScoreBoardViewModel", "ScoreBoardViewModel created")
    }
}