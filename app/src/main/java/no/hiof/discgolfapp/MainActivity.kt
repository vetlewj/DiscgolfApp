package no.hiof.discgolfapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import no.hiof.discgolfapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var auth: FirebaseAuth
    private lateinit var authStateListener: AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController
        val drawerLayout = binding.drawerLayout

        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)

        val db = Firebase.firestore

        val topAppBar = binding.topAppBar
        topAppBar.setNavigationOnClickListener {
            Log.d("MainActivity", "Navigation icon clicked")
            drawerLayout.open()
        }

        binding.bottomNavView.setupWithNavController(navController)
        binding.navView.setupWithNavController(navController)

        createAuthenticationListener()

    }
    private fun createAuthenticationListener() {
        authStateListener = FirebaseAuth.AuthStateListener {
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
        auth.addAuthStateListener(authStateListener)
    }

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.onSignInResult(res)
    }

    private fun createSignInIntent(){
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()
        signInLauncher.launch(signInIntent)
    }

    private fun onSignInResult (result: FirebaseAuthUIAuthenticationResult){
        val response = result.idpResponse
        if (result.resultCode == RESULT_OK){
            //Login successfull
            val user = FirebaseAuth.getInstance().currentUser
            Toast.makeText(this, this.getString(R.string.sign_in) + user?.displayName, Toast.LENGTH_SHORT).show()
        }
        else {
            //Login failed.
            Log.d("Autenticate User", "Sign in failed")
        }
    }






}