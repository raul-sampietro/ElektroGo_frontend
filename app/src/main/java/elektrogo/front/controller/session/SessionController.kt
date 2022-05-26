package elektrogo.front.controller.session

import android.content.Context
import elektrogo.front.controller.FrontendController
import kotlinx.coroutines.runBlocking

object SessionController {
    private var currentSession: Session? = null;

    fun setCurrentSession(session: Session?) {
        currentSession = session
    }

    fun isLogged(): Boolean {
        return currentSession != null
    }

    fun getId(context: Context): String? {
        return currentSession!!.getId(context)
    }

    fun getProvider(context: Context): String? {
        return currentSession!!.getProvider(context)
    }

    fun getUsername(context: Context): String = runBlocking {
        FrontendController.getUserById(getId(context)!!, getProvider(context)!!)!!.username
    }

    fun getEmail(context: Context): String? {
        return currentSession!!.getEmail(context)
    }

    fun getName(context: Context): String? {
        return currentSession!!.getName(context)
    }

    fun getGivenName(context: Context): String? {
        return currentSession!!.getGivenName(context)
    }

    fun getFamilyName(context: Context): String? {
        return currentSession!!.getFamilyName(context)
    }

    fun getImageUrl(context: Context): String? {
        return currentSession!!.getImageUrl(context)
    }
}