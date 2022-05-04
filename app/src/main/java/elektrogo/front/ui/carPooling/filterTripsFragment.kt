/**
 * @file filterTripsFragment.kt
 * @author Marina Alapont
 * @date 12/04/2022
 * @brief Implementacio d'un fragment per tal de cercar trajectes.
 */
package elektrogo.front.ui.carPooling

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog

import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import elektrogo.front.R
import elektrogo.front.model.CarPooling
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

/**
 * @brief La clase filterTripsFragment representa la GUI de la pantalla on l'usuari insereix les dades per cercar trajectes i on veu el llistat resultant.
 */
class filterTripsFragment : Fragment() {

    /**
     * @brief Instancia del client de la api Places de google maps.
     */
    private lateinit var placesClient: PlacesClient
    /**
     * @brief Referencia al boto del layout que s'usa per seleccionar la data. S'inicialitza mes tard.
     */
    private lateinit var dateButton: Button
    /**
     * @brief Referencia al boto del layout que s'usa per fer la cerca. S'inicialitza mes tard.
     */
    private lateinit var filtrarButton : Button
    /**
     * @brief Referencia al boto del layout que s'usa per resetejar tots els camps. S'inicialitza mes tard.
     */
    private lateinit var resetButton : Button
    /**
     * @brief Referencia al boto del layout que s'usa per seleccionar l'hora de partida minima. S'inicialitza mes tard.
     */
    private lateinit var timeFromButton: Button
    /**
     * @brief Referencia al boto del layout que s'usa per seleccionar l'hora de partida maxima. S'inicialitza mes tard.
     */
    private lateinit var timeToButton: Button
    /**
     * @brief Instancia que s'inicialitzara mes tard per l'AutocompleteSupportFragment del primer cercador, de l'api Places.
     */
    private lateinit var autocompleteSupportFragment : AutocompleteSupportFragment
    /**
     * @brief Instancia que s'inicialitzara mes tard per l'AutocompleteSupportFragment del segon cercador, de l'api Places.
     */
    private lateinit var autocompleteSupportFragment2 : AutocompleteSupportFragment
    /**
     * @brief Intsancia que s'inicialitzara mes tard per al TextView que mostrara errors per al primer cercador .
     */
    private lateinit var originText : TextView
    /**
     * @brief Intsancia que s'inicialitzara mes tard per al TextView que mostrara errors per al segon cercador .
     */
    private lateinit var destinationText : TextView
    /**
     * @brief Valor de la latitud i longitud de l'origen.
     */
    private var latLngOrigin : LatLng?= null

    /**
     * @brief Valor de la latitud del desti.
     */
    private var latLngDestination : LatLng?= null
    /**
     * @brief Variable que guardara el valor de l'hora minima de partida seleccionada.
     */
    private  var fromTimeSelected: String?=null
    /**
     * @brief Variable que guardara el valor de l'hora maxima de partida seleccionada.
     */
    private  var toTimeSelected: String?=null
    /**
     * @brief Variable que guardara el valor de la data de sortida seleccionada.
     */
    private  var dateSelected: String?=null
    /**
     * @brief Llista que guardara els diferents trajectes obtinguts de la cerca.
     */
    private lateinit var filteredList: ArrayList<CarPooling>

    /**
     * @brief Instancia de la classe FiltrarTrajectesViewModel.
     */
    private  var viewModel: filterTripsViewModel = filterTripsViewModel()

