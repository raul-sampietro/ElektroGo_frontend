/**
 * @file ReportUserFragment.kt
 * @author Gerard Castell
 * @brief Aquesta classe implementa la logica associada a la vista de reportar un usuari
 *
 */
package elektrogo.front.ui.report
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import elektrogo.front.R
import elektrogo.front.model.Vehicle
import java.util.*


/**
 * @brief La classe Report User obte i comprova les dades obtingudes a la vista report_user.xml i fa les crides amb backend per guardar la informacio
 */
class ReportUserFragment : AppCompatActivity() {
    companion object {
        fun newInstance() = ReportUserFragment()
    }

    private lateinit var modelView: ReportUserModelView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.report_user, container, false)
    }

}