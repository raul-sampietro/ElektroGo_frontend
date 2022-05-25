/**
 * @file FrontendController.kt
 * @author Simon Oliva, Raül Sampietro, Marina Alapont, Joel Cardona, Adria Abad
 * @brief Aquesta classe es un controlador singleton que serveix per comunicar Frontend amb Backend mitjançant crides HTTP.
 */
package elektrogo.front.controller
import android.graphics.Bitmap
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

/**
 * @brief L'objecte FrontendController es un singleton que conte totes les funcions que fan crides HTTP a Backend.
 */
object FrontendController {
    // SERVER
    private const val URL_BASE_WB = "http://10.4.41.58:8080/"
    private const val URL_BASE = "http://10.4.41.58:8080"
    // HOME
    // private const val URL_BASE_WB = "http://192.168.1.82:8080/"
    // private const val URL_BASE = "http://192.168.1.82:8080"
    // MOBILE NETWORK
    //private const val URL_BASE_WB = "http://192.168.43.104:8080/"
    //private const val URL_BASE = "http://192.168.43.104:8080"

    //Add functions you need here :)
    private val client =
        HttpClient(Android) {
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

    // #################################################
    // #  USERS                                        #
    // #################################################

    private const val URL_USERS = "${URL_BASE}/users"

    suspend fun addUser(user: User): Int {
        val httpResponse: HttpResponse = client.post(URL_USERS) {
            contentType(ContentType.Application.Json)
            body = user
        }
        if (httpResponse.status.value != 201) {
            val responseJson = Gson().fromJson(httpResponse.readText(), httpRespostes::class.java)
            val statusCode = responseJson.status
            return statusCode
        }
        else return httpResponse.status.value
    }

    suspend fun getUserById(id: String, provider: String): User? {
        val httpResponse: HttpResponse = client.get("${URL_USERS}/provider/${provider}/id/${id}")
        if (httpResponse.status.value != 200) {
            return null
        }
        return httpResponse.receive()
    }

    /**
     * @brief Metode que es comunica amb BackEnd per tal d'obtenir el path o uri de la fotografia de perfil d'un usuari.
     * @param username nom d'usuari del usuari per el que volem la imatge de perfil.
     * @return Retorna un String que es el path de la imatge de perfil de l'usuari per el qual l'hem demanat, si no en te retorna el string buit.
     */
    suspend fun getUserProfilePhoto(username: String): String {
        val httpResponse: HttpResponse = client.get("${URL_USERS}/${username}")
        if (httpResponse.status.value != 200) {
            return ""
        }
        val user : User = httpResponse.receive()
        return user.imageUrl
    }

    suspend fun deleteUser(username: String): Int {
        val httpResponse: HttpResponse = client.delete("${URL_USERS}/${username}")
        return if (httpResponse.status.value != 200) {
            val responseJson = Gson().fromJson(httpResponse.readText(), httpRespostes::class.java)
            responseJson.status
        } else httpResponse.status.value
    }

    // #################################################
    // #  RATINGS                                      #
    // #################################################

    private const val URL_RATINGS = "${URL_BASE}/ratings"

    /**
     * @brief Metode que envia un Rating d'un usuari a Backend per enregistrar-lo a la BD.
     * @pre
     * @post Si s'ha pogut connectar amb el servidor, retorna l'status de la crida HTTP.
     */
    suspend fun rateUser(rating: Rating): Int {
        val httpResponse: HttpResponse = client.post(URL_RATINGS) {
            contentType(ContentType.Application.Json)
            body = rating
        }
        return httpResponse.status.value
    }

    /**
     * @brief Metode que es comunica amb Backend per tal d'obtenir la valoracio mitjana d'un usuari.
     * @param username nom d'usuari del usuari per el que volem la valoracio mitjana.
     * @return Retorna un Pair<Int,RatingAvg> on el int es el code status i RatingAvg un objecte amb valor del rating i numero de persones que han valorat.
     */
    suspend fun getRating(username: String): Pair<Int, RatingAvg?> {
        val httpResponse: HttpResponse = client.get("${URL_RATINGS}/to/${username}/avg") {
            contentType(ContentType.Application.Json)
        }
        val status: Int = httpResponse.status.value
        val avgRating: RatingAvg?
        if (httpResponse.status.value != 200) {
            avgRating = RatingAvg(-1.0,-1)
        } else avgRating = httpResponse.receive()
        return Pair(status, avgRating)
    }

    suspend fun unrateUser(userFrom: String, userTo: String): Int {
        val httpResponse: HttpResponse = client.delete("${URL_RATINGS}/from/${userFrom}/to/${userTo}")
        return httpResponse.status.value
    }

    // #################################################
    // #  REPORTS                                      #
    // #################################################

    private const val URL_REPORTS = "${URL_BASE}/reports"

    // #################################################
    // #  DRIVERS                                      #
    // #################################################

    private const val URL_DRIVERS = "${URL_BASE}/drivers"

    suspend fun addDriver(username: String): Int {
        val httpResponse: HttpResponse = client.post("${URL_DRIVERS}/${username}")
        if (httpResponse.status.value != 200) {
            val responseJson = Gson().fromJson(httpResponse.readText(), httpRespostes::class.java)
            val statusCode = responseJson.status
            return statusCode
        }
        else return httpResponse.status.value
    }

    suspend fun  getDriver(username: String): Boolean {
        val httpResponse: HttpResponse = client.get("${URL_DRIVERS}/${username}")
        return httpResponse.status.value == 200
    }

    // #################################################
    // #  VEHICLES                                     #
    // #################################################

    private const val URL_VEHICLES = "${URL_BASE}/vehicles"

    suspend fun sendVehicleInfo(vehicleInfo: Vehicle, username: String): Int {
        val httpResponse: HttpResponse = client.post("${URL_VEHICLES}/from/${username}") {
            contentType(ContentType.Application.Json)
            body = vehicleInfo
        }
        if (httpResponse.status.value != 200) {
            val responseJson = Gson().fromJson(httpResponse.readText(), httpRespostes::class.java)
            val statusCode = responseJson.status
            return statusCode
        } else return httpResponse.status.value
    }

    //TODO POT FALLAR
    @OptIn(InternalAPI::class)
    suspend fun sendVehiclePhoto(licensePlate: String, vehiclePic: Bitmap) {
        val stream = ByteArrayOutputStream()
        vehiclePic.compress(Bitmap.CompressFormat.PNG, 20, stream)
        val image = stream.toByteArray()
        // TODO pas de parametres Http
        val response: HttpResponse = client.submitFormWithBinaryData(
            url = "${URL_VEHICLES}/${licensePlate}/image",
            formData = formData {
                append("image", image, Headers.build {
                    append(HttpHeaders.ContentType, "image/png")
                    append(HttpHeaders.ContentDisposition, "filename=ignore.png")
                })
            }
        )
    }

    suspend fun getVehicleList(username: String): ArrayList<Vehicle> {
        val vehicles: ArrayList<Vehicle> = client.get("${URL_VEHICLES}/from/${username}")
        return vehicles
    }

    suspend fun deleteVehicle(numberPlate: String, username: String) {
        val response: HttpResponse = client.delete("${URL_VEHICLES}/${numberPlate}/from/${username}")
    }

    // #################################################
    // #  CHARGING STATIONS                            #
    // #################################################

    private const val URL_CHARGING_STATIONS = "${URL_BASE}/charging-stations"

    /**
     * @brief Metode que obte totes les ChargingStations emmagatzemades as la BD de Backend.
     * @pre
     * @post Si s'ha pogut connectar amb el servidor, totes les ChargingStations de la BD s'han afegit a l'arrayList stations, i es retornen juntament amb l'status de la crida HTTP.
     */
    suspend fun getChargingPoints(): Pair<Int, ArrayList<ChargingStation>> {
        val httpResponse: HttpResponse = client.get(URL_CHARGING_STATIONS)
        val status: Int = httpResponse.status.value

        val stations: ArrayList<ChargingStation>
        if (status != 200) stations = ArrayList<ChargingStation>()
        else stations = httpResponse.receive()

        return Pair(status, stations)
    }

    // #################################################
    // #  CAR POOLING                                  #
    // #################################################

    private const val URL_CAR_POOLING = "${URL_BASE}/car-pooling"

    suspend fun saveCarpooling(trip: CarPooling): Int {
        val httpResponse: HttpResponse = client.post(URL_CAR_POOLING) {
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
        startTimeMax: String?,
        username: String
    ): Pair<Int, ArrayList<CarPooling>> {
        try {
            val httpResponse: HttpResponse = client.get("${URL_CAR_POOLING}/search") {
                parameter("LatO", originLatitude)
                parameter("LongO", originLongitude)
                parameter("LatD", destinationLatitude)
                parameter("LongD", destinationLongitude)
                parameter("sDate", dateIni)
                parameter("sTimeMin", startTimeMin)
                parameter("sTimeMax", startTimeMax)
                parameter("username", username)
            }
            val trips: ArrayList<CarPooling>
            val status: Int = httpResponse.status.value
            if (httpResponse.status.value != 200) {
                trips = ArrayList<CarPooling>()
            } else trips = httpResponse.receive()
            return Pair(status, trips)
        }
        catch(e: Exception){
            return Pair(504, ArrayList<CarPooling>())
        }
    }

    suspend fun getAllTrips(): Pair<Int, ArrayList<CarPooling>> {
        val httpResponse: HttpResponse = client.get(URL_CAR_POOLING) {

        }
        val trips: ArrayList<CarPooling>
        val status: Int = httpResponse.status.value
        if (httpResponse.status.value != 200) {
            trips = ArrayList<CarPooling>()
        } else trips = httpResponse.receive()
        return Pair(status, trips)
    }

    suspend fun getTripsByUsername(username: String?): Pair<Int, ArrayList<CarPooling>> {
        val httpResponse: HttpResponse = client.get("${URL_CAR_POOLING}/from/${username}")
        val trips: ArrayList<CarPooling>
        val status: Int = httpResponse.status.value
        if (httpResponse.status.value != 200) {
            trips = ArrayList<CarPooling>()
        } else trips = httpResponse.receive()
        return Pair(status, trips)
    }

    suspend fun askForTripsDefault(username: String): Pair<Int, ArrayList<CarPooling>> {
        try {
            val httpResponse: HttpResponse = client.get(URL_CAR_POOLING) {
                parameter("order", true)
                parameter("username", username)
            }
            val trips: ArrayList<CarPooling>
            val status: Int = httpResponse.status.value
            if (httpResponse.status.value != 200) {
                trips = ArrayList<CarPooling>()
            } else trips = httpResponse.receive()
            return Pair(status, trips)
        }
        catch(e: Exception){
            return Pair(504, ArrayList<CarPooling>())
        }
    }

    // #################################################
    // #  ROUTES                                       #
    // #################################################

    private const val URL_ROUTES = "${URL_BASE}/routes"

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
        val httpResponse: HttpResponse = client.get("${URL_BASE_WB}route/calculate") {
            parameter("latO", latitudeOrigin)
            parameter("longO", longitudeOrigin)
            parameter("latD", latitudeDestination)
            parameter("longD", longitudeDestination)
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

    // #################################################
    // #  CHATS                                        #
    // #################################################

    private const val URL_CHATS = "${URL_BASE}/chats"

    suspend fun getChatList(username: String): ArrayList<String> {
        val chats: ArrayList<String> = client.get("${URL_CHATS}/${username}")
        return chats
    }

    suspend fun getConversation(userA: String, userB: String): ArrayList<Message> {
        val chats: ArrayList<Message> = client.get("${URL_CHATS}/messages/${userA}/${userB}")
        return chats
    }

    suspend fun sendMessage(sender: String, receiver: String , message: String): Int  {
        val httpResponse: HttpResponse = client.post("$${URL_CHATS}/messages/${sender}/${receiver}"){
            parameter("message", message)
        }
        if (httpResponse.status.value != 200) {
            val responseJson = Gson().fromJson(httpResponse.readText(), httpRespostes::class.java)
            return responseJson.status
        }
        else return httpResponse.status.value
    }

    suspend fun deleteChat(userA: String, userB: String): Int {
        val httpResponse: HttpResponse = client.delete("${URL_CHATS}/${userA}/${userB}")
        if (httpResponse.status.value != 200) {
            val responseJson = Gson().fromJson(httpResponse.readText(), httpRespostes::class.java)
            return responseJson.status
        }
        else return httpResponse.status.value
    }

    suspend fun getReceivedMessages(user: String): ArrayList<Message> {
        val chats: ArrayList<Message> = client.get("${URL_CHATS}/messages/to/${user}")
        return chats
    }

    // #################################################
    // #  REVPOLLUTION SERVICE                         #
    // #################################################

    suspend fun getAirQuality(lat: Double, lon: Double): String {
        val httpResponse: HttpResponse = client.get("http://10.4.41.56/RevPollution/services/stations/quality?lat=${lat}&lon=${lon}")
        if (httpResponse.status.value != 200) {
            return ""
        }
        return httpResponse.receive()
    }

    suspend fun getUserCreatedTrips(username: String): Pair<Int, ArrayList<CarPooling>> {
        val httpResponse : HttpResponse = client.get ("${URL_CAR_POOLING}/created/${username}")
        val trips: ArrayList<CarPooling>
        var status: Int = httpResponse.status.value
        if (httpResponse.status.value != 200) {
            val responseJson = Gson().fromJson(httpResponse.readText(), httpRespostes::class.java)
            val statusCode = responseJson.status
            status=statusCode
            trips = ArrayList<CarPooling>()
        } else trips = httpResponse.receive()
        return Pair(status, trips)
    }

    suspend fun addMemberToATrip(username: String, tripId: Long?): Int {
        val httpResponse : HttpResponse = client.post("${URL_CAR_POOLING}/${tripId}/from/${username}")
        var status: Int = httpResponse.status.value
        if (status!=200) {
            val responseJson = Gson().fromJson(httpResponse.readText(), httpRespostes::class.java)
            val statusCode = responseJson.status
            status = statusCode
        }
        return status
    }
}

