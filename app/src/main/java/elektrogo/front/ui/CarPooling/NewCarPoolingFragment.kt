package elektrogo.front.ui.CarPooling

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import elektrogo.front.R
import java.sql.Time
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

class NewCarPoolingFragment() : Fragment() {

    private lateinit var viewModel: NewCarPoolingFragmentViewModel

    private lateinit var autocompleteSupportFragment : AutocompleteSupportFragment

    private lateinit var autocompleteSupportFragment2 : AutocompleteSupportFragment

    private lateinit var buttonPublish: Button

    private lateinit var buttonDate: Button

    private lateinit var buttonHour: Button

    private lateinit var selectedDate: TextView

    private lateinit var selectedHour: TextView

    private lateinit var dropVehicles: Spinner

    private lateinit var dropSeats: Spinner


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_new_car_pooling, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //Inicialitzacio de les variables relacionades amb la interficie
        viewModel = NewCarPoolingFragmentViewModel()
        buttonPublish = requireActivity().findViewById(R.id.PublishButton)
        buttonDate = requireActivity().findViewById(R.id.DateButton)
        buttonHour = requireActivity().findViewById(R.id.HourButton)
        selectedDate = requireActivity().findViewById(R.id.DateSelected)
        selectedHour = requireActivity().findViewById(R.id.HourSelected)
        dropVehicles = requireActivity().findViewById(R.id.VehicleSelector) //Obtinc l'spinner dels vehicles
        dropSeats = requireActivity().findViewById(R.id.SeatsOfferedSelector) //Obtinc l'spinner dels seients
        //Inicialitzem les variables per tal d'evitar problemes amb les dates i hores
        var day: Int = 28
        var month: Int = 10
        var year: Int = 1920
        var hour: Int = 11
        var minute: Int = 11

        buttonDate.setOnClickListener {
            var calendar = Calendar.getInstance()
            day = calendar.get(Calendar.DAY_OF_MONTH)
            month = calendar.get(Calendar.MONTH)
            year = calendar.get(Calendar.YEAR)

            val date : String = "$year-$month-$day"
            val dpd = DatePickerDialog(requireActivity(), DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                // Display Selected date in textbox
                selectedDate.setText("$dayOfMonth/$monthOfYear/$year")

            }, year, month, day)

            dpd.show()
        }

        buttonHour.setOnClickListener {
            var calendar = Calendar.getInstance()
            hour = calendar.get(Calendar.HOUR_OF_DAY)
            minute = calendar.get(Calendar.MINUTE)

            val tpd = TimePickerDialog(requireActivity(), TimePickerDialog.OnTimeSetListener { view, hour, minute ->

                // Display Selected date in textbox
                selectedHour.setText("$hour:$minute")

            }, minute, hour, true)

            tpd.show()
        }

        var dateSelected : LocalDate? = LocalDate.parse("$year-$month-$day")
        var hourSelected : LocalTime? = LocalTime.parse("$hour:$minute")

        //Obtenim les dades dels vehicles del usuari logejat
        //TODO NO FER-HO HARDCODED!
        val vehicles = viewModel.getVehicleList("Test"); //Obtenim els vehicles de l'usuari

        //Ens encarreguem dels select menu
        //Menu de les matricules del vehicle



        var matricules : ArrayList<String> = arrayListOf()
        for (i in vehicles){
            matricules.add(i.numberPlate)
        }

        val VehiclesAdapter = ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_item, matricules)  //Defineixo l'estil del spinner
        VehiclesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)   //Defineixo l'estil del dropdown
        dropVehicles.setAdapter(VehiclesAdapter)  //modifico l'adapter de l'spinner

        var seientsDisponiblesMax: Int = 0
        for (i in vehicles) {
            if (i.numberPlate.equals(dropVehicles.selectedItem.toString())) seientsDisponiblesMax =
                i.seats - 1
        }
        val selectSeients = (1..seientsDisponiblesMax).toList().asReversed().toTypedArray() //Passo el rang d'anys a una array
        val SeatsAdapter = ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_item, selectSeients)  //Defineixo l'estil del spinner
        SeatsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)   //Defineixo l'estil del dropdown
        dropSeats.setAdapter(SeatsAdapter)  //modifico l'adapter de l'spinner

        //Cada vegada que es canvia el vehicle s'ha de canviar els seients disponibles!
        dropVehicles?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var seientsDisponiblesMax: Int = 0
                for (i in vehicles) {
                    if (i.numberPlate.equals(dropVehicles.selectedItem.toString())) seientsDisponiblesMax =
                        i.seats - 1
                }
                val selectSeients = (1..seientsDisponiblesMax).toList().asReversed().toTypedArray() //Passo el rang d'anys a una array
                val SeatsAdapter = ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_item, selectSeients)  //Defineixo l'estil del spinner
                SeatsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)   //Defineixo l'estil del dropdown
                dropSeats.setAdapter(SeatsAdapter)  //modifico l'adapter de l'spinner
            }
        }

    }

}
