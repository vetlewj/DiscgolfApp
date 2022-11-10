package no.hiof.discgolfapp.services

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObjects
import no.hiof.discgolfapp.model.ScoreCard

class StoredStatisticsViewModel : ViewModel() {
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()


    private var _scoreCards: MutableLiveData<List<ScoreCard>> = MutableLiveData()
    val scoreCards: LiveData<List<ScoreCard>> = _scoreCards

    private var retryCount = 0
    private var maxRetries = 5

    fun getCourseScoreCardsFromFireStore(courseId: Int) {
        Log.d("StoredStatistics", "getScoreCards: $courseId")
        val storedCards = firestore.collection("scorecardsv1")
            .whereEqualTo("playerId", firebaseAuth.currentUser?.uid)
            .whereEqualTo("finished", true)
            .whereEqualTo("courseId", courseId)
            .get()
        storedCards.addOnSuccessListener { documents ->
            _scoreCards.postValue(documents.toObjects())
        }
    }

    fun getBestScoreForCourse(courseId: Int): Int {
        if (_scoreCards.value != null) {
            return _scoreCards.value?.minByOrNull { it.totalScore }?.totalScore ?: 0
        }
        if (retryCount < maxRetries) {
            retryCount++
            getCourseScoreCardsFromFireStore(courseId)
            return getBestScoreForCourse(courseId)
        }
        Log.d("StoredStatistics", "getBestScoreForCourse: No scorecards for course $courseId were found")
        return 0
    }

    fun getAvgScoreForCourse(courseId: Int): Int {
        if (_scoreCards.value != null) {
            return _scoreCards.value?.map { it.totalScore }?.average()?.toInt() ?: 0
        }
        if (retryCount < maxRetries) {
            retryCount++
            getCourseScoreCardsFromFireStore(courseId)
            return getAvgScoreForCourse(courseId)
        }
        Log.d("StoredStatistics", "getAvgScoreForCourse: No scorecards for course $courseId were found")
        return 0
    }
}