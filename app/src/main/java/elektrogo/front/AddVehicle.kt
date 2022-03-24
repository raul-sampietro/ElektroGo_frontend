package elektrogo.front

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextUtils
import android.widget.*
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import elektrogo.front.ui.VehicleModel
import kotlinx.coroutines.launch
import java.io.FileNotFoundException
import java.io.IOException
import java.util.*

class AddVehicle : AppCompatActivity() {
    private val selectPhoto = 1
    private var imageUri: Uri? = null
    private var bitmapVehicleImage: Bitmap? = null //Bitmap de la imatge del cotxe

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
                isValid = checkLicensePlate(licensePlateString)
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

                var vehicleInfo = VehicleModel(brandVehicle.getText().toString(), vehicleModel.getText().toString(), licensePlate.getText().toString(),
                    drivingRange.getText().toString().toInt(), dropYearSpinner.selectedItem.toString().toInt(), seatsVehcile.getText().toString().toInt(), null)
                lifecycleScope.launch{
                    FrontendController.sendVehicleInfo(vehicleInfo)
                    FrontendController.sendVehiclePhoto(licensePlate.getText().toString(),
                        bitmapVehicleImage!!
                    )
                }
                Toast.makeText(this, resources.getString(R.string.VehicleCreatedSuccessfully), Toast.LENGTH_SHORT).show()
                finish() //Back to menu
            }
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



    private fun checkLicensePlate(licensePlateToCheck: Editable): Boolean {
        var isValid = true
        for(i in licensePlateToCheck.indices){   //Check licensePlate has valid input
            if (licensePlateToCheck.length == 7) { //Then it is a car
                if (i < 4 && licensePlateToCheck[i] !in '0'..'9') {
                    isValid = false
                } else if (i >= 4 && (licensePlateToCheck[i] !in 'A'..'Z' && licensePlateToCheck[i] !in 'a'..'z')) {
                    isValid = false
                }
            }
            else { //Then it is a moped
                if (i in 1..4 && licensePlateToCheck[i] !in '0'..'9') {
                    isValid = false
                } else if ((i >= 5 || i == 0) && (licensePlateToCheck[i] !in 'A'..'Z' && licensePlateToCheck[i] !in 'a'..'z')) {
                    isValid = false
                }
            }
        }
        return isValid
    }

}