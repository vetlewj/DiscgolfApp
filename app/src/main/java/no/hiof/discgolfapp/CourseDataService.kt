package no.hiof.discgolfapp

import android.content.Context
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import no.hiof.discgolfapp.model.Course
import org.json.JSONArray
import org.json.JSONException

class CourseDataService(var context: Context) {
    companion object {
        const val QUERY_FOR_COURSES = "https://discgolfmetrix.com/api.php?content=courses_list&country_code="
        const val COUNTRY_CODE = "NO"
    }

    interface ListOfCoursesByCountryCodeResponse {
        fun onError(message: String?)
        fun onResponse(courseModels: ArrayList<Course?>)
    }

    fun getListOfCourses(countryCode: String, coursesByCountryCodeResponse: ListOfCoursesByCountryCodeResponse) {
        val url = QUERY_FOR_COURSES + countryCode
        val courses: ArrayList<Course?> = ArrayList()

        val request = JsonObjectRequest(Request.Method.GET, url, null, { response ->

            val coursesList: JSONArray = response.getJSONArray("courses")
            //val courseObject = coursesList.getJSONObject(0)

            for (i in 1..coursesList.length()) {
                val courseObject = coursesList.getJSONObject(i -1)
                val course = Course(
                    courseObject.getString("ID").toInt(),
                    courseObject.getString("Name"),
                    null,
                    null,
                    courseObject.getString("Area"),
                    courseObject.getString("City"),
                    courseObject.getString("Location"),
                    if (courseObject.getString("X").equals("")) null else courseObject.getString("X").toFloat(),
                    if (courseObject.getString("Y").equals("")) null else courseObject.getString("Y").toFloat()
                )
                Course.addCourseToAllCourses(course)
                //courses.add(course)
            }


            coursesByCountryCodeResponse.onResponse(Course.getAllCourses())

        }, { error ->
                coursesByCountryCodeResponse.onError(message = "Something went wrong")
        })

        RequestQueueSingleton.getInstance(context).addToRequestQueue(request)
    }

}