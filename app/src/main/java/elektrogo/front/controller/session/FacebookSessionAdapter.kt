package elektrogo.front.controller.session

import android.content.Context
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.facebook.GraphResponse
import elektrogo.front.model.User
import org.json.JSONObject

class FacebookSessionAdapter : Session {
    override fun getId(context: Context): String? {
        lateinit var id: String
        val request = GraphRequest.newMeRequest(
            AccessToken.getCurrentAccessToken()
        ) { obj, response -> id = obj!!.optString("id") }
        request.executeAsync()
        return id
    }

    override fun getProvider(context: Context): String {
        return "FACEBOOK"
    }

    override fun getEmail(context: Context): String? {
        TODO("Not yet implemented")
    }

    override fun getName(context: Context): String? {
        TODO("Not yet implemented")
    }

    override fun getGivenName(context: Context): String? {
        TODO("Not yet implemented")
    }

    override fun getFamilyName(context: Context): String? {
        TODO("Not yet implemented")
    }

    override fun getImageUrl(context: Context): String? {
        TODO("Not yet implemented")
    }

}