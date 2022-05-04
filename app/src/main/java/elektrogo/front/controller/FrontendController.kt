/**
 * @file FrontendController.kt
 * @author Simon Oliva, Raül Sampietro, Marina Alapont, Joel Cardona, Adria Abad
 * @brief Aquesta classe es un controlador singleton que serveix per comunicar Frontend amb Backend mitjançant crides HTTP.
 */
package elektrogo.front.controller
import android.graphics.Bitmap
import android.util.Log
import com.google.gson.Gson
import elektrogo.front.controller.session.SessionController
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

/**
 * @brief L'objecte FrontendController es un singleton que conte totes les funcions que fan crides HTTP a Backend.
 */
object FrontendController {
    private const val URL_BASE = "http://10.4.41.58:8080/"

    //private const val URL_BASE = "http://10.6.11.69:8080/"
    private const val URL_VEHICLE = "${URL_BASE}vehicle/"
    private const val URL_USER = "${URL_BASE}user/"

    //Add functions you need here :)
    private val client =
        HttpClient(Android) {   //Exemple de com fer una crida amb el nostre servidor!
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

    suspend fun sendVehicleInfo(vehicleInfo: Vehicle, username: String): Int {
        val httpResponse: HttpResponse = client.post("${URL_VEHICLE}create?") {
            parameter("userNDriver", username)
            contentType(ContentType.Application.Json)
            body = vehicleInfo
        }
        if (httpResponse.status.value != 200) {
            val responseJson = Gson().fromJson(httpResponse.readText(), httpRespostes::class.java)
            val statusCode = responseJson.status
            return statusCode
        } else return httpResponse.status.value
    }

    @OptIn(InternalAPI::class)
    suspend fun sendVehiclePhoto(licensePlate: String, vehiclePic: Bitmap) {
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

    /**
     * @brief S'encarrega de fer les crides a FrontendController corresponents per tal d'enviar la informacio d'origen, desti i autonomia per la ruta i rebre els waypoints.
     * @param latitudeDestination latitud  del lloc de desti.
     * @param longitudeDestination longitud del lloc de desti.
     * @param latitudeOrigin latitud del lloc d'origen.
     * @param longitudeOrigin longitud del lloc d'origen.
     * @param drivingRange autonomia del vehicle en aquell instant
     * @pre tots les parametres son no nulls
     * @return Retorna el statusCode dins d'una array en cas d'error o un array amb longituds i latituds que representen els waypoints en cas contrari.
     */
    suspend fun sendRouteInfo(
        latitudeOrigin: Double,
        longitudeOrigin: Double,
        latitudeDestination: Double,
        longitudeDestination: Double,
        drivingRange: Int
    ): ArrayList<Double> {
        val httpResponse: HttpResponse = client.get("${URL_BASE}route/calculate") {
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
        } else waypoints = httpResponse.receive()
        return waypoints
    }

    /**
     * @brief Metode que obte totes les ChargingStations emmagatzemades as la BD de Backend.
     * @pre
     * @post Si s'ha pogut connectar amb el servidor, totes les ChargingStations de la BD s'han afegit a l'arrayList stations, i es retornen juntament amb l'status de la crida HTTP.
     */
    suspend fun getChargingPoints(): Pair<Int, ArrayList<ChargingStation>> {
        val httpResponse: HttpResponse = client.get("${URL_BASE}ChargingStations")
        val status: Int = httpResponse.status.value

        val stations: ArrayList<ChargingStation>
        if (status != 200) stations = ArrayList<ChargingStation>()
        else stations = httpResponse.receive()

        return Pair(status, stations)
    }

    /**
     * @brief Metode que envia un Rating d'un usuari a Backend per enregistrar-lo a la BD.
     * @pre
     * @post Si s'ha pogut connectar amb el servidor, retorna l'status de la crida HTTP.
     */
    suspend fun rateUser(rating: Rating): Int {
        val httpResponse: HttpResponse = client.post("${URL_BASE}users/rate") { //confirmar que ha de ser post
            contentType(ContentType.Application.Json)
            body = rating
        }
        return httpResponse.status.value
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


    /**
     * @brief Metode que es comunica amb FrontendController per tal d'obtenir tots les trajectes pels quals els valors coincideixen amb els parametres passats.
     * @param originLatitude latitud del origen del trajecte desitjat.
     * @param originLongitude longitud del origen del trajecte desitjat.
     * @param destinationLatitude latitud del destí del trajecte desitjat.
     * @param destinationLongitude longitud del destí del trajecte desitjat.
     * @param dateIni data d'inici del trajecte
     * @param startTimeMin hora en la que es vol que com a mínim comenci el trajecte.
     * @param startTimeMax hora en la que es vol que com a macim comenci el trajecte.
     * @post Es retorna un llistat de objectes CarPooling que representen els trajectes que coincideixen amb la cerca.
     * @return Retorna un Pair < Int, Array<CarPooling>> on int es el status code i el array els trajectes resultants.
     */
    suspend fun getTrips(
        originLatitude: Double,
        originLongitude: Double,
        destinationLatitude: Double,
        destinationLongitude: Double,
        dateIni: String?,
        startTimeMin: String?,
        startTimeMax: String?
    ): Pair<Int, ArrayList<CarPooling>> {
        val httpResponse: HttpResponse = client.get("${URL_BASE}car-pooling/sel") {
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
        } else trips = httpResponse.receive()
        return Pair(status, trips)
    }

    suspend fun getAllTrips(): Pair<Int, ArrayList<CarPooling>> {
        val httpResponse: HttpResponse = client.get("${URL_BASE}car-poolings") {

        }
        val trips: ArrayList<CarPooling>
        val status: Int = httpResponse.status.value
        if (httpResponse.status.value != 200) {
            trips = ArrayList<CarPooling>()
        } else trips = httpResponse.receive()
        return Pair(status, trips)
    }


    /**
     * @brief Metode que es comunica amb Backend per tal d'obtenir la valoracio mitjana d'un usuari.
     * @param username nom d'usuari del usuari per el que volem la valoracio mitjana.
     * @return Retorna un Pair<Int,Double> on el int es el code status i el double la valoracio mitjana de l'usuari.
     */

    suspend fun getRating(username: String): Pair<Int, Double> {
        val httpResponse: HttpResponse = client.get("${URL_BASE}user/avgRate") {
            contentType(ContentType.Application.Json)
            parameter("userName", username)
        }
        val status: Int = httpResponse.status.value
        val avgRating: Double
        if (httpResponse.status.value != 200) {
            avgRating = -1.0
        } else avgRating = httpResponse.receive()
        return Pair(status, avgRating)
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

    suspend fun addDriver(driver: Driver): Int {
        val httpResponse: HttpResponse = client.post("${URL_BASE}drivers/create") {
            contentType(ContentType.Application.Json)
            body = driver
        }
        if (httpResponse.status.value != 200) {
            val responseJson = Gson().fromJson(httpResponse.readText(), httpRespostes::class.java)
            val statusCode = responseJson.status
            return statusCode
        }
        else return httpResponse.status.value
    }

    /**
     * @brief Metode que es comunica amb BackEnd per tal d'obtenir el path o uri de la fotografia de perfil d'un usuari.
     * @param username nom d'usuari del usuari per el que volem la imatge de perfil.
     * @return Retorna un String que es el path de la imatge de perfil de l'usuari per el qual l'hem demanat, si no en te retorna el string buit.
     */
    suspend fun getUserProfilePhoto(username: String): String {
        val httpResponse: HttpResponse = client.get(URL_USER) {
            parameter("username", username)
        }
        if (httpResponse.status.value != 200) {
            return ""
        }
        val user : User = httpResponse.receive()
        return user.imageUrl
    }

    suspend fun getChatList(username: String): ArrayList<String> {
        val chats: ArrayList<String> = client.get("${URL_BASE}chat/findByUser") {
            parameter("user", username)
        }
        return chats
    }

    suspend fun getConversation(userA: String, userB: String): ArrayList<Message> {
        val chats: ArrayList<Message> = client.get("${URL_BASE}chat/findByConversation") {
            parameter("userA", userA)
            parameter("userB", userB)
        }
        return chats
    }

    suspend fun sendMessage(sender: String, receiver: String , message: String): Int  {
        val httpResponse: HttpResponse = client.post("${URL_BASE}chat/sendMessage"){
            parameter("sender", sender)
            parameter("receiver", receiver)
            parameter("message", message)
        }
        if (httpResponse.status.value != 200) {
            val responseJson = Gson().fromJson(httpResponse.readText(), httpRespostes::class.java)
            return responseJson.status
        }
        else return httpResponse.status.value
    }

    suspend fun askForTripsDefault(): Pair<Int, ArrayList<CarPooling>> {
            val httpResponse: HttpResponse = client.get("${URL_BASE}car-pooling/sel")
            val trips: ArrayList<CarPooling>
            val status: Int = httpResponse.status.value
            if (httpResponse.status.value != 200) {
                trips = ArrayList<CarPooling>()
            } else trips = httpResponse.receive()
            return Pair(status, trips)
    }
}

