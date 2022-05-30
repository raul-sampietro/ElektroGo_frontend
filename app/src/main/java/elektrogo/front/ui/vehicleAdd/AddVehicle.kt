/**
 * @file AddVehicle.kt
 * @author Joel Cardona
 * @brief Aquesta classe implementa la logica associada a la vista de afegir vehicle.
 *
 */
package elektrogo.front.ui.vehicleAdd

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.widget.*
import androidx.appcompat.widget.Toolbar
import elektrogo.front.R
import elektrogo.front.controller.session.SessionController
import elektrogo.front.model.Vehicle
import elektrogo.front.ui.vehicleList.VehicleListActivity
import java.io.FileNotFoundException
import java.io.IOException
import java.util.*

/**
 * @brief La classe AddVehicle obte i comprova les dades obtingudes a la vista activity_add_vehicle.xml i fa les crides amb backend per guardar la informacio
 */
class AddVehicle : AppCompatActivity() {
    /**
     * @brief Nombre de fotografies que l'usuari ha d'afegir
     */
    private val selectPhoto = 1

    /**
     * @brief La Uri de la imatge del vehicle
     */
    private var imageUri: Uri? = null

    /**
     * @brief Bitmap on es guarda la imatge del vehicle que l'usuari afegeix
     */
    private var bitmapVehicleImage: Bitmap? = null //Bitmap de la imatge del cotxe

    /**
     * @brief Instancia de la classe AddVehicleModelView
     */
    private val addVehicleModelView = AddVehicleModelView()

    private lateinit var toolbar2 : Toolbar

