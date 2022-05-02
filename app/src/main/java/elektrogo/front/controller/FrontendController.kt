package elektrogo.front.controller
import android.graphics.Bitmap
import android.util.Log
import com.google.gson.Gson
import elektrogo.front.model.*
import io.ktor.client.*
import io.ktor.client.call.*
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
    //private const val URL_BASE = "http://10.6.11.69:8080/"
    private const val URL_VEHICLE = "${URL_BASE}vehicle/"
    private const val URL_USER = "${URL_BASE}user/"

    //Add functions you need here :)
    private val client = HttpClient(Android) {   //Exemple de com fer una crida amb el nostre servidor!
        expectSuccess = false
        engine {
            connectTimeout = 10_000
            socketTimeout = 10_000
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

    suspend fun sendVehicleInfo(vehicleInfo: Vehicle): Int {
        val httpResponse: HttpResponse = client.post("${URL_VEHICLE}create?") {
            parameter("userNDriver", "Test2")
            contentType(ContentType.Application.Json)
            body = vehicleInfo
        }
        if (httpResponse.status.value != 200) {
            val responseJson = Gson().fromJson(httpResponse.readText(), httpRespostes::class.java)
            val statusCode = responseJson.status
            return statusCode
        }
        else return httpResponse.status.value
    }

    @OptIn(InternalAPI::class)
    suspend fun sendVehiclePhoto(licensePlate: String, vehiclePic: Bitmap){
        val stream = ByteArrayOutputStream()
        vehiclePic.compress(Bitmap.CompressFormat.PNG, 20, stream)
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

    suspend fun sendRouteInfo(latitudeOrigin: Double, longitudeOrigin: Double, latitudeDestination: Double, longitudeDestination: Double, drivingRange: Int): ArrayList<Double> {
        val httpResponse: HttpResponse = client.get("${URL_BASE}route/calculate"){
            parameter("oriLat", latitudeOrigin)
            parameter("oriLon", longitudeOrigin)
            parameter("destLat", latitudeDestination)
            parameter("destLon", longitudeDestination)
            parameter("range", drivingRange)
        }
        val waypoints: ArrayList<Double>
        if (httpResponse.status.value != 200) {
            val responseJson = Gson().fromJson(httpResponse.readText(), httpRespostes::class.java)
            val statusCode = responseJson.status
            waypoints = arrayListOf(statusCode.toDouble())
        }
        else  waypoints = httpResponse.receive()
        return waypoints
   }
    suspend fun getChargingPoints(): Pair<Int, ArrayList<ChargingStation>> {
        val httpResponse: HttpResponse = client.get("${URL_BASE}ChargingStations")
        val status: Int = httpResponse.status.value

        val stations: ArrayList<ChargingStation>
        if ( status != 200) stations = ArrayList<ChargingStation>()
        else stations = httpResponse.receive()
        
        return Pair(status, stations)
    }

    suspend fun saveCarpooling(trip: CarPooling): Int {
        val httpResponse: HttpResponse = client.post("${URL_BASE}car-pooling/create") {
            contentType(ContentType.Application.Json)
            body = trip
        }
        if (httpResponse.status.value != 200) {
            val responseJson = Gson().fromJson(httpResponse.readText(), httpRespostes::class.java)
            val statusCode = responseJson.status
            return statusCode
        } else return httpResponse.status.value
    }

   suspend fun getTrips(originLatitude: Double, originLongitude: Double, destinationLatitude: Double, destinationLongitude: Double, dateIni: String?, startTimeMin: String?, startTimeMax: String?): Pair<Int, ArrayList<CarPooling>> {
       val httpResponse: HttpResponse = client.get("${URL_BASE}car-pooling/sel"){
           parameter("LatO", originLatitude)
           parameter("LongO", originLongitude)
           parameter("LatD", destinationLatitude)
           parameter("LongD", destinationLongitude)
           parameter("sDate", dateIni)
           parameter("sTimeMin", startTimeMin)
           parameter("sTimeMax", startTimeMax)
       }
       val trips: ArrayList<CarPooling>
       val status: Int = httpResponse.status.value
       if (httpResponse.status.value != 200) {
           trips = ArrayList<CarPooling>()
       }
       else trips = httpResponse.receive()
       return Pair(status,trips)
   }

    suspend fun getRating (username: String): Pair<Int,Double> {
        val httpResponse: HttpResponse = client.get("${URL_BASE}user/avgRate") {
            contentType(ContentType.Application.Json)
            parameter("userName", username)
        }
        var status : Int = httpResponse.status.value
        var avgRating : Double
        if (httpResponse.status.value != 200) {
           /* val responseJson = Gson().fromJson(httpResponse.readText(), httpRespostes::class.java)
            val statusCode = responseJson.status
            status = statusCode*/
            avgRating  = -1.0
        } else avgRating = httpResponse.receive()
        return Pair(status,avgRating)
    }

    suspend fun getUserById(id: String, provider: String): User? {
        val httpResponse: HttpResponse = client.get(URL_USER) {
            parameter("id", id)
            parameter("provider", provider)
        }
        if (httpResponse.status.value != 200) {
            return null
        }
        return httpResponse.receive()
    }

    suspend fun addUser(user: User): Int {
        val httpResponse: HttpResponse = client.post("${URL_BASE}users/create") {
            contentType(ContentType.Application.Json)
            body = user
        }
        if (httpResponse.status.value != 200) {
            val responseJson = Gson().fromJson(httpResponse.readText(), httpRespostes::class.java)
            val statusCode = responseJson.status
            return statusCode
        }
        else return httpResponse.status.value
    }
}

