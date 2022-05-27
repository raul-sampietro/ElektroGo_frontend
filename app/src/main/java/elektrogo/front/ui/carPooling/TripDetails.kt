/**
 * @file tripDetails.kt
 * @author Marina Alapont
 * @date 28/04/2022
 * @brief Implementacio d'una classe per tal de veure la informacio detallada d'un trajecte.
 */
package elektrogo.front.ui.carPooling

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ViewManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import elektrogo.front.R
import elektrogo.front.controller.session.SessionController
import elektrogo.front.model.CarPooling
import elektrogo.front.model.User
import elektrogo.front.ui.chatConversation.ChatConversation
import java.text.SimpleDateFormat
import java.util.*


/**
 * @brief La clase tripDetails es l'activity on es mostra els detalls del trajecte seleccionat.
 */
class TripDetails : AppCompatActivity() {

    /**
     * @brief Instancia de la classe filterTripsViewModel.
     */
    private var viewModel: TripDetailsViewModel = TripDetailsViewModel()

    private lateinit var toolbar2 : Toolbar

    /**
     * @brief Metode que s'executa un cop l'activity ha estat creada. S'encarrega de mostrar per pantalla l'informacio rebuda per parametres del trajecte a veure'n els detalls.
     * @param savedInstanceState Estat de la instancia.
     * @pre
     * @post capta l'informacio pasada desde el fragment filterTripsFragment i la mostra per pantalla.
     */
    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.trip_details)
        toolbar2  = findViewById(R.id.toolbar_main)
        toolbar2.title= getString(R.string.detailsLabel)
        setSupportActionBar(toolbar2)

        val tripSerialized = intent.getStringExtra("Trip")
        val trip : CarPooling = Gson().fromJson(tripSerialized, CarPooling::class.java)
        Log.i("llego", trip.id.toString())
        val username= trip!!.username
        val startDate = trip.startDate
        val startTime = trip.startTime
        val cancelDate = trip.cancelDate
        val state = trip.state
        val offeredSeats = trip.offeredSeats
        val occupiedSeats = trip.occupiedSeats
        val restrictions = trip.restrictions
        val details = trip.details
        val originString = trip.origin
        val destinationString= trip.destination
        val vehicleNumberPlate = trip.vehicleNumberPlate
        val latDest = trip.latitudeDestination
        val lonDest = trip.longitudeDestination
        val id = trip.id

        val usernameText :TextView  = this.findViewById(R.id.usernameDetails)
        val startDateText : TextView = this.findViewById(R.id.dateDetails)
        val startTimeText : TextView = this.findViewById(R.id.timeDetails)
        val seatsText : TextView = this.findViewById(R.id.occupiedseats)
        val restrictionText : TextView = this.findViewById(R.id.restrictionsInfo)
        val detailsText : TextView = this.findViewById(R.id.detailsInfo)
        val destinationFull : TextView = this.findViewById(R.id.destinationFull)
        val originFull : TextView = this.findViewById(R.id.originFull)
        val qaImage : ImageView = this.findViewById(R.id.airqualityImage)
        //Obtenci dels membres que participen en el trajecte
        val listView: ListView = this.findViewById(R.id.listMembers)
        var memberList : ArrayList<User>
        var resultDefault : Pair <Int, ArrayList<User>> = viewModel.askForMembersOfATrip(id!!)
        if (resultDefault.first != 200) {
            Toast.makeText(this, "Hi ha hagut un error, intenta-ho més tard2", Toast.LENGTH_LONG).show()
        }
        else {
            memberList = resultDefault.second
            listView.adapter = MembersListAdapter(this as Activity, memberList,id, trip)
        }

        //TODO: Crida amb el servei de RevPollution
        val qualityAir: String = viewModel.getAirQuality(latDest, lonDest)
        if (qualityAir == "Bad") qaImage.setImageResource(R.drawable.airbad)
        else if (qualityAir == "Mid") qaImage.setImageResource(R.drawable.airmid)
        else if (qualityAir == "Good") qaImage.setImageResource(R.drawable.airgood)
        else qaImage.setImageResource(R.drawable.ic_baseline_image_not_supported_24)


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

        originFull.text = originString
        destinationFull.text=destinationString

        val imageViewProfile : ImageView = findViewById(R.id.profile_image2)
        var imagePath : String = viewModel.getUsersProfilePhoto(username!!)
        Log.i("imagePath", imagePath)
        if (imagePath.equals("null") || imagePath.equals("")) {
            imageViewProfile.setImageResource(R.drawable.avatar)
        }
        else Picasso.get().load(imagePath).into(imageViewProfile)

        val ratingPair = viewModel.getRating(username)
        if (ratingPair.first != 200) {
            Toast.makeText(this, getString(R.string.ServerError), Toast.LENGTH_LONG).show()
        } else renderRating(ratingPair.second!!.ratingValue)
        val numValorations : TextView = this.findViewById(R.id.numberValorations)
        numValorations.text = "(${ratingPair.second!!.numberOfRatings})"


        val shareButton : ImageButton = this.findViewById(R.id.shareButton)
        shareButton.setOnClickListener {
            val  myIntent : Intent = Intent(Intent.ACTION_SEND)
            myIntent.type = "text/plain"
            var shareBody :String = ""
            if (SessionController.getUsername(this) == username ){
                shareBody = getString(R.string.shareOwnTrip, dateTmp,startTime.substring(0, startTime.length-3),originString,destinationString, (offeredSeats-occupiedSeats).toString() )
            }
            else shareBody = getString(R.string.shareTrip, dateTmp,startTime.substring(0, startTime.length-3),originString,destinationString, (offeredSeats-occupiedSeats).toString() )
            myIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
            startActivity(Intent.createChooser(myIntent, getString(R.string.share)))
        }

        val btnCancel : Button = this.findViewById(R.id.btn_cancelarTrajecte)

        val today = Calendar.getInstance().time
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        val cancelDay = formatter.parse(cancelDate)

        if ((SessionController.getUsername(this) != username) || (today >= cancelDay) || (state == "cancelled")) (btnCancel.parent as ViewManager).removeView(btnCancel)
        else {
            btnCancel.setOnClickListener {
                val confirmDialog = CancelTripDialog()

                val bundle = Bundle()
                bundle.putString("tripID", id.toString()) //passem l'identificador del trajecte
                confirmDialog.arguments = bundle

                confirmDialog.show(supportFragmentManager, "confirmDialog")
            }
        }

        val btnUnirse: Button = this.findViewById(R.id.unirseTrajecte)
        btnUnirse.setOnClickListener {
            val context = this
            val intent = Intent(context, ChatConversation::class.java).apply {
                val currentUsername : String = SessionController.getUsername(context)
                putExtra("userA", currentUsername)
                putExtra("userB", username)
            }
            context.startActivity(intent)
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