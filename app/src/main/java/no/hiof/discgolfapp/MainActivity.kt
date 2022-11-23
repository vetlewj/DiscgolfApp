package no.hiof.discgolfapp

import android.content.ContentValues
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import no.hiof.discgolfapp.databinding.ActivityMainBinding
import no.hiof.discgolfapp.model.User
import java.util.Date

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var auth: FirebaseAuth
    private lateinit var authStateListener: AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth
        createAuthListener()
        // setFirestoreSettings()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController
        val drawerLayout = binding.drawerLayout

        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)


        val topAppBar = binding.topAppBar
        topAppBar.setNavigationOnClickListener {
            Log.d("MainActivity", "Navigation icon clicked")
            drawerLayout.open()
        }

        binding.bottomNavView.setupWithNavController(navController)
        binding.navView.setupWithNavController(navController)

    }

    private fun createAuthListener(){
        authStateListener = AuthStateListener {
            val firebaseUser = auth.currentUser
            if (firebaseUser == null) {
                createSignInIntent()
            } else {
                Log.d("Authenticate", "User: " + auth.currentUser?.email)
            }
        }
    }


    override fun onResume(){
        super.onResume()
        auth.addAuthStateListener(authStateListener)
    }

    override fun onPause(){
        super.onPause()
        auth.removeAuthStateListener(authStateListener)
    }

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.onSignInResult(res)
    }

    private fun createSignInIntent(){
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build(),
            AuthUI.IdpConfig.AnonymousBuilder().build()
        )
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()
        signInLauncher.launch(signInIntent)
    }

    private fun onSignInResult (result: FirebaseAuthUIAuthenticationResult){
        val response = result.idpResponse
        Log.d("Making user response", "$response")
        if (result.resultCode == RESULT_OK){
            //Login successfull
            val user = FirebaseAuth.getInstance().currentUser
            // Create a new user document in users collection if user is new
            if(response!!.isNewUser) {
                val db = Firebase.firestore

                val newUser = if(response.providerType == "anonymous") {
                    Toast.makeText(this, "${this.getString(R.string.sign_in)} gjest", Toast.LENGTH_SHORT).show()

                    User(
                        authUid = "${user?.uid}",
                        guest = true,
                        dateCreated = Date(),
                        name = null,
                        email = null,
                        pictureUrl = null,
                        scoreCards = ArrayList(),
                        discs = ArrayList(),
                        friends = null,
                        throws = ArrayList(),
                        friendsRequests = null
                    )
                } else {
                    Toast.makeText(this, "${this.getString(R.string.sign_in)}  ${user?.displayName}", Toast.LENGTH_SHORT).show()

                    User(
                        authUid = "${user?.uid}",
                        guest = false,
                        dateCreated = Date(),
                        name = "${user?.displayName}",
                        email = "${user?.email}",
                        pictureUrl = null,
                        scoreCards = ArrayList(),
                        discs = ArrayList(),
                        friends = ArrayList(),
                        throws = ArrayList(),
                        friendsRequests = ArrayList()
                    )
                }

                // Add a new document
                db.collection("users").document("${user?.uid}")
                    .set(newUser)
                    .addOnSuccessListener { documentReference ->
                        Log.d("Making user collection", "DocumentSnapshot added with ID: ${user?.uid}")
                    }
                    .addOnFailureListener { e ->
                        Log.w(ContentValues.TAG, "Error adding document", e)
                    }

            }


        }
        else {
            //Login failed.
            Log.d("Autenticate User", "Sign in failed")
        }
    }
    fun setFirestoreSettings(){
        val db = Firebase.firestore
        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
            .build()
        db.firestoreSettings = settings
    }
}