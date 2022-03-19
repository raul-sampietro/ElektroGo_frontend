package elektrogo.front

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import java.util.*

class AddVehicle : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_vehicle)

        val licensePlate: EditText = findViewById(R.id.LicensePlate)
        val vehicleModel: EditText = findViewById(R.id.VehicleModel)
        val brandVehicle: EditText = findViewById(R.id.BrandInput)
        val drivingRange: EditText = findViewById(R.id.drivingRangeInput)
        val fabricationYear: Spinner = findViewById(R.id.FabricationYearInput)

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

            if(isValid){
                //crida a la base de dades (per fer)
                finish() //Back to menu
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