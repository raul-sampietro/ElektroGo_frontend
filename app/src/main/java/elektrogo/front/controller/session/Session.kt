package elektrogo.front.controller.session

import android.content.Context

interface Session {
    fun getId(context: Context): String?
    fun getProvider(context: Context): String?
    fun getEmail(context: Context): String?
    fun getName(context: Context): String?
    fun getGivenName(context: Context): String?
    fun getFamilyName(context: Context): String?
    fun getImageUrl(context: Context): String?
}