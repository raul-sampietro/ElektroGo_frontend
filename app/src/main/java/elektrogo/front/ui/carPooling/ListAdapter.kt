/**
 * @file ListAdapter.kt
 * @author Marina Alapont
 * @date 23/04/2022
 * @brief Implementacio d'un adaptador per tal de mostrar la llista amb els resultats obtinguts de cercar trajectes.
 */
package elektrogo.front.ui.carPooling

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import elektrogo.front.R
import elektrogo.front.model.CarPooling


/**
 * @brief La clase ListAdapter s'encarrega de mostrar, per cada trajecte de filteredList, l'informacio resultant en una llista.
 */
class ListAdapter (private val context : Activity, private val filteredList : ArrayList<CarPooling>) : ArrayAdapter<CarPooling>(context, R.layout.filter_list_item, filteredList){

    /**
     * @brief Instancia de la classe FiltrarTrajectesViewModel.
     */
    private lateinit var viewModel: filterTripsViewModel

    /**
     * @brief Metode que mostra en pantalla, en una llista, la diferent informacio que te un trajecte, per cada trajecta obtingut de la cerca.
     * @pre
     * @post Es mostra un llistat amb els trajectes dins de filteredList
     */
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.filter_list_item, null)

        // TODO imageView
        val occupiedseats : TextView = view.findViewById(R.id.occupiedseats)
        val origin : TextView = view.findViewById(R.id.citynameOrigin)
        val destination : TextView = view.findViewById(R.id.citynameDest)
        val startTime : TextView = view.findViewById(R.id.time)
        val date :TextView = view.findViewById(R.id.dateFiltered)
        val user : TextView = view.findViewById(R.id.username)
        val f = filteredList[position]
    //    val rating = viewModel.getRating(f.username)

        var occupied : String = (f.offeredSeats - f.occupiedSeats).toString()
        occupied += "/"
        occupied += f.offeredSeats.toString()
        occupiedseats.text = occupied
        origin.text = f.originString
        destination.text = f.destinationString
        startTime.text = f.startTime
        date.text = f.startDate
        user.text = f.username

        return view
    }
    /**
     * @brief Metode que s'encarrega de renderitzar les estrelles que te l'usuari segons la valoracio mitjana que rep.
     * @pre
     * @post Es mostren les estrelles segons la valoracio que l'usuari te.
     */
    private fun renderRating(rating: Double, view: View?) {
     val star1 : ImageView = view!!.findViewById(R.id.estrella1)
     val star2 : ImageView = view.findViewById(R.id.estrella2)
     val star3 : ImageView = view.findViewById(R.id.estrella3)
     val star4 : ImageView = view.findViewById(R.id.estrella4)
     val star5 : ImageView = view.findViewById(R.id.estrella5)

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