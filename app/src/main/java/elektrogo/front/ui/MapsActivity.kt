/**
 * @file MapsActivity.kt
 * @author Marina Alapont
 * @date 12/03/2022
 * @brief Implementacio de la classe MapsActivity.
 */


package elektrogo.front.ui

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.Task
import elektrogo.front.R
import elektrogo.front.databinding.ActivityMapsBinding

/**
 * @brief La classe MapsActivity representa el mapa de la nostra aplicacio
 */
class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    /**
     * @brief Instancia de l'API GoogleMap.
     */
    private lateinit var mMap: GoogleMap
    /**
     * @brief Vinculacio de la vista.
     */
    private lateinit var binding: ActivityMapsBinding
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


    /**
     * @brief Metode executat al crear l'activity del mapa.
     * @param Bundle dades rebudes de l'activity anterior.
     * @pre
     * @post Al crear el mapa es crea un fragment associat a l'activity i s'enllaça aquesta amb el seu layout. Es mostra el mapa.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    /**
     * @brief Metode executat un cop el mapa s'ha creat.
     * @param GoogleMap instancia de GoogleMap, adquirida gracies a la connexio amb l'API.
     * @pre el metode onCreate() s'ha executat sense problemes
     * @post Es mostra el mapa amb l'opcio de geolocalitzacio. El mapa esta centrat primerament en la ubicacio actual aproximada del dispositiu si els permissos estan garantits.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        checkAndEnableLocation()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (isLocationPermissionGranted()) {
          if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
                return
            } else {
              val currentLocationTask: Task<Location> = fusedLocationClient.getCurrentLocation(PRIORITY_HIGH_ACCURACY, cancellationTokenSource.token)
                currentLocationTask.addOnCompleteListener { task: Task<Location> ->
                    val currentLoc = if (task.isSuccessful) {
                        val currentLoc: Location = task.result
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(currentLoc.latitude, currentLoc.longitude)))
                        mMap.moveCamera(CameraUpdateFactory.zoomTo(12.0f))
                    } else {
                        val exception = task.exception
                    }
                }
            }
        } else {
            val barcelona = LatLng(41.3879, 2.16992)
            mMap.moveCamera(CameraUpdateFactory.newLatLng(barcelona))
            mMap.moveCamera(CameraUpdateFactory.zoomTo(7.0f))

        }

    }

    /**
     * @brief Metode que comprova si els permissos de localitzacio estan permesos.
     * @pre
     * @return Retorna un bolea segons si els permisos estan donats o no.
     */
    private fun isLocationPermissionGranted() = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED
    /**
     * @brief Metode que comprova si els permissos necessaris estan donats i si no ho estan, els demana.
     * @pre
     * @post Si els permissos no estan donats, els demana a l'usuari.
     */
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
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){
            Toast.makeText(this, "Accede a ajustes de tu dispositivo y acepta los permisos", Toast.LENGTH_SHORT).show()
        }
        else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE_LOCATION)
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)){
            Toast.makeText(this, "Accede a ajustes de tu dispositivo y acepta los permisos", Toast.LENGTH_SHORT).show()
        }
        else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), REQUEST_CODE_LOCATION)
        }

    }

    /**
     * @brief Metode que analitza el resultat de les peticions de permissos.
     * @pre
     * @post Si el codi correpont al que s'esperava, s'activa MyLocation al mapa, en cas contrari es mostra un text per demanar de nou els permissos.
     */
    override fun onRequestPermissionsResult(  //It doesn't need to be fixed, it's not an error, it works anyways.
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            REQUEST_CODE_LOCATION -> if (grantResults.isNotEmpty()&& grantResults[0]== PackageManager.PERMISSION_GRANTED){
                mMap.isMyLocationEnabled=true //It doesn't need to be fixed, it's not an error, it works anyways.
            }
            else {
                Toast.makeText(this,"Para activar la localizacion ve a ajustes y acepta los permisos", Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }
    }
     //Maybe we should deal with the case where the users opens de app, gives permission but in middle of the executions goes to settings and deny permissions. At the moment it seems that
     // its dealed in the already existent methods.
}