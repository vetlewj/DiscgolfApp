package no.hiof.discgolfapp.services

import android.content.Context
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
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

    private val _coursesByCountryCodeAndWithSameParentID: MutableLiveData<ArrayList<Course>?> = MutableLiveData()
    val coursesByCountryCodeAndWithSameParentID: LiveData<ArrayList<Course>?> = _coursesByCountryCodeAndWithSameParentID

    private val _coursesByCountryCodeAndWithSameParentIDWithHoles: MutableLiveData<ArrayList<Course>?> = MutableLiveData()
    val coursesByCountryCodeAndWithSameParentIDWithHoles: LiveData<ArrayList<Course>?> = _coursesByCountryCodeAndWithSameParentIDWithHoles


    fun fetchCourses(coursesCode: String, courseType: CourseType, context: Context) {
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
            val response = repository.getCoursesByCountryCode(coursesCode, courseType, context)

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

        fun fetchCourse(CourseID: String, context: Context) {

                // Checking courses exists in cache
                val cachedCourse = CoursesCache.courseMap[CourseID]
                if (cachedCourse != null) {
                    _courseByIDLiveData.postValue(cachedCourse)
                    return
                }

                viewModelScope.launch {
                    val response = repository.getCourseByID(CourseID, context)

                    _courseByIDLiveData.postValue(response)

                    // Updating the cache
                    response?.let {
                        CoursesCache.courseMap[CourseID] = response
                    }
                }
            }

        fun fetchAdditionalInfoFromCoursesWithSameParentID(countryCode: String, parentID: Int, lifecycleOwner: LifecycleOwner, context: Context ) {
                val listOfCoursesWithSameParentIDWithHoles = ArrayList<Course>()

                fetchAllCoursesWithSameParentID(countryCode, parentID, context)
                coursesByCountryCodeAndWithSameParentID.observe(lifecycleOwner) { listOfCoursesWithSameParentID ->
                     viewModelScope.launch {
                        listOfCoursesWithSameParentID!!.forEach { course ->

                            fetchCourseWithHoles(course.uid.toString(), context)?.let {
                                listOfCoursesWithSameParentIDWithHoles.add(it)
                            }
                        }
                            _coursesByCountryCodeAndWithSameParentIDWithHoles.postValue(
                                listOfCoursesWithSameParentIDWithHoles
                            )
                    }

                }
        }

    private fun fetchAllCoursesWithSameParentID(courseCode: String, parentID: Int, context: Context)    {
        viewModelScope.launch {
            val response = repository.getCoursesByCountryCodeAndWithSameParentID(courseCode, parentID, context)

            _coursesByCountryCodeAndWithSameParentID.postValue(response)
        }
    }

    private suspend fun fetchCourseWithHoles(CourseID: String, context: Context): Course? {
        // Checking courses exists in cache
        val cachedCourse = CoursesCache.courseMap[CourseID]
        if (cachedCourse != null) {
            return cachedCourse
        }

        val response = repository.getCourseByID(CourseID, context)
        // Updating the cache
        response?.let {
            CoursesCache.courseMap[CourseID] = response
        }

        return response

    }


        fun fetchWeather(lat: String, lon: String) {
            viewModelScope.launch {
                val response = repository.getWeatherByCoordinates(lat, lon)

                _weatherByCoordinatesLiveData.postValue(response)

            }
        }

        fun getSortedCoursesByDistance(lat: Double, lon: Double, lifecycleOwner: LifecycleOwner, context: Context) {
            if (_sortedCourseList.value?.isNotEmpty() == true) {
                return
            } else {
                fetchCourses("NO", CourseType.TYPE2, context)
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
            return ArrayList(sortedCourses.subList(0, 50))
        }
    }

