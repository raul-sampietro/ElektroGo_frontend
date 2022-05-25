package elektrogo.front.ui.chatConversation

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputEditText
import elektrogo.front.R
import elektrogo.front.controller.session.SessionController
import elektrogo.front.model.CarPooling
import elektrogo.front.ui.carPooling.FilterTripsViewModel
import elektrogo.front.ui.carPooling.ListAdapterTrips
import elektrogo.front.ui.carPooling.TripDetails

class AddMemberDialog : DialogFragment() {

    private  var viewModel = AddMemberDialogViewModel()
    lateinit var resultList : ArrayList<CarPooling>


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = androidx.appcompat.app.AlertDialog.Builder(it)

            builder.apply {
                Log.i("add", "Estoy en el dialog")

                setTitle("Add member to a trip")
                val fragmentAdd = layoutInflater.inflate(R.layout.add_member_fragment, null)
                setView(fragmentAdd)
                val listView: ListView = fragmentAdd.findViewById(R.id.addMemberTripsList ) as ListView
                val array = ArrayList<CarPooling>()
                val carpooling1 : CarPooling = CarPooling(1, "2022-05-25", "11:05:00", 4, 1, "none", "none2", "1234MAV", "Canet de Mar", "Vic", "MarinaA", "2022-05-24", 4.2, 1.4,2.3,4.4)
                array.add(carpooling1)
                array.add(carpooling1)
                array.add(carpooling1)
                array.add(carpooling1)
                array.add(carpooling1)
               // var result : Pair <Int, ArrayList<CarPooling>> = viewModel.getUserCreatedTrips(SessionController.getUsername(requireContext()))
              //  if (result.first != 200) {
              //      Toast.makeText(context, getString(R.string.ServerError), Toast.LENGTH_LONG).show()
              //  }
             //   else {
                   // resultList = result.second
                    resultList=array
                    listView.adapter = ListAdapterTrips(requireActivity(), resultList)
                //}

                val username = arguments?.getString("member")!!

                listView.setOnItemClickListener({ parent, view, position, id ->
                    viewModel.addMemberToATrip(username, resultList[position])
                })
            }
            // Crea el dialeg
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")

    }













}