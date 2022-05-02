package elektrogo.front.controller.session

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn

class GoogleSessionAdapter : Session {
    override fun getId(context: Context): String? {
        return GoogleSignIn.getLastSignedInAccount(context)!!.id
    }

    override fun getProvider(context: Context): String {
        return "GOOGLE"
    }

    override fun getEmail(context: Context): String? {
        return GoogleSignIn.getLastSignedInAccount(context)!!.email
    }

    override fun getName(context: Context): String? {
        return GoogleSignIn.getLastSignedInAccount(context)!!.displayName
    }

    override fun getGivenName(context: Context): String? {
        return GoogleSignIn.getLastSignedInAccount(context)!!.givenName
    }

    override fun getFamilyName(context: Context): String? {
        return GoogleSignIn.getLastSignedInAccount(context)!!.familyName
    }

    override fun getImageUrl(context: Context): String {
        return GoogleSignIn.getLastSignedInAccount(context)!!.photoUrl.toString()
    }

}