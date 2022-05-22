package elektrogo.front.ui.valorarUsuari

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import elektrogo.front.R
import elektrogo.front.controller.FrontendController
import elektrogo.front.controller.session.SessionController
import elektrogo.front.model.Rating
import kotlinx.coroutines.runBlocking

class ValorarUsuariFragment : Fragment() {

    companion object {
        fun newInstance() = ValorarUsuariFragment()
    }

    private lateinit var viewModel: ValorarUsuariViewModel

    private var stars = arrayListOf<ImageView>()

    private var puntuacio = 0
    private lateinit var ratedUser:String
    private lateinit var commentView:TextInputEditText
    private lateinit var valorarBtn:Button



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.valorar_usuari_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ValorarUsuariViewModel::class.java)


        stars.add(requireView().findViewById(R.id.star1) as ImageView)
        stars.add(requireView().findViewById(R.id.star2) as ImageView)
        stars.add(requireView().findViewById(R.id.star3) as ImageView)
        stars.add(requireView().findViewById(R.id.star4) as ImageView)
        stars.add(requireView().findViewById(R.id.star5) as ImageView)

        for (i in stars.indices) {
            stars[i].setOnClickListener {
                puntuacio = if (puntuacio != i+1) i+1
                else 0
                setStars()
            }
        }

        commentView = requireView().findViewById(R.id.comment)

        valorarBtn = requireView().findViewById(R.id.buttonValorarUsuari)
        valorarBtn.setOnClickListener {

            val userWhoRates = SessionController.getUsername(requireActivity())
            val valoracio = Rating(userWhoRates, ratedUser, puntuacio, commentView.text.toString())

            var status = -1
            try { status = runBlocking{ FrontendController.rateUser(valoracio) } }
            catch (e: Exception) {}

            if (status == 200) Toast.makeText(activity, "Has valorat a $ratedUser amb $puntuacio estrelles.", Toast.LENGTH_SHORT).show()
            else Toast.makeText(activity, "No s'ha pogut valorar a $ratedUser.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setStars() {
        for (i in stars.indices) {
            if (i < puntuacio) stars[i].setImageResource(R.drawable.ic_starplena)
            else stars[i].setImageResource(R.drawable.ic_starbuida)
        }
    }

    fun setRatedUser(username: String) { ratedUser = username }

}