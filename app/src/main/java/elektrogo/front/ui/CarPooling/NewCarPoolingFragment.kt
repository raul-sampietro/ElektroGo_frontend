package elektrogo.front.ui.CarPooling

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.icu.util.Calendar
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import elektrogo.front.R
import java.time.LocalDate
import java.time.LocalTime
import java.util.*


class NewCarPoolingFragment() : Fragment() {

    private lateinit var viewModel: NewCarPoolingFragmentViewModel

    private lateinit var placesClient: PlacesClient

    private var latLngOrigin : LatLng?= null

    private var latLngDestination : LatLng?= null

    private lateinit var autocompleteSupportFragment : AutocompleteSupportFragment

    private lateinit var autocompleteSupportFragment2 : AutocompleteSupportFragment

    private lateinit var originText : TextView

    private lateinit var destinationText : TextView

    private lateinit var buttonPublish: Button

    private lateinit var buttonDate: Button

    private lateinit var buttonHour: Button

    private lateinit var selectedDate: TextView

    private lateinit var selectedHour: TextView

    private lateinit var dropVehicles: Spinner

    private lateinit var vehicleLabel : TextView

    private lateinit var dropSeats: Spinner

    private lateinit var SeatsLabel : TextView


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
        vehicleLabel = requireActivity().findViewById(R.id.VehicleLabel)
        SeatsLabel = requireActivity().findViewById(R.id.SeatsOffered)
        dropSeats = requireActivity().findViewById(R.id.SeatsOfferedSelector) //Obtinc l'spinner dels seients
        originText = requireActivity().findViewById(R.id.errorViewOrigin)
        destinationText = requireActivity().findViewById(R.id.errorViewDestination)
        //Inicialitzem les variables per tal d'evitar problemes amb les dates i hores
        var day: Int = 28
        var month: Int = 10
        var year: Int = 1920
        var hour: Int = 11
        var minute: Int = 11

        //Boto per la data
        buttonDate.setOnClickListener {
            var calendar = Calendar.getInstance()
            day = calendar.get(Calendar.DAY_OF_MONTH)
            month = calendar.get(Calendar.MONTH)
            year = calendar.get(Calendar.YEAR)

            val date : String = "$year-$month-$day"
            val dpd = DatePickerDialog(requireActivity(), DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                // Display Selected date in textbox
                selectedDate.setText("$dayOfMonth/$monthOfYear/$year")
                selectedDate.setError(null)
            }, year, month, day)

            dpd.show()
        }

        //Boto per la hora
        buttonHour.setOnClickListener {
            var calendar = Calendar.getInstance()
            hour = calendar.get(Calendar.HOUR_OF_DAY)
            minute = calendar.get(Calendar.MINUTE)

            val tpd = TimePickerDialog(requireActivity(), TimePickerDialog.OnTimeSetListener { view, hour, minute ->

                // Display Selected date in textbox
                selectedHour.setText("$hour:$minute")
                selectedHour.setError(null)
            }, minute, hour, true)

            tpd.show()
        }
        //Passem la data i hora a variables
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

        if (!Places.isInitialized()) Places.initialize(this.requireContext(),resources.getString(R.string.google_maps_key))
        placesClient= Places.createClient(this.requireContext())


        buttonPublish.setOnClickListener {
            if (!isValid()){
                Toast.makeText(context, resources.getString(R.string.errorOnFields),Toast.LENGTH_SHORT).show()
            }
            else {
                //Crida amb backend per crear el carpooling nou.

            }
        }
        val crossCallbackOrigin = requireActivity().findViewById<View>(R.id.viewCrossCallbackOrigin)
        crossCallbackOrigin.setOnClickListener {
            latLngOrigin=null
            autocompleteSupportFragment.setText("")
        }
        val crossCallbackDestination = requireActivity().findViewById<View>(R.id.viewCrossCallbackDestination)
        crossCallbackDestination.setOnClickListener {
            latLngDestination=null
            autocompleteSupportFragment2.setText("")
        }
        getAutocompleteLocation()
    }

    private fun getAutocompleteLocation () {
        autocompleteSupportFragment = childFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment
        autocompleteSupportFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG))
        autocompleteSupportFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                originText.error = null
                latLngOrigin = place.latLng
                if (latLngOrigin == null) Toast.makeText(context, resources.getString(R.string.errorOnLocation),Toast.LENGTH_SHORT).show()
            }
            override fun onError(status: Status) {
                Log.i("PlacesApiError", "An error occurred: $status")
            }
        })
        autocompleteSupportFragment2 = childFragmentManager.findFragmentById(R.id.autocomplete_fragment2) as AutocompleteSupportFragment
        autocompleteSupportFragment2.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG))
        autocompleteSupportFragment2.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                destinationText.error = null
                latLngDestination = place.latLng
                if (latLngDestination == null)  Toast.makeText(context, resources.getString(R.string.errorOnLocation),Toast.LENGTH_LONG).show()
            }
            override fun onError(status: Status) {
                Log.i("PlacesApiError", "An error occurred: $status")
            }
        })

    }

    private fun isValid():Boolean {
        var valid : Boolean = true

        if (TextUtils.isEmpty(selectedDate.text)){
            valid = false
            selectedDate.error = resources.getString(R.string.fieldRequired)
        } else selectedDate.setError(null)

        if (TextUtils.isEmpty(selectedHour.text)){
            valid = false
            selectedHour.error = resources.getString(R.string.fieldRequired)
        } else selectedHour.setError(null)


       if (dropVehicles.selectedItem==null){
           valid = false
           vehicleLabel.error = resources.getString(R.string.fieldRequired)
       } else vehicleLabel.setError(null)

       if (dropSeats.selectedItem==null){
           valid = false
           SeatsLabel.error = resources.getString(R.string.fieldRequired)
       } else SeatsLabel.setError(null)

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