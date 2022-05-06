package elektrogo.front.ui.carPooling


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.github.sundeepk.compactcalendarview.CompactCalendarView
import com.github.sundeepk.compactcalendarview.CompactCalendarView.CompactCalendarViewListener
import elektrogo.front.R
import elektrogo.front.controller.session.SessionController
import elektrogo.front.model.CarPooling
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

class MyTripsFragment : Fragment() {

    companion object {
        fun newInstance() = MyTripsFragment()
    }
    private lateinit var filteredList: ArrayList<CarPooling>
    private lateinit var calendar : CompactCalendarView
    private lateinit var forwardMonth : ImageButton
    private lateinit var backwardMonth : ImageButton
    private lateinit var actualMonth : TextView
    private var month : Int = 0
    private var year : Int = 0


    private var viewModel: MyTripsViewModel = MyTripsViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.my_trips_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initialize()

        val listView: ListView = view.findViewById(R.id.filterListView)
        var date : LocalDate = LocalDate.now()
        month = date.monthValue
        year = date.year
        val defaultZoneId: ZoneId = ZoneId.systemDefault()
        calendar.setCurrentDate(Date.from(date.atStartOfDay(defaultZoneId).toInstant()))
        val username = SessionController.getUsername(context as Activity)
        setActualMonthText()
        calendar.shouldScrollMonth(false)
        forwardMonth.setOnClickListener {
            calendar.scrollRight()
            month +=1
            if (month == 13) {
                month = 1
                year += 1
            }
            setActualMonthText()
        }
        backwardMonth.setOnClickListener {
            calendar.scrollLeft()
            month -=1
            if (month == 0) {
                month = 12
                year -=1
            }
            setActualMonthText()
        }


        var result : Pair <Int, ArrayList<CarPooling>> = viewModel.askForTripsForUser(username)
        if (result.first != 200) {
            Toast.makeText(context, "Hi ha hagut un error, intenta-ho més tard", Toast.LENGTH_LONG).show()
        }
        else {
            filteredList = TripsOnDate(result.second, date)
            listView.adapter = ListAdapterTrips(context as Activity, filteredList)
        }
        /*
        calendar.setOnDateChangeListener { Calview, year, month, dayOfMonth ->
            if (result.first == 200) {
                date = LocalDate.of(year, Month.of(month), dayOfMonth)
                filteredList = TripsOnDate(result.second, date)
                listView.adapter = ListAdapterTrips(context as Activity, filteredList)
            }
        }*/

        listView.setOnItemClickListener { parent, view, position, id ->
            val i = Intent(context, TripDetails::class.java)
            i.putExtra("username", filteredList[position].username)
            i.putExtra("startDate", filteredList[position].startDate)
            i.putExtra("startTime", filteredList[position].startTime)
            i.putExtra("offeredSeats", filteredList[position].offeredSeats)
            i.putExtra("occupiedSeats", filteredList[position].occupiedSeats)
            i.putExtra("restrictions", filteredList[position].restrictions)
            i.putExtra("details", filteredList[position].details)
            i.putExtra("originString", filteredList[position].origin)
            i.putExtra("destinationString", filteredList[position].destination)
            i.putExtra("vehicleNumberPlate", filteredList[position].vehicleNumberPlate)
            startActivity(i)
        }

    }

    private fun setActualMonthText() {
        when (month) {
            1 -> actualMonth.setText(getString(R.string.Jan))
            2 -> actualMonth.setText(getString(R.string.Feb))
            3 -> actualMonth.setText(getString(R.string.Mar))
            4 -> actualMonth.setText(getString(R.string.Apr))
            5 -> actualMonth.setText(getString(R.string.May))
            6 -> actualMonth.setText(getString(R.string.Jun))
            7 -> actualMonth.setText(getString(R.string.Jul))
            8 -> actualMonth.setText(getString(R.string.Aug))
            9 -> actualMonth.setText(getString(R.string.Sept))
            10 -> actualMonth.setText(getString(R.string.Oct))
            11 -> actualMonth.setText(getString(R.string.Nov))
            12 -> actualMonth.setText(getString(R.string.Dec))
        }
        var s = actualMonth.text
        actualMonth.setText("$s $year")
    }

    private fun initialize() {
        calendar = requireActivity().findViewById(R.id.compactcalendar_view)
        forwardMonth = requireActivity().findViewById(R.id.forwardMonth)
        backwardMonth = requireActivity().findViewById(R.id.backwardMonth)
        actualMonth = requireActivity().findViewById(R.id.textMonth)
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