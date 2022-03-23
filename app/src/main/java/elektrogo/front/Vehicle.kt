package elektrogo.front

import android.graphics.Bitmap
import java.time.LocalDate

data class Vehicle(var brand : String,
                   var model : String,
                   var numberPlate : String,
                   var drivingRange : Int,
                   var fabricationYear : Int,
                   var seats : Int,
                   var picture : Bitmap?) {
}
