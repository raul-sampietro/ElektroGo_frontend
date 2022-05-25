package elektrogo.front.ui.chatConversation


import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import elektrogo.front.controller.FrontendController
import elektrogo.front.model.CarPooling
import elektrogo.front.model.RatingAvg
import kotlinx.coroutines.runBlocking

class AddMemberDialogViewModel :  ViewModel(){
    fun getUserCreatedTrips(username: String): Pair <Int, ArrayList<CarPooling>> = runBlocking{
        return@runBlocking FrontendController.getUserCreatedTrips(username)
    }

   // fun addMemberToATrip (username: String, trip : CarPooling) : Int = runBlocking {
     //   return@runBlocking FrontendController.addMemberToATrip(username, trip)
    //}
}