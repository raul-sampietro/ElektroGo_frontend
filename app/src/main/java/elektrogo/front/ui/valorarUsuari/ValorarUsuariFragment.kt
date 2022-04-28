package elektrogo.front.ui.valorarUsuari

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import elektrogo.front.R

class ValorarUsuariFragment : Fragment() {

    companion object {
        fun newInstance() = ValorarUsuariFragment()
    }

    private lateinit var viewModel: ValorarUsuariViewModel

    private lateinit var star1: View
    private lateinit var star2: View
    private lateinit var star3: View
    private lateinit var star4: View
    private lateinit var star5: View


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        star1 = requireView().findViewById(R.id.star1)
        star2 = requireView().findViewById(R.id.star2)
        star3 = requireView().findViewById(R.id.star3)
        star4 = requireView().findViewById(R.id.star4)
        star5 = requireView().findViewById(R.id.star5)


        return inflater.inflate(R.layout.valorar_usuari_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ValorarUsuariViewModel::class.java)
        // TODO: Use the ViewModel
    }

}