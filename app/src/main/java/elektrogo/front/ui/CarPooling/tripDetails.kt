package elektrogo.front.ui.CarPooling

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import elektrogo.front.R


class tripDetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.trip_details)
        val username = intent.getStringExtra("username")
        val startDate = intent.getStringExtra("startDate")
        val startTime = intent.getStringExtra("startTime")
        val offeredSeats = intent.getStringExtra("offeredSeats")
        val occupiedSeats = intent.getStringExtra("occupiedSeats")
        val restrictions = intent.getStringExtra("restrictions")
        val details = intent.getStringExtra("details")
        val latitudeOrigin = intent.getStringExtra("latitudeOrigin")
        val longitudeOrigin = intent.getStringExtra("longitudeOrigin")
        val originString = intent.getStringExtra("originString")
        val latitudeDestination = intent.getStringExtra("latitudeDestination")
        val longitudeDestination = intent.getStringExtra("longitudeDestination")
        val destinationString = intent.getStringExtra("destinationString")
        val vehicleNumberPlate = intent.getStringExtra("vehicleNumberPlate")

      //  val imageid = intent.getIntExtra("imageid", R.drawable.a)

    }
}