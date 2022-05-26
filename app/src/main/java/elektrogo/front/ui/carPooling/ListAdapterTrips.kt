/**
 * @file ListAdapter.kt
 * @author Marina Alapont
 * @date 23/04/2022
 * @brief Implementacio d'un adaptador per tal de mostrar la llista amb els resultats obtinguts de cercar trajectes.
 */
package elektrogo.front.ui.carPooling

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.squareup.picasso.Picasso
import elektrogo.front.R
import elektrogo.front.model.CarPooling
import java.text.SimpleDateFormat

/**
 * @brief La clase ListAdapter s'encarrega de mostrar, per cada trajecte de filteredList, l'informacio resultant en una llista.
 */
class ListAdapterTrips (private val context : Activity, private val filteredList : ArrayList<CarPooling>) : ArrayAdapter<CarPooling>(context, R.layout.filter_list_item, filteredList){

    /**
     * @brief Instancia de la classe filterTripsViewModel.
     */
    private var viewModel: FilterTripsViewModel  = FilterTripsViewModel()

    /**
     * @brief Metode que mostra en pantalla, en una llista, la diferent informacio que te un trajecte, per cada trajecta obtingut de la cerca.
     * @pre
     * @post Es mostra un llistat amb els trajectes dins de filteredList
     */
    @SuppressLint("SimpleDateFormat")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.filter_list_item, null)

        Log.i("add", "Estoy en el adapter")

        // TODO imageView
        val occupiedseats : TextView = view.findViewById(R.id.occupiedseats)
        val origin : TextView = view.findViewById(R.id.citynameOrigin)
        val destination : TextView = view.findViewById(R.id.citynameDest)
        val startTime : TextView = view.findViewById(R.id.time)
        val date :TextView = view.findViewById(R.id.dateFiltered)
        val user : TextView = view.findViewById(R.id.username)

        val f = filteredList[position]
        val ratingPair = viewModel.getRating(f.username)
        if (ratingPair.first != 200) {
            Toast.makeText(context, context.getString(R.string.ServerError), Toast.LENGTH_LONG).show()
        } else renderRating(ratingPair.second!!.ratingValue, view)
        val numValorations : TextView = view.findViewById(R.id.numberValorations)
        numValorations.text = "(${ratingPair.second!!.numberOfRatings})"

        var occupied : String = (f.offeredSeats - f.occupiedSeats).toString()
        occupied += "/"
        occupied += f.offeredSeats.toString()
        occupiedseats.text = occupied
        startTime.text = f.startTime.substring(0, f.startTime.length-3)

        val dateTmp = f.startDate
        val input = SimpleDateFormat("yyyy-MM-dd")
        val output = SimpleDateFormat("dd/MM/yyyy")
        val oneWayTripDate = input.parse(dateTmp) // parse input
        date.text = output.format(oneWayTripDate) // format output
        user.text = f.username

        var originBrief : String
        if (f.origin.length > 32){
            originBrief = f.origin.substring(0, 32)
            originBrief += "..."
            origin.text = originBrief
        }
        else origin.text = f.origin

        var destinationBrief : String
        if (f.destination.length > 32){
            destinationBrief = f.destination.substring(0, 32)
            destinationBrief += "..."
            destination.text = destinationBrief
        }
        else destination.text = f.destination

        val imageViewProfile : ImageView = view.findViewById(R.id.profile_image)
        val imagePath = viewModel.getUsersProfilePhoto(f.username)
        if (!imagePath.equals("null")  and !imagePath.equals("") ) Picasso.get().load(imagePath).into(imageViewProfile)
        else imageViewProfile.setImageResource(R.drawable.avatar)
        return view
    }
    /**
     * @brief Metode que s'encarrega de renderitzar les estrelles que te l'usuari segons la valoracio mitjana que rep.
     * @pre
     * @post Es mostren les estrelles segons la valoracio que l'usuari te.
     */
    private fun renderRating(ratingPassed: Double, view: View?) {
        Log.i("add", "Estoy en el render rating")

        val star1 : ImageView = view!!.findViewById(R.id.estrella1)
     val star2 : ImageView = view.findViewById(R.id.estrella2)
     val star3 : ImageView = view.findViewById(R.id.estrella3)
     val star4 : ImageView = view.findViewById(R.id.estrella4)
     val star5 : ImageView = view.findViewById(R.id.estrella5)

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