    /**
     * @brief Metode que es crida quan es crea un AddVehicle activity
     * @param savedInstanceState rep informacio d'altres activitats
     * @post Si l'usuari ha afegit les dades correctament es fa una crida a backend per processar la informacio.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_vehicle)
        toolbar2  = findViewById(R.id.toolbar_main)
        toolbar2.title= getString(R.string.AddVehicle)
        toolbar2.setTitleTextColor(getColor(R.color.white))
        setSupportActionBar(toolbar2)


        val licensePlate: EditText = findViewById(R.id.LicensePlate)
        val vehicleModel: EditText = findViewById(R.id.VehicleModel)
        val brandVehicle: EditText = findViewById(R.id.BrandInput)
        val drivingRange: EditText = findViewById(R.id.drivingRangeInput)
        val seatsVehcile: EditText = findViewById(R.id.seatsVehicleInput)
        val fabricationYear: Spinner = findViewById(R.id.FabricationYearInput)
        var imageErrorMessage: TextView = findViewById(R.id.errorImage)
        val imageButton: Button = findViewById(R.id.addImage)


        imageButton.setOnClickListener(){
            val selectImageIntent: Intent = Intent(Intent.ACTION_PICK)
            selectImageIntent.setType("image/*")
            startActivityForResult(selectImageIntent, selectPhoto)
        }

        val saveVehicleButton: Button = findViewById(R.id.SaveVehicleButton)

        val year = Calendar.getInstance().get(Calendar.YEAR); //Obtenim l'any actual

        var dropYearSpinner: Spinner = findViewById(R.id.FabricationYearInput) //Obtinc l'spinner

        val any = (2000..year).toList().toTypedArray() //Passo el rang d'anys a una array

        val FabricationYearAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, any)  //Defineixo l'estil del spinner

        FabricationYearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)   //Defineixo l'estil del dropdown

        dropYearSpinner.setAdapter(FabricationYearAdapter)  //modifico l'adapter de l'spinner

        saveVehicleButton.setOnClickListener {
            var isValid = true
            val licensePlateString = licensePlate.getText()

            if (TextUtils.isEmpty(licensePlateString)) { //Check that licensePlate value is not null
                licensePlate.setError(resources.getString(R.string.ObligatoryField))
                isValid = false
            } else {
                isValid = addVehicleModelView.checkLicensePlate(licensePlateString)
                if(!isValid) licensePlate.setError(resources.getString(R.string.invalidLicensePlate))
            }

            if (isValid && licensePlateString.length < 7){    //Check licensePlate length is not less than seven
                licensePlate.setError(resources.getString(R.string.notEnoughCharactersLicensePlate))
                isValid = false
            }

            if (TextUtils.isEmpty(vehicleModel.getText())) {
                vehicleModel.setError(resources.getString(R.string.ObligatoryField))
                isValid = false
            }

            if(TextUtils.isEmpty(brandVehicle.getText())) {
                brandVehicle.setError(resources.getString(R.string.ObligatoryField))
                isValid = false
            }

            if(TextUtils.isEmpty(drivingRange.getText())) {
                drivingRange.setError(resources.getString(R.string.ObligatoryField))
                isValid = false
            }

            if(TextUtils.isEmpty(seatsVehcile.getText())) {
                seatsVehcile.setError(resources.getString(R.string.ObligatoryField))
                isValid = false
            }

            if(bitmapVehicleImage==null){
                isValid = false
                imageErrorMessage.setText(resources.getString(R.string.ObligatoryFieldImage))
            }

            if(bitmapVehicleImage!=null){
                imageErrorMessage.setText("")
            }

            if(isValid){

                var vehicleInfo = Vehicle(brandVehicle.getText().toString(), vehicleModel.getText().toString(), licensePlate.getText().toString(),
                    drivingRange.getText().toString().toInt(), dropYearSpinner.selectedItem.toString().toInt(), seatsVehcile.getText().toString().toInt(), null, "pending")
                var statusCode = addVehicleModelView.sendVehicleInfo(vehicleInfo, SessionController.getUsername(this))

                if (statusCode == 433) Toast.makeText(this, resources.getString(R.string.DriverVehicleAlreadyExists), Toast.LENGTH_LONG).show()
                else if (statusCode == 440) Toast.makeText(this, resources.getString(R.string.WrongVehicleInfo), Toast.LENGTH_LONG).show()
                else if (statusCode in 500..599) Toast.makeText(this, resources.getString(R.string.ServerError), Toast.LENGTH_LONG).show()
                else if (statusCode in 200..299){
                    addVehicleModelView.saveVehicleImage(licensePlate.text.toString(), bitmapVehicleImage!!)
                    Toast.makeText(this, resources.getString(R.string.VehicleCreatedSuccessfully), Toast.LENGTH_LONG).show()
                    var intent = Intent(this, VehicleListActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
            else Toast.makeText(this, resources.getString(R.string.VehicleNotCreated), Toast.LENGTH_LONG).show()
        }
    }


    /**
     * @brief Obte la imatge que l'usuari afegeix del vehicle
     * @param requestCode Codi de la peticio
     * @param resultCode Codi del resultat
     * @param data Intent  d'un altre activitat
     * @post Quan l'usuari selecciona una imatge es guarda la informacio d'aquesta als atributs imatgeUri i bitmapVehicleImage
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var imageErrorMessage: TextView = findViewById(R.id.errorImage)

        if(requestCode == selectPhoto && resultCode == RESULT_OK && data != null && data.getData() != null){
            imageUri = data.getData()!!
            imageErrorMessage.setText("")
            try{
                bitmapVehicleImage = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri)
                var imageView: ImageView = findViewById(R.id.vehicleImage)
                imageView.setImageBitmap(bitmapVehicleImage)
            } catch(e: FileNotFoundException){
                e.printStackTrace()
            } catch(e: IOException){
                e.printStackTrace()
            }
        }
    }

    //Listener del botó d'enrere de la barra d'Android
    override fun onBackPressed() {
        if (onBackPressedDispatcher.hasEnabledCallbacks()) {
            super.onBackPressed()
        } else {
            var intent = Intent(this, VehicleListActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


}