package elektrogo.front.ui.CarPooling


import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.CalendarView.OnDateChangeListener
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import elektrogo.front.R
import elektrogo.front.controller.session.SessionController
import elektrogo.front.model.CarPooling
import elektrogo.front.ui.carPooling.ListAdapter
import java.time.LocalDate
import java.time.Month
import java.util.*
import kotlin.collections.ArrayList

class MyTripsFragment : Fragment() {

    companion object {
        fun newInstance() = MyTripsFragment()
    }
    private lateinit var filteredList: ArrayList<CarPooling>
    private lateinit var calendar : CalendarView

    private var viewModel: MyTripsViewModel = MyTripsViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.my_trips_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        calendar = requireActivity().findViewById(R.id.calendarView)
        val listView: ListView = view.findViewById(R.id.filterListView)
        var date : LocalDate = LocalDate.now()
        val calendarAux = Calendar.getInstance()
        calendarAux.set (date.year, date.month.value, date.dayOfMonth )
        calendar.setDate(calendarAux.timeInMillis)
        val username = SessionController.getUsername(context as Activity)
        var result : Pair <Int, ArrayList<CarPooling>> = viewModel.askForTripsForUser(username)
        if (result.first != 200) {
            Toast.makeText(context, "Hi ha hagut un error, intenta-ho més tard", Toast.LENGTH_LONG).show()
        }
        else {
            //filteredList = result.second
            //listView.adapter = ListAdapter(context as Activity, filteredList)
        }
        calendar.setOnDateChangeListener { Calview, year, month, dayOfMonth ->
            if (result.first == 200) {
                date = LocalDate.of(year, Month.of(month), dayOfMonth)
                filteredList = TripsOnDate(result.second, date)
                listView.adapter = ListAdapter(context as Activity, filteredList)
            }
        }

    }

    private fun TripsOnDate(trips: ArrayList<CarPooling>, date: LocalDate?): ArrayList<CarPooling> {
        var tripsOnDate : ArrayList<CarPooling> = ArrayList<CarPooling>()
        for (cP: CarPooling in trips) {
            val dateString = date.toString()
            if (cP.startDate.equals(date.toString())) tripsOnDate.add(cP)
            else println("$dateString is not equal to ${cP.startDate} ")
        }
        return tripsOnDate
    }


}