package elektrogo.front.ui.vehicleList

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import elektrogo.front.controller.FrontendController
import elektrogo.front.model.Driver
import kotlinx.coroutines.runBlocking

class NewDriverViewModel : ViewModel() {
    fun sendDriverInfo(driver: Driver, username: String): Int  = runBlocking{
        return@runBlocking  FrontendController.newDriver(driver, username)
    }

    fun saveDriverFrontImage(username: String, frontImage: Bitmap) = runBlocking {
        FrontendController.sendFrontPhoto(username, frontImage)
    }

    fun saveDriverReverseImage(username: String, reverseImage: Bitmap) = runBlocking {
        FrontendController.sendFrontPhoto(username, reverseImage)
    }
}