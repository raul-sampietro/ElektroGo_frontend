/**
 * @file routeFragment.kt
 * @author Marina Alapont
 * @date 26/03/2022
 * @brief Implementacio d'un fragment per tal d'entrar dades per la creacio d'una ruta.
 */
package elektrogo.front.ui.route

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
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

/**
 * @brief La clase Rating representa la GUI de la pantalla on l'usuari inserira les dades per demanar la generacio d'una ruta al sistema.
 */

class RouteFragment() : Fragment() {

    /**
     * @brief Instancia de la classe rutaModelView.
     */
    private val rutaModelView = RouteModelView()
    /**
     * @brief Instancia d'un placesClient de l'api Places.
     */
    private lateinit var placesClient: PlacesClient
    /**
     * @brief Valor de la latitud i longitud de l'origen.
     */
    private var latLngOrigin : LatLng?= null

    /**
     * @brief Valor de la latitud del desti.
     */
    private var latLngDestination : LatLng?= null

    /**
     * @brief Instancia que s'inicialitzara més tard per l'AutocompleteSupportFragment del primer cercador, de l'api Places.
     */
    private lateinit var autocompleteSupportFragment : AutocompleteSupportFragment
    /**
     * @brief Instancia que s'inicialitzara més tard per l'AutocompleteSupportFragment del segon cercador, de l'api Places.
     */
    private lateinit var autocompleteSupportFragment2 : AutocompleteSupportFragment
    /**
     * @brief Intsancia que s'inicialitzara més tard per al EditText que conte l'autonomia .
     */
    private lateinit var textAutonomia : EditText
    /**
     * @brief Intsancia que s'inicialitzara més tard per al TextView que mostrara errors per al primer cercador .
     */
    private lateinit var originText : TextView
    /**
     * @brief Intsancia que s'inicialitzara més tard per al TextView que mostrara errors per al segon cercador .
     */
    private lateinit var destinationText : TextView

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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ruta, container, false)
    }
    /**
     * @brief Metode que s'executa un cop la vista ha estat creada. Conté tot el funcionament dels cercadors i els errors.
     * @param view Vista que s'ha creat.
     * @param savedInstanceState Estat de la instancia.
     * @pre
     * @post capta les escritures de l'usuari als cercadors i en fa l'autocomplete. Al premer el botó crida la funcio on es redirigira a l'usuari. Si no s'ha omplert cap camp mostra l'error corresponent.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
         textAutonomia = requireActivity().findViewById(R.id.editTextAutonomia)
         originText = requireActivity().findViewById(R.id.errorViewOrigin)
         destinationText = requireActivity().findViewById(R.id.errorViewDestination)

        if (!Places.isInitialized()) Places.initialize(this.requireContext(),resources.getString(R.string.google_maps_key))
        placesClient= Places.createClient(this.requireContext())
        val buttonRoute: Button = requireActivity().findViewById(R.id.buttonRoute)
        buttonRoute.setOnClickListener {
           if (!isValid()){
               Toast.makeText(context, resources.getString(R.string.errorOnFields),Toast.LENGTH_SHORT).show()
            }
            else {
               val waypoints : ArrayList<Double> = rutaModelView.sendRouteInfo(latLngOrigin!!, latLngDestination!!, textAutonomia.text.toString().toInt())
               if (waypoints.size==1) Toast.makeText(context, resources.getString(R.string.errorOnRoute),Toast.LENGTH_LONG).show()
               else {
                   var w: Int = 0
                   var stringURI: String =
                       "https://www.google.com/maps/dir/?api=1&origin=${latLngOrigin!!.latitude},${latLngOrigin!!.longitude}&destination=${latLngDestination!!.latitude},${latLngDestination!!.longitude}"
                   while (w < waypoints.size - 1) {
                       if (w == 0) stringURI += "&waypoints="
                       else stringURI += "|"
                       stringURI += "${waypoints[w]},${waypoints[w + 1]}"
                       w += 2
                   }
                   stringURI += "&travelmode=driving"
                   val mapIntent: Intent = Intent(Intent.ACTION_VIEW, Uri.parse(stringURI))
                   mapIntent.setPackage("com.google.android.apps.maps")
                   startActivity(mapIntent)
               }

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
    /**
     * @brief Listener sobre els cercadors, fa els autocomplete de les localitzacions.
     * @pre
     * @post Si l'usuari comença una cerca, es mostra les possibles localitzacions que està buscant. En clickar una localització es guarda les coordenades d'aquesta.
     */
    private fun getAutocompleteLocation () {
        autocompleteSupportFragment = childFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment
        autocompleteSupportFragment.setHint(getString(R.string.OrigenString))
        autocompleteSupportFragment.setPlaceFields(listOf(Place.Field.ID,Place.Field.NAME,Place.Field.LAT_LNG))
        autocompleteSupportFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener{
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
        autocompleteSupportFragment2.setHint(getString(R.string.DestiString))
        autocompleteSupportFragment2.setPlaceFields(listOf(Place.Field.ID,Place.Field.NAME,Place.Field.LAT_LNG))
        autocompleteSupportFragment2.setOnPlaceSelectedListener(object : PlaceSelectionListener{
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
    /**
     * @brief Metode que comprova que tots les camps de text obligatoris han estat omplerts.
     * @pre
     * @post Es dona valor a un boolea valid, que si es fals voldra dir que un camp obligatori no s'ha omplert.
     * @return Retorna el boolea valid.
     */

    private fun isValid():Boolean {
        var valid : Boolean = true
        if (TextUtils.isEmpty(textAutonomia.text)){
            valid = false
            textAutonomia.error = resources.getString(R.string.fieldRequired)
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
