package elektrogo.front.ui.carPooling

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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


class filtrarTrajectesFragment : Fragment() {

    companion object {
        fun newInstance() = filtrarTrajectesFragment()
    }

    private lateinit var placesClient: PlacesClient
    private lateinit var dataButton: Button
    private lateinit var timeFromButton: Button
    private lateinit var timeToButton: Button
    private lateinit var autocompleteSupportFragment: AutocompleteSupportFragment
    private lateinit var autocompleteSupportFragment2: AutocompleteSupportFragment
    private lateinit var originText : TextView
    private lateinit var destinationText : TextView
    private  var latLngOrigin : LatLng?=null
    private  var latLngDestination: LatLng?=null
    private lateinit var fromTimeSelected: String
    private lateinit var toTimeSelected: String
    private lateinit var dateSelected: String

    private lateinit var viewModel: FiltrarTrajectesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.filtrar_trajectes_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        dataButton = requireActivity().findViewById(R.id.dataButtonFiltrar)
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

        var day: Int = 28
        var month: Int = 10
        var year: Int = 1920
        var hour: Int = 11
        var minute: Int = 11

        dataButton.setOnClickListener {
            var calendar = Calendar.getInstance()
            day = calendar.get(Calendar.DAY_OF_MONTH)
            month = calendar.get(Calendar.MONTH)
            year = calendar.get(Calendar.YEAR)

            val dpd = DatePickerDialog(requireActivity(), DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                // Display Selected date in textbox
                dataButton.text = "$dayOfMonth/$monthOfYear/$year"
                dateSelected = "$dayOfMonth/$monthOfYear/$year"

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
                    timeFromButton.text = "$hour:0$minute"
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
                    timeToButton.text = "$hour:0$minute"
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


}