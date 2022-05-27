package elektrogo.front.ui.vehicleList

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import elektrogo.front.R
import elektrogo.front.controller.session.SessionController
import elektrogo.front.model.Driver
import java.io.FileNotFoundException
import java.io.IOException

class NewDriverFragment : Fragment() {

    companion object {
        fun newInstance() = NewDriverFragment()
    }

    private lateinit var viewModel: NewDriverViewModel

    private val verifyingDriverFragment = VerifyingDriverFragment()

    private lateinit var frontImage : ImageView

    private lateinit var reverseImage : ImageView

    private lateinit var frontButton : Button

    private lateinit var reverseButton : Button

    private lateinit var sendButton: Button

    private val selectPhoto = 1

    private var imageUriFront: Uri? = null

    private var imageUriReverse: Uri? = null

    private var bitmapImageFront: Bitmap? = null

    private var bitmapImageReverse : Bitmap? = null

    private var request : String = "front"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_new_driver, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        frontImage = requireActivity().findViewById(R.id.frontImage)
        reverseImage = requireActivity().findViewById(R.id.reverseImage)
        frontButton = requireActivity().findViewById(R.id.frontalButton)
        reverseButton = requireActivity().findViewById(R.id.reverseButton)
        sendButton = requireActivity().findViewById(R.id.sendButton)

        frontButton.setOnClickListener{
            val selectImageIntent: Intent = Intent(Intent.ACTION_PICK)
            request = "front"
            selectImageIntent.setType("image/*")
            startActivityForResult(selectImageIntent, selectPhoto)
        }

        reverseButton.setOnClickListener{
            val selectImageIntent: Intent = Intent(Intent.ACTION_PICK)
            selectImageIntent.setType("image/*")
            request = "reverse"
            startActivityForResult(selectImageIntent, selectPhoto)
        }

        sendButton.setOnClickListener {
            if (imageUriFront != null && imageUriReverse != null){
                var driver = Driver(SessionController.getUsername(requireActivity()), "pendent")
                var statusCode = viewModel.sendDriverInfo(driver, SessionController.getUsername(requireActivity()))

                if (statusCode == 439) Toast.makeText(requireActivity(), resources.getString(R.string.DriverAlreadyExists), Toast.LENGTH_LONG).show()
                else if (statusCode == 500) Toast.makeText(requireActivity(), resources.getString(R.string.ServerError), Toast.LENGTH_LONG).show()
                else if (statusCode == 200){
                    viewModel.saveDriverFrontImage(SessionController.getUsername(requireActivity()), bitmapImageFront!!)
                    viewModel.saveDriverReverseImage(SessionController.getUsername(requireActivity()), bitmapImageReverse!!)
                    Toast.makeText(requireActivity(), resources.getString(R.string.DriverRequestRecived), Toast.LENGTH_LONG).show()
                    loadFragment(verifyingDriverFragment)
                }
            }
            else {
                Toast.makeText(requireActivity(), resources.getString(R.string.requiredImage), Toast.LENGTH_LONG).show()
            }
        }



    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(NewDriverViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == selectPhoto && resultCode == AppCompatActivity.RESULT_OK && data != null && data.getData() != null){
            if (request.equals("front")) imageUriFront = data.data!!
            else if (request.equals( "reverse")) imageUriReverse = data.getData()!!
            if (request.equals("front")){
                try{
                    bitmapImageFront = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), imageUriFront)
                    frontImage.setImageBitmap(bitmapImageFront)
                } catch(e: FileNotFoundException){
                    e.printStackTrace()
                } catch(e: IOException){
                    e.printStackTrace()
                }
            } else if (request.equals("reverse")) {
                try{
                    bitmapImageReverse = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, imageUriReverse)
                    reverseImage.setImageBitmap(bitmapImageReverse)
                } catch(e: FileNotFoundException){
                    e.printStackTrace()
                } catch(e: IOException){
                    e.printStackTrace()
                }

            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction: FragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_container, fragment, "VehicleListFragment")
        transaction.commit()
    }
}