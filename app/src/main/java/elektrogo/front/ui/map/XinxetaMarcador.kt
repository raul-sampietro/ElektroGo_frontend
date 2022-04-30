package elektrogo.front.ui.map

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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


    fun setStationTitle(t: String) {
        var title = view?.findViewById<TextView>(R.id.stationTitle)
        if (title != null) title.text = t
    }

    @SuppressLint("SetTextI18n")
    fun setDescriptivaDesignacio(d: String?) {
        var descView = view?.findViewById<TextView>(R.id.descriptiva_designacio)
        if (descView != null) {
            if (d != null) descView.text = d
            else descView.text = "DESC: --"
        }
    }

    fun setTipusConnexio(c: String?) {
        var conView = view?.findViewById<TextView>(R.id.tipusConnexio)
        if (conView != null) {
            if (c != null) conView.text = c
            else conView.text = "--"
        }
    }

    fun setKW(kW: Double?) {
        var kWView = view?.findViewById<TextView>(R.id.kw)
        if (kWView != null) {
            if (kW != null) kWView.text = kW.toString()
            else kWView.text = "--"
        }
    }

    @SuppressLint("SetTextI18n")
    fun setACDC(acdc: String?) {
        var acdcView = view?.findViewById<TextView>(R.id.AcDc)
        if (acdcView != null) {
            if (acdc != null) acdcView.text = "AC/DC: $acdc"
            else acdcView.text = "--"
        }
    }

    fun setNumeroPlaces(p: String?) {
        var nPlacesView = view?.findViewById<TextView>(R.id.numeroPlaces)
        if (nPlacesView != null) {
            if (p != null) nPlacesView.text = p
            else nPlacesView.text = "--"
        }
    }

    fun setTipusVehicle(s: String?) {
        var vehicleView = view?.findViewById<ImageView>(R.id.tipusVehicle)
        if (vehicleView != null) {

            if (s != null) {
                if (s == "moto") {/*mostra moto_sola*/}
                else if (s.length > 8) {/*mostra cotxe_moto*/}
                else {/*mostrar cotxe*/}
            }
            else {/*mostrar interrogant o ovni*/}
        }
    }
}