    /**
     * @brief Metode que s'executa al crear el fragment.
     * @pre
     * @post Es crea la vista per al fragment.
     * @return Retorna el layout creat.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.filter_trips_fragment, container, false)
        return view
    }

    /**
     * @brief Metode que s'executa un cop la vista ha estat creada. Conte tot el funcionament dels cercadors, els errors i el display de resultats.
     * @param view Vista que s'ha creat.
     * @param savedInstanceState Estat de la instancia.
     * @pre
     * @post capta les escritures de l'usuari als cercadors i diferents botons de data i hora . Al premer el boto de filtrar crida la funcio que capta les respostes i despres les mostra amb l'ajuda d'un ListAdapter. Si no s'ha omplert els camps obligatoris mostra l'error corresponent.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        dateButton = requireActivity().findViewById(R.id.dataButtonFiltrar)
        resetButton = requireActivity().findViewById(R.id.reset)
        filtrarButton = requireActivity().findViewById(R.id.Filtrar)
        timeFromButton=requireActivity().findViewById(R.id.timeFromButtonFiltrar)
        timeToButton=requireActivity().findViewById(R.id.timeToButtonFiltrar)
        originText = requireActivity().findViewById(R.id.errorViewOriginFiltrar)
        destinationText = requireActivity().findViewById(R.id.errorViewDestinationFiltrar)
        var createTripButton : com.google.android.material.floatingactionbutton.FloatingActionButton = requireActivity().findViewById(R.id.createTrip)

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
        //mostro uns trajectes default a la llista
        var resultDefault : Pair <Int, ArrayList<CarPooling>> = viewModel.askForTripsDefault()
        if (resultDefault.first != 200) {
            Toast.makeText(context, "Hi ha hagut un error, intenta-ho més tard", Toast.LENGTH_LONG).show()
        }
        else {
            filteredList = resultDefault.second
            listView.adapter = ListAdapter(context as Activity, filteredList)
        }
        filtrarButton.setOnClickListener {
            if (validate()) {
                //mostro els trajectes resultants de la busqueda
               var result : Pair <Int, ArrayList<CarPooling>> = viewModel.askForTrips(latLngOrigin, latLngDestination, dateSelected, fromTimeSelected, toTimeSelected)
                if (result.first != 200) {
                    Toast.makeText(context, "Hi ha hagut un error, intenta-ho més tard", Toast.LENGTH_LONG).show()
                }
                else {
                    filteredList = result.second
                    listView.adapter = ListAdapter(context as Activity, filteredList)
                }
            }
            else Toast.makeText(context, getString(R.string.errorFieldsFiltrar),Toast.LENGTH_SHORT).show()

        }
        resetButton.setOnClickListener {
            autocompleteSupportFragment.setText("")
            autocompleteSupportFragment2.setText("")
            latLngDestination= null
            latLngOrigin= null
            dateSelected = null
            fromTimeSelected = null
            toTimeSelected = null
            dateButton.text = "Data"
            timeToButton.text = "Fins a"
            timeFromButton.text = "Des de"
            autocompleteSupportFragment2.setText("")
            //mostro uns trajectes default a la llista
            var resultDefault : Pair <Int, ArrayList<CarPooling>> = viewModel.askForTripsDefault()
            if (resultDefault.first != 200) {
                Toast.makeText(context, "Hi ha hagut un error, intenta-ho més tard", Toast.LENGTH_LONG).show()
            }
            else {
                filteredList = resultDefault.second
                listView.adapter = ListAdapter(context as Activity, filteredList)
            }
        }

        listView.setOnItemClickListener(OnItemClickListener { parent, view, position, id ->
            val i = Intent(context, tripDetails::class.java)
            i.putExtra("username", filteredList[position].username)
            i.putExtra("startDate", filteredList[position].startDate)
            i.putExtra("startTime", filteredList[position].startTime)
            i.putExtra("offeredSeats",filteredList[position].offeredSeats)
            i.putExtra("occupiedSeats", filteredList[position].occupiedSeats)
            i.putExtra("restrictions", filteredList[position].restrictions)
            i.putExtra("details", filteredList[position].details)
            i.putExtra("originString", filteredList[position].origin)
            i.putExtra("destinationString", filteredList[position].destination)
            i.putExtra("vehicleNumberPlate", filteredList[position].vehicleNumberPlate)
            startActivity(i)
        })

        createTripButton.setOnClickListener {
            val fragmentNewCarPooling = NewCarPoolingFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.pooling, fragmentNewCarPooling, "findThisFragment")
                .addToBackStack(null)
                .commit();
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
                    dateSelected = "$year-0$correctMonth-0$dayOfMonth"
                }
                else if (dayOfMonth < 10 && correctMonth >= 10){
                    dateButton.text = "0$dayOfMonth/$correctMonth/$year"
                    dateSelected = "$year-$correctMonth-0$dayOfMonth"
                }
                else if (dayOfMonth >= 10 && correctMonth < 10){
                    dateButton.text = "$dayOfMonth/0$correctMonth/$year"
                    dateSelected = "$year-0$correctMonth-$dayOfMonth"
                }
                else {
                    dateButton.text = "$dayOfMonth/$correctMonth/$year"
                    dateSelected = "$year-$correctMonth-$dayOfMonth"
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
                    fromTimeSelected = "$hour:0$minute:00"
                }
                else {
                    timeFromButton.text = "$hour:$minute"
                    fromTimeSelected = "$hour:$minute:00"
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
                    toTimeSelected = "$hour:0$minute:00"
                }
                else {
                    timeToButton.text = "$hour:$minute"
                    toTimeSelected = "$hour:$minute:00"
                }
            }, minute, hour, true)

            tpd.show()
        }
    }
    /**
     * @brief Listener sobre els cercadors, fa els autocomplete de les localitzacions.
     * @pre
     * @post Si l'usuari comença una cerca, es mostra les possibles localitzacions que esta buscant. En clickar una localitzacio es guarda les coordenades d'aquesta.
     */
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

    /**
     * @brief Metode que comprova que tots les camps obligatoris han estat omplerts i que el que s'ha omplert es correcte.
     * @pre
     * @post Es dona valor a un boolea valid, que si es fals voldra dir que un camp obligatori no s'ha omplert.
     * @return Retorna el boolea valid.
     */

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