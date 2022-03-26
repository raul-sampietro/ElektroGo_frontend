package elektrogo.front.controller
import android.graphics.Bitmap
import elektrogo.front.model.Vehicle
import elektrogo.front.model.ChargingStation
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.util.*
import java.io.ByteArrayOutputStream

object FrontendController {
    private const val URL_BASE = "http://10.4.41.58:8080/"
    private const val URL_VEHICLE = "${URL_BASE}vehicle/"

    //Add functions you need here :)
    private val client = HttpClient(Android){   //Exemple de com fer una crida amb el nostre servidor!
        engine {
            connectTimeout = 60_000
            socketTimeout = 60_000
        }
        install(Logging) {
            level = LogLevel.ALL
        }
        install(JsonFeature) {
            serializer = KotlinxSerializer(kotlinx.serialization.json.Json {
                prettyPrint = true
                isLenient = true
            })
        }
    }

    suspend fun sendVehicleInfo(vehicleInfo: Vehicle) {
        val httpResponse: HttpResponse = client.post("http://10.4.41.58:8080/vehicle/create?userNDriver=Test"){
            contentType(ContentType.Application.Json)
            body = vehicleInfo
        }
    }

    @OptIn(InternalAPI::class)
    suspend fun sendVehiclePhoto(licensePlate: String, vehiclePic: Bitmap) {
        val stream = ByteArrayOutputStream()
        vehiclePic.compress(Bitmap.CompressFormat.PNG, 90, stream)
        val image = stream.toByteArray()
        // TODO pas de parametres Http
        val response: HttpResponse = client.submitFormWithBinaryData(
            url = "${URL_VEHICLE}setImage?numberPlate=$licensePlate",
            formData = formData {
                append("image", image, Headers.build {
                    append(HttpHeaders.ContentType, "image/png")
                    append(HttpHeaders.ContentDisposition, "filename=ignore.png")
                })
            }
        )
    }

    suspend fun getVehicleList(username: String): ArrayList<Vehicle> {
        val vehicles: ArrayList<Vehicle> = client.get("${URL_VEHICLE}readVehicles") {
            parameter("userName", username)
        }
        return vehicles
    }

    suspend fun deleteVehicle(username: String, numberPlate: String) {
        val response: HttpResponse = client.post("${URL_VEHICLE}deleteDriverVehicle") {
            parameter("nPVehicle", username)
            parameter("userDriver", numberPlate)
        }
    }

    suspend fun getChargingPoints(): ArrayList<ChargingStation> {
        return client.get("~${URL_BASE}ChargingStations")
    }
}
