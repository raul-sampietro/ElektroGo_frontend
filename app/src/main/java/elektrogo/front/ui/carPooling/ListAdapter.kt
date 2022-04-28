package elektrogo.front.ui.carPooling

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import elektrogo.front.R

class ListAdapter (private val context : Activity, private val filteredList : ArrayList<CarPooling>) : ArrayAdapter<CarPooling>(context, R.layout.filter_list_item, filteredList){

    private lateinit var viewModel: FiltrarTrajectesViewModel

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
        val rating = viewModel.getRating(f.username)

        renderRating (rating, view)
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

    private fun renderRating(rating: Double, view: View?) {
     val star1 : ImageView = view!!.findViewById(R.id.estrella1)
     val star2 : ImageView = view.findViewById(R.id.estrella2)
     val star3 : ImageView = view.findViewById(R.id.estrella3)
     val star4 : ImageView = view.findViewById(R.id.estrella4)
     val star5 : ImageView = view.findViewById(R.id.estrella5)

     star1.setImageResource(R.drawable.ic_starplena)
     star2.setImageResource(R.drawable.ic_starplena)
     star3.setImageResource(R.drawable.ic_starplena)
     star4.setImageResource(R.drawable.ic_starplena)
     star5.setImageResource(R.drawable.ic_starplena)






    }
}