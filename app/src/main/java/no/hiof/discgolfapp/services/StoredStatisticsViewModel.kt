package no.hiof.discgolfapp.services

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObjects
import no.hiof.discgolfapp.model.Course
import no.hiof.discgolfapp.model.ScoreCard
import kotlin.math.roundToInt

class StoredStatisticsViewModel : ViewModel() {
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()


    private var _scoreCards: MutableLiveData<List<ScoreCard>> = MutableLiveData()
    val scoreCards: LiveData<List<ScoreCard>> = _scoreCards

    private var _avgScoresMap: MutableLiveData<MutableMap<Int, Int>> = MutableLiveData()
    val avgScoresMap: LiveData<MutableMap<Int, Int>> = _avgScoresMap

    private var _bestScoresMap: MutableLiveData<MutableMap<Int, Int>> = MutableLiveData()
    val bestScoresMap: LiveData<MutableMap<Int, Int>> = _bestScoresMap

    private var _lastScoresMap: MutableLiveData<MutableMap<Int, Int>> = MutableLiveData()
    val lastScoresMap: LiveData<MutableMap<Int, Int>> = _lastScoresMap

    private var retryCount = 0
    private var maxRetries = 5

    fun fetchCourseScoreCardsFromFireStore(courseId: Int) {
        Log.d("StoredStatistics", "getScoreCards: $courseId")
        val storedCards = firestore.collection("scorecards")
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
            fetchCourseScoreCardsFromFireStore(courseId)
            return getBestScoreForCourse(courseId)
        }
        Log.d(
            "StoredStatistics",
            "getBestScoreForCourse: No scorecards for course $courseId were found"
        )
        return 0
    }

    fun getAvgScoreForCourse(courseId: Int): Int {
        if (_scoreCards.value != null) {
            return _scoreCards.value?.map { it.totalScore }?.average()?.toInt() ?: 0
        }
        if (retryCount < maxRetries) {
            retryCount++
            fetchCourseScoreCardsFromFireStore(courseId)
            return getAvgScoreForCourse(courseId)
        }
        Log.d(
            "StoredStatistics",
            "getAvgScoreForCourse: No scorecards for course $courseId were found"
        )
        return 0
    }

    fun getLastScoreForCourse(courseId: Int): Int {
        if (_scoreCards.value != null) {
            return _scoreCards.value?.maxByOrNull { it.date }?.totalScore ?: 0
        }
        if (retryCount < maxRetries) {
            retryCount++
            fetchCourseScoreCardsFromFireStore(courseId)
            return getLastScoreForCourse(courseId)
        }
        Log.d(
            "StoredStatistics",
            "getLastScoreForCourse: No scorecards for course $courseId were found"
        )
        return 0
    }

    fun getRatingForRound(result: Int, course: Course): Int {
        if (course.ratingValue1 != null && course.ratingValue2 != null && course.ratingResult1 != null && course.ratingResult2 != null) {
            val rating =
                (course.ratingValue2 - course.ratingValue1) * (result - course.ratingResult1) / (course.ratingResult2 - course.ratingResult1) + course.ratingValue1
            return rating.roundToInt()
        }
        return 0
    }

    fun getRatingForRound(
        result: Int,
        ratingValue1: Double,
        ratingValue2: Double,
        ratingResult1: Int,
        ratingResult2: Int
    ): Int {
        return ((ratingValue2.minus(ratingValue1)) * (result.minus(ratingResult1)) / (ratingResult2.minus(
            ratingResult1
        )) + ratingValue1).roundToInt()
    }

    fun fetchAvgScoreCourseMap(courseId: Int) {
        val storedCards = firestore.collection("scorecards")
            .whereEqualTo("playerId", firebaseAuth.currentUser?.uid)
            .whereEqualTo("finished", true)
            .whereEqualTo("courseId", courseId)
            .get()
        storedCards.addOnSuccessListener { documents ->
            val scoreCards = documents.toObjects<ScoreCard>()
            val currentAvgScoresMap = _avgScoresMap.value ?: mutableMapOf()
            currentAvgScoresMap[courseId] = scoreCards.map { it.totalScore }.average().toInt()
            _avgScoresMap.value = currentAvgScoresMap
            Log.d("StoredStatistics", "fetchAvgScoreCourseMap: $currentAvgScoresMap")
        }
    }

    fun fetchBestScoreCourseMap(courseId: Int) {
        val storedCards = firestore.collection("scorecards")
            .whereEqualTo("playerId", firebaseAuth.currentUser?.uid)
            .whereEqualTo("finished", true)
            .whereEqualTo("courseId", courseId)
            .get()
        storedCards.addOnSuccessListener { documents ->
            val scoreCards = documents.toObjects<ScoreCard>()
            val currentBestScoresMap = _bestScoresMap.value ?: mutableMapOf()
            currentBestScoresMap[courseId] =
                scoreCards.minByOrNull { it.totalScore }?.totalScore ?: 0
            _bestScoresMap.value = currentBestScoresMap
            Log.d("StoredStatistics", "fetchBestScoreCourseMap: $currentBestScoresMap")
        }
    }

    fun fetchLastScoreCourseMap(courseId: Int) {
        val storedCards = firestore.collection("scorecards")
            .whereEqualTo("playerId", firebaseAuth.currentUser?.uid)
            .whereEqualTo("finished", true)
            .whereEqualTo("courseId", courseId)
            .get()
        storedCards.addOnSuccessListener { documents ->
            val scoreCards = documents.toObjects<ScoreCard>()
            val currentLastScoresMap = _lastScoresMap.value ?: mutableMapOf()
            currentLastScoresMap[courseId] = scoreCards.maxByOrNull { it.date }?.totalScore ?: 0
            _lastScoresMap.value = currentLastScoresMap
            Log.d("StoredStatistics", "fetchLastScoreCourseMap: $currentLastScoresMap")
        }
    }
}