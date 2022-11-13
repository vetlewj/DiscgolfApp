package no.hiof.discgolfapp.services

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import no.hiof.discgolfapp.helper.CourseType
import no.hiof.discgolfapp.helper.DistanceMeasure
import no.hiof.discgolfapp.model.Course
import no.hiof.discgolfapp.model.Weather
import no.hiof.discgolfapp.services.api.cache.CoursesCache

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


    fun fetchCourses(coursesCode: String, courseType: CourseType) {
        // Checking courses exists in cache
        if (courseType.type.equals("1")) {
            val cachedCourse = CoursesCache.listOfCourseMapType1AndType2WithNoParent[coursesCode]
            if (cachedCourse != null) {
                _coursesByCountryCodeLiveData.postValue(cachedCourse)
                return
            }
        } else {
            val cachedCourse = CoursesCache.listOfCourseMapType2[coursesCode]
            if (cachedCourse != null) {
                _coursesByCountryCodeLiveData.postValue(cachedCourse)
                return
            }
        }

        viewModelScope.launch {
            val response = repository.getCoursesByCountryCode(coursesCode, courseType)

            _coursesByCountryCodeLiveData.postValue(response)
                // Updating the cache
                if (courseType.type.equals("1"))
                    response?.let {
                        CoursesCache.listOfCourseMapType1AndType2WithNoParent[coursesCode] = response
                    } else {
                    response?.let {
                        CoursesCache.listOfCourseMapType2[coursesCode] = response
                    }
                }
            }
        }

//    fun fetchAllType2ConnectedToType1Courses(args.uid)    {
//        viewModelScope.launch {
//            val response = repository.getCoursesByCountryCode(coursesCode, courseType)
//
//            _coursesByCountryCodeLiveData.postValue(response)
//        }
//    }

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
                fetchCourses("NO", CourseType.TYPE2)
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
            val sortedCourses = ArrayList<Course>(courses)
            sortedCourses.sortBy {
                DistanceMeasure.getDistanceToPositionInMeters(
                    lat, lon,
                    it.latitude?.toDouble() ?: 0.0, it.longitude?.toDouble() ?: 0.0
                )
            }
            return sortedCourses
        }
    }

