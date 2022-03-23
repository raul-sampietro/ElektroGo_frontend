package elektrogo.front
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

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

    suspend fun doGetTest(): String {
        val httpResponse: HttpResponse = client.get(URL_BASE)
        val stringBody: String = httpResponse.receive()
        return stringBody
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
}
