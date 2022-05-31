package elektrogo.front.ui.chatConversation

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputEditText
import elektrogo.front.R
import elektrogo.front.controller.session.SessionController
import elektrogo.front.model.Block
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

                setTitle(getString(R.string.addMemberDialogTitle))
                val fragmentAdd = layoutInflater.inflate(R.layout.add_member_fragment, null)
                setView(fragmentAdd)
                val listView: ListView = fragmentAdd.findViewById(R.id.addMemberTripsList ) as ListView

                var result : Pair <Int, ArrayList<CarPooling>> = viewModel.getUserCreatedTrips(SessionController.getUsername(requireContext()))
                if (result.first != 200) {
                    Toast.makeText(context, getString(R.string.errorFilterTrips), Toast.LENGTH_LONG).show()
                }
                else {
                    resultList = result.second
                    listView.adapter = ListAdapterTrips(requireActivity(), resultList)
                }

                val username = arguments?.getString("member")!!

                listView.setOnItemClickListener { parent, view, position, id ->
                    val blockedList : ArrayList<Block> = viewModel.getBlocks(username)
                    var blocked = false
                    for (block in blockedList) {
                        if (block.userBlocking == SessionController.getUsername(requireContext())) {
                            blocked = true
                        }
                    }

                    val blockedListuser : ArrayList<Block> =  viewModel.getBlocks(SessionController.getUsername(requireContext()))
                    var youAreBlocked = false
                    for (block in blockedListuser) {
                        if (block.userBlocking == username) {
                            youAreBlocked = true
                        }
                    }
                    if (youAreBlocked || blocked) {
                        Toast.makeText(requireContext(),getString(R.string.cantAddBlock), Toast.LENGTH_LONG).show()
                    }
                    else {

                        val status = viewModel.addMemberToATrip(username, resultList[position].id)
                        if (status != 200) {
                            if (status == 448) {
                                Toast.makeText(
                                    context,
                                    getString(R.string.memberAlreadyAdded),
                                    Toast.LENGTH_LONG
                                ).show()
                            } else if (status == 450) Toast.makeText(
                                context,
                                getString(R.string.fullTripError),
                                Toast.LENGTH_LONG
                            ).show()
                            else {
                                Toast.makeText(
                                    context,
                                    getString(R.string.ServerError),
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                            dismiss()
                        } else {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.memberAdded),
                                Toast.LENGTH_SHORT
                            ).show()
                            dismiss()
                        }
                    }
                }
            }
            // Crea el dialeg
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")

    }













}