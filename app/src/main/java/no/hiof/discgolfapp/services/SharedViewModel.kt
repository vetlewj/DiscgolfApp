package no.hiof.discgolfapp.services

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import no.hiof.discgolfapp.helper.DistanceMeasure
import no.hiof.discgolfapp.model.Course
import no.hiof.discgolfapp.model.Weather

class SharedViewModel : ViewModel() {
    private val repository = SharedRepository();

    private val _coursesByCountryCodeLiveData = MutableLiveData<ArrayList<Course>?>()
    val coursesByCountryCodeLiveData: LiveData<ArrayList<Course>?> = _coursesByCountryCodeLiveData

    private val _courseByIDLiveData = MutableLiveData<Course?>()
    val courseByIDLiveData: LiveData<Course?> = _courseByIDLiveData

    private val _weatherByCoordinatesLiveData = MutableLiveData<Weather?>()
    val weatherByCoordinatesLiveData: LiveData<Weather?> = _weatherByCoordinatesLiveData

    private val _sortedCourseList: MutableLiveData<ArrayList<Course>> = MutableLiveData()
    val sortedCourseList: LiveData<ArrayList<Course>> = _sortedCourseList


    fun fetchCourses(coursesCode: String) {

        // Checking courses exists in cache
        val cachedCourses = CoursesCache.listOfCourseMap[coursesCode]
        if (cachedCourses != null) {
            _coursesByCountryCodeLiveData.postValue(cachedCourses)
            return
        }

        // if not in cache, request character form API
        viewModelScope.launch {
            val response = repository.getCoursesByCountryCode(coursesCode)

            _coursesByCountryCodeLiveData.postValue(response)

            // Updating the cache
            response?.let {
                CoursesCache.listOfCourseMap[coursesCode] = response
            }
        }
    }

    fun fetchCourse(CourseID: String) {

        // Checking courses exists in cache
        val cachedCourse = CoursesCache.courseMap[CourseID]
        if (cachedCourse != null) {
            _courseByIDLiveData.postValue(cachedCourse)
            return
        }

        viewModelScope.launch {
            val response = repository.getCourseByID(CourseID)

            _courseByIDLiveData.postValue(response)

            // Updating the cache
            response?.let {
                CoursesCache.courseMap[CourseID] = response
            }
        }
    }

    fun fetchWeather(lat: String, lon: String) {
        viewModelScope.launch {
            val response = repository.getWeatherByCoordinates(lat, lon)

            _weatherByCoordinatesLiveData.postValue(response)

        }
    }

    fun getSortedCoursesByDistance(lat: Double, lon: Double, lifecycleOwner: LifecycleOwner) {
        if (_sortedCourseList.value?.isNotEmpty() == true) {
            return
        } else {
            fetchCourses("NO")

        }
        coursesByCountryCodeLiveData.observe(lifecycleOwner) { it ->
            if (it != null) {
                _sortedCourseList.value =
                    getSortedCoursesByDistance(it, lat, lon)
            }
        }
    }

    private fun getSortedCoursesByDistance(
        courses: ArrayList<Course>,
        lat: Double,
        lon: Double
    ): ArrayList<Course> {
        Log.d(
            "SharedViewModel",
            "Sorting courses by distance from $lat, $lon, ${_sortedCourseList.value?.size}"
        )
        courses.sortBy {
            DistanceMeasure.getDistanceToPositionInMeters(
                lat, lon,
                it.latitude?.toDouble() ?: 0.0, it.longitude?.toDouble() ?: 0.0
            )
        }
        return courses
    }
}