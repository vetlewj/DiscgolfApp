package no.hiof.discgolfapp.services

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import no.hiof.discgolfapp.model.Course

class SharedViewModel: ViewModel() {
    private val repository = SharedRepository();

    private val _coursesByCountryCodeLiveData = MutableLiveData<ArrayList<Course>?>()
    val coursesByCountryCodeLiveData: LiveData<ArrayList<Course>?> = _coursesByCountryCodeLiveData

    private val _courseByIDLiveData = MutableLiveData<Course?>()
    val courseByIDLiveData: LiveData<Course?> = _courseByIDLiveData

    fun refreshCourses(coursesCode: String) {

        // Checking courses exists in cache
        val cachedCourses = CoursesCache.listOfCourseMap[coursesCode]
        if(cachedCourses != null) {
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
        if(cachedCourse != null) {
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


}