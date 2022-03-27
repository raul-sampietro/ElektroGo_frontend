package elektrogo.front.ui.map
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import elektrogo.front.controller.FrontendController
import elektrogo.front.model.ChargingStation

import kotlinx.coroutines.runBlocking

class MapsFragmentViewModel : ViewModel() {

    /**
     * @brief Metode que obte els punts de carrega de la base de backend.
     * @pre
     * @return Retorna un ArrayList de les estacions de carrega que hi ha a la base de dades.
     */
    fun getStations() : ArrayList<ChargingStation> /*sintaxi per fer return*/ = runBlocking {
        FrontendController.getChargingPoints()
    }

    /**
     * @brief Metode que transforma un recurs vectorial en un Bitmap, per poder mostrar-ho al mapa.
     * @pre El context es correcte i el vector passat com a parametre existeix.
     * @return Retorna un BitMapDescriptor a partir del vector indicat.
     */
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