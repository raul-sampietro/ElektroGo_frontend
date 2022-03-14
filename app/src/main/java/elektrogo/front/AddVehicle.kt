package elektrogo.front

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText

class AddVehicle : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_vehicle)

        val licensePlate: EditText = findViewById(R.id.LicensePlate)
        val vehicleModel: EditText = findViewById(R.id.VehicleModel)
        val saveVehicleButton: Button = findViewById(R.id.SaveVehicleButton)

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