package elektrogo.front.ui.login

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import elektrogo.front.MainActivity
import elektrogo.front.R
import elektrogo.front.controller.FrontendController
import elektrogo.front.controller.session.FacebookSessionAdapter
import elektrogo.front.controller.session.GoogleSessionAdapter
import elektrogo.front.controller.session.SessionController
import kotlinx.coroutines.runBlocking
import java.util.*


class LoginFragment : Fragment() {

    private val RC_SIGN_IN = 123
    private lateinit var callbackManager: CallbackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_login, container, false)

        // Google login
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        // Build a GoogleSignInClient with the options specified by gso.
        val mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);

        // Set the dimensions of the sign-in button.
        val signInButtonGoogle: SignInButton = view.findViewById(R.id.sign_in_button)
        signInButtonGoogle.setSize(SignInButton.SIZE_WIDE)
        signInButtonGoogle.setOnClickListener {
            val signInIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

        // Facebook login
        val signInButtonFacebook: LoginButton = view.findViewById(R.id.login_button)
        signInButtonFacebook.setReadPermissions(listOf("public_profile", "email"));
        signInButtonFacebook.setFragment(this)
        callbackManager = CallbackManager.Factory.create();

        signInButtonFacebook.registerCallback(callbackManager, object : FacebookCallback<LoginResult?> {
            override fun onSuccess(loginResult: LoginResult?) {
                Toast.makeText(activity, loginResult!!.accessToken.userId, Toast.LENGTH_SHORT).show()
                val token = AccessToken.getCurrentAccessToken()
                if (token != null) {
                    // primer login de l'usuari
                    if (true/* !existsUser(Profile.getCurrentProfile()!!.id!!, "FACEBOOK")*/) {
                        val firstLoginFragment = FirstLoginFragment()
                        val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
                        transaction.replace(R.id.frame_container, firstLoginFragment)
                        transaction.addToBackStack(null)
                        transaction.commit()
                    // no es el primer login del user
                    } else {
                        SessionController.setCurrentSession(FacebookSessionAdapter())
                        val intent = Intent(requireActivity(), MainActivity::class.java)
                        startActivity(intent)
                    }
                }
            }

            override fun onCancel() {
                Toast.makeText(activity, "Login canceled", Toast.LENGTH_SHORT).show()
            }

            override fun onError(exception: FacebookException) {
                Toast.makeText(activity, "Exception rose: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
        })

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
        // Result returned from Facebook login
        else {
            callbackManager.onActivityResult(requestCode, resultCode, data)
        }
    }

    // Google login
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            // Signed in successfully, show authenticated UI.
            if (account != null) {
                // primer login de l'usuari
                if (!existsUser(account.id!!, "GOOGLE")) {
                    val firstLoginFragment = FirstLoginFragment()
                    val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
                    transaction.replace(R.id.frame_container, firstLoginFragment)
                    transaction.addToBackStack(null)
                    transaction.commit()
                // no es el primer login del user
                } else {
                    SessionController.setCurrentSession(GoogleSessionAdapter())
                    val intent = Intent(requireActivity(), MainActivity::class.java)
                    startActivity(intent)
                }
            }
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(ContentValues.TAG, "signInResult:failed code=" + e.statusCode)
        }
    }

    private fun existsUser(id: String, provider: String): Boolean = runBlocking {
        FrontendController.getUserById(id, provider) != null
    }
}