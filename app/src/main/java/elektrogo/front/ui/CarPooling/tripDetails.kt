package elektrogo.front.ui.CarPooling

import android.os.Bundle
import android.widget.TextClock
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import elektrogo.front.R
import org.w3c.dom.Text


class tripDetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.trip_details)
        val username = intent.getStringExtra("username")
        val startDate = intent.getStringExtra("startDate")
        val startTime = intent.getStringExtra("startTime")
        val offeredSeats = intent.getIntExtra("offeredSeats", 1)
        val occupiedSeats = intent.getIntExtra("occupiedSeats", 1)
        val restrictions = intent.getStringExtra("restrictions")
        val details = intent.getStringExtra("details")
        val originString = intent.getStringExtra("originString")
        val destinationString= intent.getStringExtra("destinationString")
        val vehicleNumberPlate = intent.getStringExtra("vehicleNumberPlate")

        val usernameText :TextView  = this.findViewById(R.id.usernameDetails)
        val startDateText : TextView = this.findViewById(R.id.dateDetails)
        val startTimeText : TextView = this.findViewById(R.id.timeDetails)
        val seatsText : TextView = this.findViewById(R.id.occupiedseats)
        val restrictionText : TextView = this.findViewById(R.id.restrictionsInfo)
        val detailsText : TextView = this.findViewById(R.id.detailsInfo)
        val origin: TextView = this.findViewById(R.id.originDetails)
        val destination: TextView = this.findViewById(R.id.destDetails)

        usernameText.text = username
        startDateText.text=startDate
        startTimeText.text=startTime
        var occupied : String = (offeredSeats - occupiedSeats).toString()
        occupied += "/"
        occupied += offeredSeats.toString()
        seatsText.text = occupied
        restrictionText.text = restrictions
        detailsText.text=details
        origin.text = originString
        destination.text=destinationString


        //  val imageid = intent.getIntExtra("imageid", R.drawable.a)

    }
}