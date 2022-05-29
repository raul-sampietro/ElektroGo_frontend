/**
 * @file tripDetails.kt
 * @author Marina Alapont
 * @date 28/04/2022
 * @brief Implementacio d'una classe per tal de veure la informacio detallada d'un trajecte.
 */
package elektrogo.front.ui.carPooling

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
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
import elektrogo.front.ui.profile.ProfileActivity
import java.sql.Time
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

        //Obtencio dels membres que participen en el trajecte
        val listView: ListView = this.findViewById(R.id.listMembers)
        var memberList : ArrayList<User> = arrayListOf()
        var resultDefault : Pair <Int, ArrayList<User>> = viewModel.askForMembersOfATrip(id!!)
        if (resultDefault.first != 200) {
            Toast.makeText(this, getString(R.string.membersError), Toast.LENGTH_LONG).show()
        }
        else {
            memberList = resultDefault.second
            val nMembers = memberList.size
            if (memberList.size > 0 ) findViewById<TextView>(R.id.noMembers).visibility = View.GONE
            val lp: LinearLayout.LayoutParams = listView.layoutParams as LinearLayout.LayoutParams
            lp.height = nMembers*82*3
            listView.layoutParams = lp
            listView.adapter = MembersListAdapter(this as Activity, memberList,id, trip)
        }

        //TODO: Crida amb el servei de RevPollution
        val qualityAir: String = viewModel.getAirQuality(latDest, lonDest)
        if (qualityAir == "Bad") qaImage.setImageResource(R.drawable.airbad)
        else if (qualityAir == "Mid") qaImage.setImageResource(R.drawable.airmid)
        else if (qualityAir == "Good") qaImage.setImageResource(R.drawable.airgood)
        else qaImage.setImageResource(R.drawable.ic_baseline_wifi_off_24)


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
            Toast.makeText(this, getString(R.string.errorRatings), Toast.LENGTH_LONG).show()
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

        var today = Calendar.getInstance().time
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        today = formatter.parse(formatter.format((today)))
        val cancelDay = formatter.parse(cancelDate)



        var userParticipates = false;
        for (u: User in memberList) {
            if (u.username == SessionController.getUsername(this)) {
                userParticipates = true;
            }
        }

        val startDay = formatter.parse(startDate)

        val formatterTime = SimpleDateFormat("HH:mm:ss")
        val dateStartTime = formatterTime.parse(startTime)
        val startTimeT = Time(dateStartTime.time)
        var actualTime = Time(System.currentTimeMillis())
        actualTime = Time(formatterTime.parse(formatterTime.format(actualTime)).time)
        if (userParticipates && (SessionController.getUsername(this) != username) && (today < startDay || (today == startDay && actualTime < startTimeT)) && state !="cancelled" && state != "finished") {
            btnCancel.setText(R.string.abandonTrip)
            btnCancel.setOnClickListener {
                val status = viewModel.abandonTrip(id, SessionController.getUsername(this));
                if (status != 200) {
                    Toast.makeText(
                        this,
                        R.string.errorAbandonTrip,
                        Toast.LENGTH_LONG
                    ).show()
                }
                else { //Trip abandoned successfully
                    var memberListAux : ArrayList<User> = arrayListOf()

                    for (u: User in memberList) {
                        if (u.username != SessionController.getUsername(this)) {
                            memberListAux.add(u);
                        }
                    }
                    listView.adapter = MembersListAdapter(this as Activity, memberListAux,id, trip)
                    (btnCancel.parent as ViewManager).removeView(btnCancel)
                }
            }

        }
        else if ((SessionController.getUsername(this) != username) || (today >= cancelDay) || (state == "cancelled")) (btnCancel.parent as ViewManager).removeView(btnCancel)
        else {
            btnCancel.setOnClickListener {
                val confirmDialog = CancelTripDialog()

                val bundle = Bundle()
                bundle.putString("tripID", id.toString()) //passem l'identificador del trajecte
                confirmDialog.arguments = bundle

                confirmDialog.show(supportFragmentManager, "confirmDialog")
            }
        }

        val btnFinish: Button = findViewById(R.id.btn_finalitzarTrajecte)

        if ((SessionController.getUsername(this) != username) || (today < startDay || (today == startDay && actualTime < startTimeT)) || (state == "finished")) {
            (btnFinish.parent as ViewManager).removeView(btnFinish)
        }
        else {
            btnFinish.setOnClickListener {
                val alertDialog: AlertDialog? = this.let {
                    val builder = AlertDialog.Builder(it)
                    // TODO hardcoded strings
                    builder.setMessage("Vols finalitzar aquest trajecte?")
                    builder.apply {
                        setPositiveButton("SI",
                            DialogInterface.OnClickListener { dialog, id ->
                                val status = viewModel.finishTrip(trip.id!!.toInt())
                                if (status == 200)
                                    Toast.makeText(context, "Trajecte finalitzat", Toast.LENGTH_LONG).show()
                                else
                                    Toast.makeText(context, "No s'ha pogut finalitzar el trajecte", Toast.LENGTH_LONG).show()
                                //val intent = Intent(context, VehicleListActivity::class.java)
                                //startActivity(intent)
                                //finish()
                            })
                        setNegativeButton("NO",
                            DialogInterface.OnClickListener { dialog, id ->
                                Toast.makeText(context, "No s'ha finalitzat el trajecte", Toast.LENGTH_LONG).show()
                            })
                    }
                    builder.create()
                }
                alertDialog?.show()
            }
        }


        val btnUnirse: Button = this.findViewById(R.id.unirseTrajecte)
        val currentUsername : String = SessionController.getUsername(this)
        // no es mostra el boto per als teus propis trajectes
        if (currentUsername == username) btnUnirse.visibility = View.GONE
        // si l'usuari ja es membre del trajecte no es mosstra el boto
        for (user: User in memberList) {
            if (user.username == currentUsername) btnUnirse.visibility = View.GONE
        }

        btnUnirse.setOnClickListener {
            val context = this
            val intent = Intent(context, ChatConversation::class.java).apply {
                putExtra("userA", currentUsername)
                putExtra("userB", username)
            }
            context.startActivity(intent)
        }

        val infoUserHost : LinearLayout = this.findViewById(R.id.infoUserHost)
        infoUserHost.setOnClickListener{
            val i  = Intent(this, ProfileActivity::class.java)
            i.putExtra("username", username)
            startActivity(i)
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