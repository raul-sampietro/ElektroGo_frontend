/**
 * @file ReportUserFragment.kt
 * @author Gerard Castell
 * @brief Aquesta classe implementa la logica associada a la vista de reportar un usuari
 *
 */
package elektrogo.front.ui.report
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import elektrogo.front.R
import elektrogo.front.controller.FrontendController
import elektrogo.front.controller.session.SessionController
import elektrogo.front.model.Rating
import elektrogo.front.model.Report
import elektrogo.front.ui.profile.GuestProfileFragment
import kotlinx.coroutines.runBlocking


/**
 * @brief La classe Report User obte i comprova les dades obtingudes a la vista report_user.xml i fa les crides amb backend per guardar la informacio
 */
class ReportUserFragment : Fragment() {

    companion object {
        fun newInstance() = ReportUserFragment()
    }

    private lateinit var modelView: ReportUserModelView
    private lateinit var reportedUser:String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.report_user, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        reportedUser = arguments?.getString("guestUser")!!
        val textReport: EditText = requireView().findViewById(R.id.editTextTextMultiLine)
        val button: Button = requireView().findViewById(R.id.button)
        val userWhoRates = SessionController.getUsername(requireActivity())

        button.setOnClickListener {
            val textReportString = textReport.getText()

            if (TextUtils.isEmpty(textReportString)) {
                textReport.setError(resources.getString(R.string.ObligatoryField))
            }
            else {
                val rep = Report(userWhoRates, reportedUser, textReport.text.toString())
                var status = -1
                try { status = runBlocking{ FrontendController.reportUser(rep)} }
                catch (e: Exception) {}

                if (status == 200) Toast.makeText(activity, "Has reportat a $reportedUser", Toast.LENGTH_SHORT).show()
                else Toast.makeText(activity, "No s'ha pogut reportar a $reportedUser.", Toast.LENGTH_SHORT).show()


            }

        }
    }

    fun setRatedUser(username: String) { reportedUser = username }


}