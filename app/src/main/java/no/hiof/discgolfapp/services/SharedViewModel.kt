package no.hiof.discgolfapp.services

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import no.hiof.discgolfapp.helper.response.GetListOfCoursesByCountryCodeResponse
import no.hiof.discgolfapp.model.Course

class SharedViewModel: ViewModel() {
    private val repository = SharedRepository();

    private val _coursesByCountryCodeLiveData = MutableLiveData<ArrayList<Course>?>()
    val coursesByCountryCodeLiveData: LiveData<ArrayList<Course>?> = _coursesByCountryCodeLiveData

    fun refreshCourses(coursesCode: String) {
        viewModelScope.launch {
            val response = repository.getCoursesByCountryCode(coursesCode)

            _coursesByCountryCodeLiveData.postValue(response)
        }

    }


}