package elektrogo.front.ui.carPooling

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import elektrogo.front.controller.FrontendController
import kotlinx.coroutines.runBlocking

class FiltrarTrajectesViewModel : ViewModel() {
    fun askForTrips(latLngOrigin: LatLng?, latLngDestination: LatLng?, dateIni:String?, startTimeMin:String?, startTimeMax:String?): ArrayList<CarPooling> = runBlocking{
        return@runBlocking FrontendController.getTrips(latLngOrigin!!.latitude, latLngOrigin.longitude, latLngDestination!!.latitude,latLngDestination.longitude,dateIni, startTimeMin, startTimeMax)
    }

    fun getRating(username: String): Double = runBlocking {
        return@runBlocking FrontendController.getRating(username)
    }
    fun bitmapFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)
        vectorDrawable!!.setBounds(
            0,
            0,
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight
        )
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }
}