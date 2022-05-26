package elektrogo.front.ui.vehicleList

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import elektrogo.front.R

class NewDriverFragment : Fragment() {

    companion object {
        fun newInstance() = NewDriverFragment()
    }

    private lateinit var viewModel: NewDriverViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_new_driver, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(NewDriverViewModel::class.java)
        // TODO: Use the ViewModel
    }

}