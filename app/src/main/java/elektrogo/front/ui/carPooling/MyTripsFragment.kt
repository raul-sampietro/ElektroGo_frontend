package elektrogo.front.ui.carPooling


import android.app.Activity
import android.content.Intent
import android.graphics.Color
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
import com.github.sundeepk.compactcalendarview.domain.Event
import com.google.gson.Gson
import elektrogo.front.R
import elektrogo.front.controller.session.SessionController
import elektrogo.front.model.CarPooling
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
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
       onViewCreatedMethod()
    }

    private fun onViewCreatedMethod(){
        initialize()

        val listView: ListView = requireView().findViewById(R.id.filterListView)
        val date : LocalDate = LocalDate.now()
        month = date.monthValue
        year = date.year
        val defaultZoneId: ZoneId = ZoneId.systemDefault()
        calendar.setUseThreeLetterAbbreviation(true)
        calendar.shouldDrawIndicatorsBelowSelectedDays(true)
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


        val result : Pair <Int, ArrayList<CarPooling>> = viewModel.askForTripsForUser(username)
        if (result.first != 200) {
            Toast.makeText(context, "Hi ha hagut un error, intenta-ho més tard", Toast.LENGTH_LONG).show()
        }
        else {
            addEvents(result.second)
            filteredList = TripsOnDate(result.second, date)
            listView.adapter = ListAdapterTrips(context as Activity, filteredList)
        }

        calendar.setListener(object : CompactCalendarViewListener {
            override fun onDayClick(dateClicked: Date) {
                val localDate = dateClicked.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                filteredList = TripsOnDate(result.second, localDate)
                listView.adapter = ListAdapterTrips(context as Activity, filteredList)

            }

            override fun onMonthScroll(firstDayOfNewMonth: Date) {

            }
        })

        listView.setOnItemClickListener { parent, Listview, position, id ->
            val i = Intent(context, TripDetails::class.java)
            val myjson : String = Gson().toJson(filteredList[position])
            i.putExtra("Trip", myjson)
            startActivity(i)
        }

    }

    private fun addEvents(filteredList: ArrayList<CarPooling>) {
        for (cP: CarPooling in filteredList) {
            val localDate : LocalDateTime = LocalDateTime.parse(cP.startDate + " " + cP.startTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            val zoneId = ZoneId.systemDefault()
            println(localDate.atZone(zoneId).toEpochSecond())
            val ev2 = Event(Color.RED, localDate.atZone(zoneId).toEpochSecond()*1000)
            calendar.addEvent(ev2)
            val list = calendar.getEvents(localDate.atZone(zoneId).toEpochSecond())
            println(list.size)
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
        val s = actualMonth.text
        actualMonth.setText("$s $year")
    }

    private fun initialize() {
        calendar = requireActivity().findViewById(R.id.compactcalendar_view)
        calendar.removeAllEvents()
        forwardMonth = requireActivity().findViewById(R.id.forwardMonth)
        backwardMonth = requireActivity().findViewById(R.id.backwardMonth)
        actualMonth = requireActivity().findViewById(R.id.textMonth)
    }

    private fun TripsOnDate(trips: ArrayList<CarPooling>, date: LocalDate?): ArrayList<CarPooling> {
        val tripsOnDate : ArrayList<CarPooling> = ArrayList<CarPooling>()
        for (cP: CarPooling in trips) {
            val dateString = date.toString()
            if (cP.startDate.equals(date.toString())) tripsOnDate.add(cP)
            else println("$dateString is not equal to ${cP.startDate} ")
        }
        return tripsOnDate
    }

    override fun onResume() {
        super.onResume()
        onViewCreatedMethod()
    }


}