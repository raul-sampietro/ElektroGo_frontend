package elektrogo.front.ui.Ruta

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import elektrogo.front.MainActivity
import elektrogo.front.R


class RutaFragment(mainActivity: MainActivity) : Fragment() {

    var activity= mainActivity
    lateinit var placesClient: PlacesClient
    var latOrigin = 0.0
    var lngOrigin = 0.0
    var latDestination = 0.0
    var lngDestination = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ruta, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (!Places.isInitialized()) Places.initialize(this.requireContext(),"AIzaSyBbPEtPTJ0UUWGvgDviE0GtGzX2pSn7L5g")
        placesClient= Places.createClient(this.requireContext())
        getAutocompleteLocation()
    }

    fun getAutocompleteLocation () {
        val autocompleteSupportFragment = childFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment
        autocompleteSupportFragment.setPlaceFields(listOf(Place.Field.ID,Place.Field.NAME,Place.Field.LAT_LNG))
        autocompleteSupportFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener{
            override fun onPlaceSelected(place: Place) {
                val latLng = place.latLng
                if (latLng != null) {
                    latOrigin = latLng.latitude
                    lngOrigin = latLng.longitude
                }
                else Toast.makeText(context, "hi ha hagut un error",Toast.LENGTH_SHORT).show()
            }

            override fun onError(status: Status) {
                Log.i("PlacesApiError", "An error occurred: $status")
            }
        })
        val autocompleteSupportFragment2 = childFragmentManager.findFragmentById(R.id.autocomplete_fragment2) as AutocompleteSupportFragment
        autocompleteSupportFragment2.setPlaceFields(listOf(Place.Field.ID,Place.Field.NAME,Place.Field.LAT_LNG))
        autocompleteSupportFragment2.setOnPlaceSelectedListener(object : PlaceSelectionListener{
            override fun onPlaceSelected(place: Place) {
                val latLng2 = place.latLng
                if (latLng2 != null) {
                    latDestination = latLng2.latitude
                    lngDestination= latLng2.longitude
                }
                else Toast.makeText(context, "hi ha hagut un error",Toast.LENGTH_SHORT).show()
            }

            override fun onError(status: Status) {
                Log.i("PlacesApiError", "An error occurred: $status")
            }
        })
    }
}
