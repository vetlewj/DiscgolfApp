package no.hiof.discgolfapp.services

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.MetadataChanges
import com.google.firebase.firestore.ktx.toObjects
import no.hiof.discgolfapp.model.Course
import no.hiof.discgolfapp.model.ScoreCard
import kotlin.math.roundToInt

class StoredStatisticsViewModel : ViewModel() {
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()


    private var _scoreCards: MutableLiveData<List<ScoreCard>> = MutableLiveData()
    val scoreCards: LiveData<List<ScoreCard>> = _scoreCards

    private var _childCoursesScoreCardsMap: MutableLiveData<MutableMap<Int, ArrayList<ScoreCard>>> =
        MutableLiveData()
    val childCoursesScoreCardsMap: LiveData<MutableMap<Int, ArrayList<ScoreCard>>> = _childCoursesScoreCardsMap

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
        ratingResult1: Double,
        ratingResult2: Double
    ): Int {
        return ((ratingValue2.minus(ratingValue1)) * (result.minus(ratingResult1)) / (ratingResult2.minus(
            ratingResult1
        )) + ratingValue1).roundToInt()
    }

    fun fetchScoreCourseListMap(listOfUidsOfCoursesWithSameParrentId: ArrayList<Int>) {
        val storedCards = firestore.collection("scorecards")
            .whereEqualTo("playerId", firebaseAuth.currentUser?.uid)
            .whereEqualTo("finished", true)
            .whereIn("courseId", listOfUidsOfCoursesWithSameParrentId)
        storedCards.addSnapshotListener(MetadataChanges.INCLUDE) { value, error ->
            if (error != null) {
                Log.d("StoredStatistics", "Listen failed.", error)
                return@addSnapshotListener
            }
            if (value != null) {
                val scoreCards = value.toObjects<ScoreCard>()
                val scoreCardsMap = mutableMapOf<Int, ArrayList<ScoreCard>>()
                for (courseId in listOfUidsOfCoursesWithSameParrentId) {
                    scoreCardsMap[courseId] = scoreCards.filter { it.courseId == courseId } as ArrayList<ScoreCard>
                }
                _childCoursesScoreCardsMap.postValue(scoreCardsMap)
            }
        }
    }

    fun getAverageScoreMap(scoreCardsMap: MutableMap<Int, java.util.ArrayList<ScoreCard>>?): MutableMap<Int, Int>? {
        if (scoreCardsMap != null) {
            val averageScoreMap = mutableMapOf<Int, Int>()
            for (courseId in scoreCardsMap.keys) {
                val scoreCards = scoreCardsMap[courseId]
                if (scoreCards != null) {
                    val averageScore = scoreCards.map { it.totalScore }.average().roundToInt()
                    averageScoreMap[courseId] = averageScore
                }
            }
            return averageScoreMap
        }
        return null
    }

    fun getBestScoreMap(scoreCardsMap: MutableMap<Int, java.util.ArrayList<ScoreCard>>?): MutableMap<Int, Int>? {
        if (scoreCardsMap != null) {
            val bestScoreMap = mutableMapOf<Int, Int>()
            for (courseId in scoreCardsMap.keys) {
                val scoreCards = scoreCardsMap[courseId]
                if (scoreCards != null) {
                    val bestScore = scoreCards.minByOrNull { it.totalScore }?.totalScore ?: 0
                    bestScoreMap[courseId] = bestScore
                }
            }
            return bestScoreMap
        }
        return null
    }

    fun getLastScoreMap(scoreCardsMap: MutableMap<Int, java.util.ArrayList<ScoreCard>>?): MutableMap<Int, Int>? {
        if (scoreCardsMap != null) {
            val lastScoreMap = mutableMapOf<Int, Int>()
            for (courseId in scoreCardsMap.keys) {
                val scoreCards = scoreCardsMap[courseId]
                if (scoreCards != null) {
                    val lastScore = scoreCards.maxByOrNull { it.date }?.totalScore ?: 0
                    lastScoreMap[courseId] = lastScore
                }
            }
            return lastScoreMap
        }
        return null
    }


}