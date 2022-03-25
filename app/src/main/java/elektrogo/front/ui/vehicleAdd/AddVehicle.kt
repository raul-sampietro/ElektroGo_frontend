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
import elektrogo.front.R
import elektrogo.front.model.Vehicle
import java.io.FileNotFoundException
import java.io.IOException
import java.util.*

class AddVehicle : AppCompatActivity() {
    private val selectPhoto = 1
    private var imageUri: Uri? = null
    private var bitmapVehicleImage: Bitmap? = null //Bitmap de la imatge del cotxe
    private val addVehicleModelView = AddVehicleModelView()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_vehicle)

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
                    drivingRange.getText().toString().toInt(), dropYearSpinner.selectedItem.toString().toInt(), seatsVehcile.getText().toString().toInt(), null)
                addVehicleModelView.sendVehicleInfo(vehicleInfo,bitmapVehicleImage!!)
                /*
                if (statusCode == 433) Toast.makeText(this, resources.getString(R.string.DriverVehicleAlreadyExists), Toast.LENGTH_LONG).show()
                else if (statusCode == 439 || statusCode == 435) Toast.makeText(this, resources.getString(R.string.VehicleAlreadyExists), Toast.LENGTH_LONG).show()
                else if (statusCode in 200..299){
                    addVehicleModelView.sendVehiclePhoto(licensePlate.getText().toString(), bitmapVehicleImage!!)
                    Toast.makeText(this, resources.getString(R.string.VehicleCreatedSuccessfully), Toast.LENGTH_LONG).show()
                }

                 */

                Toast.makeText(this, resources.getString(R.string.VehicleCreatedSuccessfully), Toast.LENGTH_SHORT).show()
                finishActivity(Activity.RESULT_OK) //Back to menu
            }
            else Toast.makeText(this, resources.getString(R.string.VehicleNotCreated), Toast.LENGTH_SHORT).show()
        }
    }



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





}