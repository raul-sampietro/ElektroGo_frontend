package elektrogo.front.ui.carPooling

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import elektrogo.front.R
import elektrogo.front.model.Vehicle
import elektrogo.front.ui.vehicleList.VehicleListAdapter
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter


class filtrarTrajectesFragment : Fragment() {

    companion object {
        fun newInstance() = filtrarTrajectesFragment()
    }

    private lateinit var placesClient: PlacesClient
    private lateinit var dateButton: Button
    private lateinit var filtrarButton : Button
    private lateinit var timeFromButton: Button
    private lateinit var timeToButton: Button
    private lateinit var autocompleteSupportFragment: AutocompleteSupportFragment
    private lateinit var autocompleteSupportFragment2: AutocompleteSupportFragment
    private lateinit var originText : TextView
    private lateinit var destinationText : TextView
    private  var latLngOrigin : LatLng?=null
    private  var latLngDestination: LatLng?=null
    private  var fromTimeSelected: String?=null
    private  var toTimeSelected: String?=null
    private  var dateSelected: String?=null
  //  private lateinit var filteredList: ArrayList<CarPooling>


    private lateinit var viewModel: FiltrarTrajectesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.filtrar_trajectes_fragment, container, false)



        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        dateButton = requireActivity().findViewById(R.id.dataButtonFiltrar)
        filtrarButton = requireActivity().findViewById(R.id.Filtrar)
        timeFromButton=requireActivity().findViewById(R.id.timeFromButtonFiltrar)
        timeToButton=requireActivity().findViewById(R.id.timeToButtonFiltrar)
        originText = requireActivity().findViewById(R.id.errorViewOriginFiltrar)
        destinationText = requireActivity().findViewById(R.id.errorViewDestinationFiltrar)

        if (!Places.isInitialized()) Places.initialize(this.requireContext(),resources.getString(R.string.google_maps_key))
        placesClient= Places.createClient(this.requireContext())
        autocompleteSupportFragment = childFragmentManager.findFragmentById(R.id.autocomplete_fragmentFiltrar) as AutocompleteSupportFragment
        autocompleteSupportFragment.setHint("Origen")
        autocompleteSupportFragment2 = childFragmentManager.findFragmentById(R.id.autocomplete_fragmentFiltrar2) as AutocompleteSupportFragment
        autocompleteSupportFragment2.setHint("Desti")
        getAutocompleteLocation()

        val crossCallbackOrigin = requireActivity().findViewById<View>(R.id.viewCrossCallbackOriginFiltrar)
        crossCallbackOrigin.setOnClickListener {
            latLngOrigin=null
            autocompleteSupportFragment.setText("")
        }
        val crossCallbackDestination = requireActivity().findViewById<View>(R.id.viewCrossCallbackDestinationFiltrar)
        crossCallbackDestination.setOnClickListener {
            latLngDestination=null
            autocompleteSupportFragment2.setText("")
        }
        val listView: ListView = view.findViewById(R.id.filterListView)

        filtrarButton.setOnClickListener {
            if (validate()) {
              //  val Pooling : CarPooling = CarPooling(1, "23/06/2022", "9:00", 6, 4, "", "", 10.56, 1.54, "Sevilla", 3.56,2.05, "Galicia", "1234ABC" )
              //  val Pooling2 : CarPooling = CarPooling(2, "23/04/2022", "17:30", 5, 2, "", "", 10.56, 1.54, "Mataro", 3.56,2.05, "Canet de Mar", "1234ABD" )

                // filteredList = viewModel.askForTrips(latLngOrigin, latLngDestination, dateSelected, fromTimeSelected, toTimeSelected)
            //    filteredList = ArrayList<CarPooling>()
              //  filteredList.add(Pooling)
              //  filteredList.add(Pooling2)
               // listView.adapter = ListAdapter(context as Activity, filteredList)
            }
            else Toast.makeText(context, getString(R.string.errorFieldsFiltrar),Toast.LENGTH_SHORT).show()

        }

        var day: Int = 28
        var month: Int = 10
        var year: Int = 1920
        var hour: Int = 11
        var minute: Int = 11

        dateButton.setOnClickListener {
            var calendar = Calendar.getInstance()
            day = calendar.get(Calendar.DAY_OF_MONTH)
            month = calendar.get(Calendar.MONTH)
            year = calendar.get(Calendar.YEAR)
            val dpd = DatePickerDialog(requireActivity(), DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                // Display Selected date in textbox

                var correctMonth = (monthOfYear+1)
                if(dayOfMonth < 10 && correctMonth < 10){
                    dateButton.text = "0$dayOfMonth/0$correctMonth/$year"
                    dateSelected = "0$dayOfMonth/0$correctMonth/$year"
                }
                else if (dayOfMonth < 10 && correctMonth >= 10){
                    dateButton.text = "0$dayOfMonth/$correctMonth/$year"
                    dateSelected = "0$dayOfMonth/$correctMonth/$year"
                }
                else if (dayOfMonth >= 10 && correctMonth < 10){
                    dateButton.text = "$dayOfMonth/0$correctMonth/$year"
                    dateSelected = "$dayOfMonth/0$correctMonth/$year"
                }
                else {
                    dateButton.text = "$dayOfMonth/$correctMonth/$year"
                    dateSelected = "$dayOfMonth/$correctMonth/$year"
                }


            }, year, month, day)

            dpd.show()
        }

       timeFromButton.setOnClickListener {
            var calendar = Calendar.getInstance()
            hour = calendar.get(Calendar.HOUR_OF_DAY)
            minute = calendar.get(Calendar.MINUTE)

            val tpd = TimePickerDialog(requireActivity(), TimePickerDialog.OnTimeSetListener { view, hour, minute ->

                // Display Selected date in textbox
                if (minute<10){
                    timeFromButton.text = "$hour:0$minute"
                    fromTimeSelected = "$hour:0$minute"

                }
                else {
                    timeFromButton.text = "$hour:$minute"
                    fromTimeSelected = "$hour:$minute"
                }

            }, minute, hour, true)

            tpd.show()
        }

        timeToButton.setOnClickListener {
            var calendar = Calendar.getInstance()
            hour = calendar.get(Calendar.HOUR_OF_DAY)
            minute = calendar.get(Calendar.MINUTE)

            val tpd = TimePickerDialog(requireActivity(), TimePickerDialog.OnTimeSetListener { view, hour, minute ->

                if (minute<10){
                    timeToButton.text = "$hour:0$minute"
                    toTimeSelected = "$hour:0$minute"

                }
                else {
                    timeToButton.text = "$hour:$minute"
                    toTimeSelected = "$hour:$minute"
                }
            }, minute, hour, true)

            tpd.show()
        }
    }

    private fun getAutocompleteLocation () {
        autocompleteSupportFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG))
        autocompleteSupportFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                originText.error = null
                latLngOrigin = place.latLng
                if (latLngOrigin == null) Toast.makeText(context, resources.getString(R.string.errorOnLocation),
                    Toast.LENGTH_SHORT).show()
            }
            override fun onError(status: Status) {
                Log.i("PlacesApiError", "An error occurred: $status")
            }
        })
        autocompleteSupportFragment2.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG))
        autocompleteSupportFragment2.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                destinationText.error = null
                latLngDestination = place.latLng
                if (latLngDestination == null)  Toast.makeText(context, resources.getString(R.string.errorOnLocation),
                    Toast.LENGTH_LONG).show()
            }
            override fun onError(status: Status) {
                Log.i("PlacesApiError", "An error occurred: $status")
            }
        })

    }

    private fun validate() :Boolean {
        var valid : Boolean = true

        if (dateButton.text != "Data" && LocalDate.parse(dateButton.text.toString(), DateTimeFormatter.ofPattern("dd/MM/yyyy")) < LocalDate.now()) {
            valid = false
            Toast.makeText(context, "La data seleccionada és incorrecta",Toast.LENGTH_LONG).show()
        }

        if (timeFromButton.text !="Des de" && timeToButton.text != "Fins a" &&(LocalTime.parse(timeFromButton.text.toString(), DateTimeFormatter.ofPattern("HH:mm"))) >= (LocalTime.parse(timeToButton.text.toString(), DateTimeFormatter.ofPattern("HH:mm")))){
            valid = false
            Toast.makeText(context, "El rang d'hores no és correcte. La primera hora donada ha de ser anterior a la segona.",Toast.LENGTH_LONG).show()
        }
        else if(timeFromButton.text!="Des de" && dateButton.text!="Data" && LocalTime.parse(timeFromButton.text.toString(), DateTimeFormatter.ofPattern("HH:mm")) < LocalTime.now() &&  LocalDate.parse(dateButton.text.toString(), DateTimeFormatter.ofPattern("dd/MM/yyyy")) == LocalDate.now()){
            valid = false
            Toast.makeText(context, "L'hora d'inici i la data seleccionades son incorrectes. Indiqui una data i hora posteriors a les actuals",Toast.LENGTH_LONG).show()
        }

        if (latLngOrigin==null) {
            valid = false
            originText.error = resources.getString(R.string.fieldRequired)
        }
        if (latLngDestination==null){
            valid=false
            destinationText.error=resources.getString(R.string.fieldRequired)
        }
        return valid
    }


}