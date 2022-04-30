package elektrogo.front.ui.map

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import elektrogo.front.R

class XinxetaMarcador : Fragment() {

    companion object {
        fun newInstance() = XinxetaMarcador()
    }

    private lateinit var viewModel: XinxetaMarcadorViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.xinxeta_marcador_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(XinxetaMarcadorViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    fun setNumChargers(n: Int) {
        var nChargersView = view?.findViewById<TextView>(R.id.descriptiva_deseignacio)
        if (nChargersView != null) {
            nChargersView.text = "Number of chargers: $n"
        }
    }
}