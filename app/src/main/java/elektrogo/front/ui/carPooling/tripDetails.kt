/**
 * @file tripDetails.kt
 * @author Marina Alapont
 * @date 28/04/2022
 * @brief Implementacio d'una classe per tal de veure la informacio detallada d'un trajecte.
 */
package elektrogo.front.ui.carPooling

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import elektrogo.front.R
import elektrogo.front.controller.session.SessionController
import java.text.SimpleDateFormat

/**
 * @brief La clase tripDetails es l'activity on es mostra els detalls del trajecte seleccionat.
 */
class tripDetails : AppCompatActivity() {

    /**
     * @brief Instancia de la classe filterTripsViewModel.
     */
    private var viewModel: tripDetailsViewModel = tripDetailsViewModel()

    /**
     * @brief Metode que s'executa un cop l'activity ha estat creada. S'encarrega de mostrar per pantalla l'informacio rebuda per parametres del trajecte a veure'n els detalls.
     * @param savedInstanceState Estat de la instancia.
     * @pre
     * @post capta l'informacio pasada desde el fragment filterTripsFragment i la mostra per pantalla.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.trip_details)
        val username = intent.getStringExtra("username")
        val startDate = intent.getStringExtra("startDate")
        var startTime = intent.getStringExtra("startTime")
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
        val destinationFull : TextView = this.findViewById(R.id.destinationFull)
        val originFull : TextView = this.findViewById(R.id.originFull)


        usernameText.text = username
        var dateTmp = startDate
        val input = SimpleDateFormat("yyyy-MM-dd")
        val output = SimpleDateFormat("dd/MM/yyyy")
        val oneWayTripDate = input.parse(dateTmp) // parse input
        dateTmp = output.format(oneWayTripDate)
        startDateText.text = dateTmp // format output

        startTimeText.text= startTime!!.substring(0, startTime!!.length-3)
        var occupied : String = (offeredSeats - occupiedSeats).toString()
        occupied += "/"
        occupied += offeredSeats.toString()
        seatsText.text = occupied
        restrictionText.text = restrictions
        detailsText.text=details
        origin.text = originString
        destination.text=destinationString

        var originBrief : String
        if (originString!!.length > 20){
            originBrief = originString.substring(0, 20)
            originBrief += "..."
            origin.text = originBrief
        }
        else origin.text = originString
        originFull.text = originString
        var destinationBrief : String
        if (destinationString!!.length > 20){
            destinationBrief = destinationString.substring(0, 20)
            destinationBrief += "..."
            destination.text = destinationBrief
        }
        else destination.text = destinationString
        destinationFull.text=destinationString

        val ratingPair = viewModel.getRating(username!!)
        if (ratingPair.first != 200) {
            Toast.makeText(this, "Hi ha hagut un error, intenta-ho més tard", Toast.LENGTH_LONG).show()
        } else renderRating(ratingPair.second!!.ratingValue)
        val numValorations : TextView = this.findViewById(R.id.numberValorations)
        numValorations.text = "(${ratingPair.second!!.numberOfRatings})"

        val imageViewProfile : ImageView = this.findViewById(R.id.profile_imageDetails)
        val imagePath = viewModel.getUsersProfilePhoto(username)
        if (!imagePath.equals("null")  or !imagePath.equals("")) Picasso.get().load(imagePath).into(imageViewProfile)
        else imageViewProfile.setImageResource(R.drawable.avatar)

        val shareButton : ImageButton = this.findViewById(R.id.shareButton)
        shareButton.setOnClickListener {
            val  myIntent : Intent = Intent(Intent.ACTION_SEND)
            myIntent.setType("text/plain")
            var shareBody :String = ""
            if (SessionController.getUsername(this) == username ){
                shareBody = getString(R.string.shareOwnTrip, dateTmp,startTime.substring(0, startTime.length-3),originString,destinationString, (offeredSeats-occupiedSeats).toString() )
            }
            else shareBody=""
            myIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
            startActivity(Intent.createChooser(myIntent, "Share using"))
        }

    }
    /**
     * @brief Metode que s'encarrega de renderitzar les estrelles que te l'usuari segons la valoracio mitjana que rep.
     * @pre
     * @post Es mostren les estrelles segons la valoracio que l'usuari te.
     */
    private fun renderRating(ratingPassed: Double) {
        val star1 : ImageView = this.findViewById(R.id.estrella1Details)
        val star2 : ImageView = this.findViewById(R.id.estrella2Details)
        val star3 : ImageView = this.findViewById(R.id.estrella3Details)
        val star4 : ImageView = this.findViewById(R.id.estrella4Details)
        val star5 : ImageView = this.findViewById(R.id.estrella5Details)
        var rating = ratingPassed/2
        var decimalValue =    rating - rating.toInt()
        var enterValue = rating.toInt()

        when (enterValue) {
            0-> {
                if (decimalValue >= 0.25 && decimalValue < 0.75 ) {
                    star1.setImageResource(R.drawable.ic_starmigplena)
                }
                else if (decimalValue >= 0.75 ){
                    star1.setImageResource(R.drawable.ic_starplena)
                }
                else {
                    star1.setImageResource(R.drawable.ic_starbuida)
                }
                star2.setImageResource(R.drawable.ic_starbuida)
                star3.setImageResource(R.drawable.ic_starbuida)
                star4.setImageResource(R.drawable.ic_starbuida)
                star5.setImageResource(R.drawable.ic_starbuida)
            }
            1 -> {
                star1.setImageResource(R.drawable.ic_starplena)
                if (decimalValue >= 0.25 && decimalValue < 0.75 ) {
                    star2.setImageResource(R.drawable.ic_starmigplena)
                }
                else if (decimalValue >= 0.75 ){
                    star2.setImageResource(R.drawable.ic_starplena)
                }
                else {
                    star2.setImageResource(R.drawable.ic_starbuida)
                }
                star3.setImageResource(R.drawable.ic_starbuida)
                star4.setImageResource(R.drawable.ic_starbuida)
                star5.setImageResource(R.drawable.ic_starbuida)
            }
            2 -> {
                star1.setImageResource(R.drawable.ic_starplena)
                star2.setImageResource(R.drawable.ic_starplena)
                if (decimalValue >= 0.25 && decimalValue < 0.75 ) {
                    star3.setImageResource(R.drawable.ic_starmigplena)
                }
                else if (decimalValue >= 0.75 ){
                    star3.setImageResource(R.drawable.ic_starplena)
                }
                else {
                    star3.setImageResource(R.drawable.ic_starbuida)
                }
                star4.setImageResource(R.drawable.ic_starbuida)
                star5.setImageResource(R.drawable.ic_starbuida)
            }
            3 -> {
                star1.setImageResource(R.drawable.ic_starplena)
                star2.setImageResource(R.drawable.ic_starplena)
                star3.setImageResource(R.drawable.ic_starplena)
                if (decimalValue >= 0.25 && decimalValue < 0.75 ) {
                    star4.setImageResource(R.drawable.ic_starmigplena)
                }
                else if (decimalValue >= 0.75 ){
                    star4.setImageResource(R.drawable.ic_starplena)
                }
                else {
                    star4.setImageResource(R.drawable.ic_starbuida)
                }
                star5.setImageResource(R.drawable.ic_starbuida)
            }
            4 -> {
                star1.setImageResource(R.drawable.ic_starplena)
                star2.setImageResource(R.drawable.ic_starplena)
                star3.setImageResource(R.drawable.ic_starplena)
                star4.setImageResource(R.drawable.ic_starplena)

                if (decimalValue >= 0.25 && decimalValue < 0.75 ) {
                    star5.setImageResource(R.drawable.ic_starmigplena)
                }
                else if (decimalValue >= 0.75 ){
                    star5.setImageResource(R.drawable.ic_starplena)
                }
                else {
                    star5.setImageResource(R.drawable.ic_starbuida)
                }
            }
            5 -> {
                star1.setImageResource(R.drawable.ic_starplena)
                star2.setImageResource(R.drawable.ic_starplena)
                star3.setImageResource(R.drawable.ic_starplena)
                star4.setImageResource(R.drawable.ic_starplena)
                star5.setImageResource(R.drawable.ic_starplena)
            }
        }
    }

}