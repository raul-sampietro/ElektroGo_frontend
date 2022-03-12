package elektrogo.front.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import elektrogo.front.R
import elektrogo.front.databinding.ActivityMapsBinding

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    companion object{
        const val REQUEST_CODE_LOCATION= 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setMinZoomPreference(7.0f)
        val barcelona = LatLng(41.3879, 2.16992)
        mMap.addMarker(MarkerOptions().position(barcelona).title("Marker in Bcn"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(barcelona))
        checkAndEnableLocation()
    }

    private fun isLocationPermissionGranted() = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED

    private fun checkAndEnableLocation(){
        if(!::mMap.isInitialized) return
        if(isLocationPermissionGranted()){
            mMap.isMyLocationEnabled=true
        }
        else {
            requestPermissions()
        }
    }

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


    override fun onRequestPermissionsResult(  //tot i que esta en vermell no cal o res que funiona!
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            REQUEST_CODE_LOCATION -> if (grantResults.isNotEmpty()&& grantResults[0]== PackageManager.PERMISSION_GRANTED){
                mMap.isMyLocationEnabled=true
            }
            else {
                Toast.makeText(this,"Para activar la localizacion ve a ajustes y acepta los permisos", Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }
    }

    //FALTA TRACTAR (de moment sembla que no peta pero en teoria cal  tractar-ho) QUE SI POSA LA APP EN SEGUNDO PLANO, VA A AJUSTES A TREURE ELS PERMISSOS I TORNA, QUE NO PETI
}