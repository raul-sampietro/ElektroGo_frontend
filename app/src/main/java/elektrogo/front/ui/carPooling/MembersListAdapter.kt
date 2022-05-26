/**
 * @file MemberListAdapter.kt
 * @author Marina Alapont
 * @date 26/05/2022
 * @brief Implementacio d'un adaptador per tal de mostrar la llista amb els resultats obtinguts de cercar els membres d'un trajecte.
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
import elektrogo.front.model.User
import java.text.SimpleDateFormat

class MembersListAdapter (private val context : Activity, private val memberList : ArrayList<User>) : ArrayAdapter<User>(context, R.layout.member_list_item, memberList){

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
        val view = inflater.inflate(R.layout.member_list_item, null)

        val f = memberList[position]

        val usernameText : TextView= view.findViewById(R.id.usernameItem)
        usernameText.text = f.username
        val ratingPair = viewModel.getRating(f.username)
        if (ratingPair.first != 200) {
            Toast.makeText(context, context.getString(R.string.ServerError), Toast.LENGTH_LONG).show()
        } else renderRating(ratingPair.second!!.ratingValue, view)

        val numValorations : TextView = view.findViewById(R.id.numberValorationsItem)
        numValorations.text = "(${ratingPair.second!!.numberOfRatings})"

        val imageViewProfile : ImageView = view.findViewById(R.id.profile_imageItem)
        val imagePath = f.imageUrl
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

        val star1 : ImageView = view!!.findViewById(R.id.estrella1Item)
        val star2 : ImageView = view.findViewById(R.id.estrella2Item)
        val star3 : ImageView = view.findViewById(R.id.estrella3Item)
        val star4 : ImageView = view.findViewById(R.id.estrella4Item)
        val star5 : ImageView = view.findViewById(R.id.estrella5Item)

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