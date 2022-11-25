package no.hiof.discgolfapp.screens.courses

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import no.hiof.discgolfapp.R
import no.hiof.discgolfapp.adapter.CourseRecyclerAdapter
import no.hiof.discgolfapp.databinding.FragmentCoursesOverviewListBinding
import no.hiof.discgolfapp.helper.CourseType
import no.hiof.discgolfapp.services.SharedViewModel


class CoursesOverviewListFragment : Fragment() {

    private var fragmentBinding: FragmentCoursesOverviewListBinding? = null

    val viewModel: SharedViewModel by lazy {
        ViewModelProvider(this).get(SharedViewModel::class.java)
    }

    // kopiert fra dokumentasjonen i firebase: https://firebase.google.com/docs/cloud-messaging/android/client
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // FCM SDK (and your app) can post notifications.
        } else {
            // TODO: Inform user that that your app will not show notifications.
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_courses_overview_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentCoursesOverviewListBinding.bind(view)
        fragmentBinding = binding
        askNotificationPermission()

        viewModel.fetchCourses("NO", CourseType.TYPE1_AND_TYPE2_WITH_NO_PARENT, requireContext())
        viewModel.coursesByCountryCodeLiveData.observe(viewLifecycleOwner) { listOfCourses ->
            if (listOfCourses == null) {
                Toast.makeText(view.context, "network call was unsuccessful", Toast.LENGTH_SHORT)
                    .show()
                return@observe
            }

            binding.courseRecyclerView.adapter =
                CourseRecyclerAdapter(listOfCourses, View.OnClickListener { it ->
                    val position = binding.courseRecyclerView.getChildAdapterPosition(it)

                    val selectedCourse = listOfCourses[position]

                    val action =
                        CoursesOverviewListFragmentDirections.actionCoursesOverviewListFragmentToCourseInfoFragment()
                    action.let {
                        it.uid = selectedCourse.uid
                        it.courseName = selectedCourse.name
                        it.type = selectedCourse.type!!
                        it.latitude = try {
                            selectedCourse.latitude!!.toFloat()
                        } catch (e: NullPointerException) {
                            1000F
                        }
                        it.longitude = try {
                            selectedCourse.longitude!!.toFloat()
                        } catch (e: NullPointerException) {
                            1000F
                        }
                    }
                    findNavController().navigate(action)
                })
            binding.courseRecyclerView.layoutManager = GridLayoutManager(context, 1)

        }

        binding.coursesOverviewListToMapSwitch.setOnCheckedChangeListener { compoundButton, b ->

            findNavController().navigate(R.id.action_coursesOverviewListFragment_to_courseMapsFragment)
        }

    }

    // Hentet fra dokumentasjonen: https://firebase.google.com/docs/cloud-messaging/android/client
    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (view?.let { ContextCompat.checkSelfPermission(it.context, Manifest.permission.POST_NOTIFICATIONS) } ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

}