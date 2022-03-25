package elektrogo.front.ui.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import elektrogo.front.MainActivity
import elektrogo.front.R
//import elektrogo.front.databinding.FragmentHomeBinding
import elektrogo.front.databinding.FragmentMapsBinding

class MapsFragment(mainActivity: MainActivity) : Fragment() {

    private var mainActivity= mainActivity

    /**
     * @brief Instancia de l'API GoogleMap.
     */
    private lateinit var mMap: GoogleMap
    /**
     * @brief Vinculacio de la vista.
     */

    private lateinit var binding: FragmentMapsBinding
    /**
     * @brief Valor que esperem que retorni el requeriment de permissos.
     */
    companion object{
        const val REQUEST_CODE_LOCATION= 0
    }
    /**
     * @brief Instancia del client de proveedor d'ubicacio de google service API.
     */
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    /**
     * @brief Token de cancelacio d'una tasca.
     */
    private var cancellationTokenSource = CancellationTokenSource()

    lateinit var placesClient: PlacesClient


    /**
     * @brief Metode executat un cop el mapa s'ha creat.
     * @param GoogleMap instancia de GoogleMap, adquirida gracies a la connexio amb l'API.
     * @pre el metode onCreateView() s'ha executat sense problemes
     * @post Es mostra el mapa amb l'opcio de geolocalitzacio. El mapa esta centrat primerament en la ubicacio actual aproximada del dispositiu si els permissos estan garantits.
     */
    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
        mMap = googleMap
        checkAndEnableLocation()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        val barcelona = LatLng(41.3879, 2.16992)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(barcelona))
        mMap.moveCamera(CameraUpdateFactory.zoomTo(7.0f))
        if (isLocationPermissionGranted()) {
                val currentLocationTask: Task<Location> = fusedLocationClient.getCurrentLocation(
                    LocationRequest.PRIORITY_HIGH_ACCURACY, cancellationTokenSource.token)
                currentLocationTask.addOnCompleteListener { task: Task<Location> ->
                    val currentLoc = if (task.isSuccessful) {
                        val currentLoc: Location = task.result
                        val loc = LatLng(currentLoc.latitude,currentLoc.longitude)
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(loc))
                        mMap.moveCamera(CameraUpdateFactory.zoomTo(14.0f))
                    } else {
                        val exception = task.exception
                    }
                }
        } else {
            val barcelona = LatLng(41.3879, 2.16992)
            mMap.moveCamera(CameraUpdateFactory.newLatLng(barcelona))
            mMap.moveCamera(CameraUpdateFactory.zoomTo(13.0f))

        }
        //TODO: Canviar la api key cada vegada que fem merge. Cadascu ha de posar la seva
        if (!Places.isInitialized()) Places.initialize(this.requireContext(),resources.getString(R.string.google_maps_key))
        placesClient= Places.createClient(this.requireContext())
        getAutocompleteLocation()
    }

    /**
     * @brief Metode que comprova si els permissos de localitzacio estan permesos.
     * @pre
     * @return Retorna un bolea segons si els permisos estan donats o no.
     */
    private fun isLocationPermissionGranted() = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED
    /**
     * @brief Metode que comprova si els permissos necessaris estan donats i si no ho estan, els demana.
     * @pre
     * @post Si els permissos no estan donats, els demana a l'usuari.
     */
    @SuppressLint("MissingPermission")
    private fun checkAndEnableLocation(){
        if(!::mMap.isInitialized) return
        if(isLocationPermissionGranted()){
            mMap.isMyLocationEnabled=true //It doesn't need to be fixed, it's not an error, it works anyways.
        }
        else {
            requestPermissions()
        }
    }
    /**
     * @brief Metode que demana al usuari que accepti els permissos necessaris.
     * @pre
     * @post Si ja els ha demanat algun cop i estan denegats, mostra un missatge demanant que els accepti. Si no els ha demanat mai, els demana.
     */
    private fun requestPermissions(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(mainActivity, Manifest.permission.ACCESS_FINE_LOCATION)){
            Toast.makeText(activity, "Accede a ajustes de tu dispositivo y acepta los permisos", Toast.LENGTH_SHORT).show()
        }
        else {
            ActivityCompat.requestPermissions(mainActivity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE_LOCATION
            )
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(mainActivity, Manifest.permission.ACCESS_COARSE_LOCATION)){
            Toast.makeText(activity, "Accede a ajustes de tu dispositivo y acepta los permisos", Toast.LENGTH_SHORT).show()
        }
        else {
            ActivityCompat.requestPermissions(mainActivity, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                REQUEST_CODE_LOCATION
            )
        }

    }

    /**
     * @brief Metode que analitza el resultat de les peticions de permissos.
     * @pre
     * @post Si el codi correpont al que s'esperava, s'activa MyLocation al mapa, en cas contrari es mostra un text per demanar de nou els permissos.
     */
    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            REQUEST_CODE_LOCATION -> if (grantResults.isNotEmpty()&& grantResults[0]== PackageManager.PERMISSION_GRANTED){
                mMap.isMyLocationEnabled=true //It doesn't need to be fixed, it's not an error, it works anyways.
            }
            else {
                Toast.makeText(context,"Para activar la localizacion ve a ajustes y acepta los permisos", Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }
    }
    //Maybe we should deal with the case where the users opens de app, gives permission but in middle of the executions goes to settings and deny permissions. At the moment it seems that
    // its dealed in the already existent methods.


    fun getAutocompleteLocation () {
        val autocompleteSupportFragment =
            childFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment
        autocompleteSupportFragment.setPlaceFields(
            listOf(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.LAT_LNG
            )
        )
        autocompleteSupportFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                val latLng = place.latLng
                if (latLng != null) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
                    mMap.moveCamera(CameraUpdateFactory.zoomTo(17.0f))
                    mMap.addMarker(MarkerOptions().position(latLng).title(place.name))
                } else Toast.makeText(context, "hi ha hagut un error", Toast.LENGTH_SHORT).show()
            }

            override fun onError(status: Status) {
                Log.i("PlacesApiError", "An error occurred: $status")
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map2) as SupportMapFragment
        mapFragment.getMapAsync(callback)
    }
}