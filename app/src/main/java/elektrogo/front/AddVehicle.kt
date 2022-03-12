package elektrogo.front

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText

class AddVehicle : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_vehicle)

        val LicensePlate: EditText = findViewById(R.id.LicensePlate)
        val VehicleModel: EditText = findViewById(R.id.VehicleModel)
        val SaveVehicleButton: Button = findViewById(R.id.SaveVehicleButton)

        SaveVehicleButton.setOnClickListener {
            if (TextUtils.isEmpty(LicensePlate.getText())) {
                LicensePlate.setError("Aquest camp és obligatori")
            }
            if (TextUtils.isEmpty(VehicleModel.getText())) {
                VehicleModel.setError("Aquest camp és obligatori")
            }
        }
    }

    /*
    override fun onResume(){
        super.onResume()

        SaveVehicleButton.setOnClickListener {
            if (TextUtils.isEmpty(LicensePlate.getText())) {
                LicensePlate.setError("Aquest camp es obligatori")

            }
        }
    }

     */
}