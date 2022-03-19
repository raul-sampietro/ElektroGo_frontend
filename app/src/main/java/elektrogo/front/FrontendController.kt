package elektrogo.front
import android.util.Log
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


    suspend fun doGetTest(): String {

        val client = HttpClient(Android){   //Exemple de com fer una crida amb el nostre servidor!
            engine {
                connectTimeout = 60_000
                socketTimeout = 60_000
            }
        }

        val httpResponse: HttpResponse = client.get("http://10.4.41.58:8080/")
        val stringBody: String = httpResponse.receive()
        client.close()
        return stringBody
    }

    fun test(): String{
        return "I work :)"
    }
}
