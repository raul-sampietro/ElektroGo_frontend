package elektrogo.front
import android.util.Log
import elektrogo.front.ui.VehicleModel
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.android.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

object FrontendController {
    //Add functions you need here :)
    private val client = HttpClient(Android){   //Exemple de com fer una crida amb el nostre servidor!
        engine {
            connectTimeout = 60_000
            socketTimeout = 60_000
        }
        install(JsonFeature) {
            serializer = KotlinxSerializer(kotlinx.serialization.json.Json {
                prettyPrint = true
                isLenient = true
            })
        }

    }

    suspend fun doGetTest(): String {
        val httpResponse: HttpResponse = client.get("http://10.4.41.58:8080/")
        val stringBody: String = httpResponse.receive()
        return stringBody
    }

    suspend fun sendVehicleInfo(vehicleInfo: VehicleModel) {
        val httpResponse: HttpResponse = client.post("http://10.4.41.58:8080/vehicle/create?userNDriver=Test"){
            contentType(ContentType.Application.Json)
            body = vehicleInfo
        }
        val stringBody: String = httpResponse.receive()
    }

}
