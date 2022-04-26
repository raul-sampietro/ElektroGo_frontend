package elektrogo.front.ui.ValorarUsuari

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import elektrogo.front.R

class ValorarUsuari : Fragment() {

    companion object {
        fun newInstance() = ValorarUsuari()
    }

    private lateinit var viewModel: ValorarUsuariViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.valorar_usuari_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ValorarUsuariViewModel::class.java)
        // TODO: Use the ViewModel
    }

}