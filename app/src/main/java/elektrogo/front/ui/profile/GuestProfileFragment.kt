package elektrogo.front.ui.profile

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.squareup.picasso.Picasso
import elektrogo.front.R
import elektrogo.front.controller.FrontendController
import elektrogo.front.controller.session.SessionController
import elektrogo.front.databinding.ProfileFragmentBinding
import elektrogo.front.model.Achievement
import elektrogo.front.model.Block
import elektrogo.front.model.Driver
import elektrogo.front.ui.login.LoginActivity
import elektrogo.front.ui.valorarUsuari.ValorarUsuariDialog
import elektrogo.front.ui.vehicleList.VehicleListActivity
import kotlinx.coroutines.runBlocking

class GuestProfileFragment : Fragment() {

    companion object {
        fun newInstance() = GuestProfileFragment()
    }

    private var viewModel: ProfileViewModel = ProfileViewModel()
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    @SuppressLint("SetTextI18n", "CutPasteId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_guest_profile, container, false)

        val username = getArguments()?.getString("username")
        val un: TextView = view.findViewById(R.id.profile_username)
        un.text = username

        val userActual = SessionController.getUsername(requireActivity())

        if (username == null) Toast.makeText(context, "There has been a problem, try again later", Toast.LENGTH_LONG).show()

        val imageViewProfile : ImageView = view.findViewById(R.id.profile_image)
        val imagePath = username?.let { viewModel.getUsersProfilePhoto(it) }
        if (!imagePath.equals("null")  and !imagePath.equals("") ) Picasso.get().load(imagePath).into(imageViewProfile)
        else imageViewProfile.setImageResource(R.drawable.avatar)


        if (username?.let { viewModel.getDriver(it) } == true) {
            val imageVerificat: ImageView = view.findViewById(R.id.verificat)
            imageVerificat.setImageResource(R.drawable.verificat)
        }


        val blockedList : ArrayList<Block> = viewModel.getBlocks(userActual)
        val achivementText : TextView = view.findViewById(R.id.achievements)
        val trophy : LinearLayout = view.findViewById(R.id.trophy)
        val textBlock : TextView = view.findViewById(R.id.block)
        val youBlocktext : TextView = view.findViewById(R.id.youBlock)
        val rateButton : Button = view.findViewById(R.id.profile_guest_valorar)
        val reportButton : Button = view.findViewById(R.id.profile_guest_denuciar)
        val blockButton : Button = view.findViewById(R.id.profile_guest_bloquejar)


        if (blockedList != null) {
            var blocked = false
            for (block in blockedList) {
                if (block.userBlocking == username) {
                    blocked = true
                }
            }
            if (blocked) {
                achivementText.setVisibility(View.GONE)
                trophy.setVisibility(View.GONE)
                rateButton.setVisibility(View.GONE)
                reportButton.setVisibility(View.GONE)
                blockButton.setVisibility(View.GONE)
            }
            else {
                val a : Achievement? =
                    username?.let { viewModel.getAchievement("Traveler", it) }

                if (a != null) {
                    val trophyImg : ImageView = view.findViewById(R.id.trophyImg)
                    val trophyName : TextView = view.findViewById(R.id.trophyName)
                    val trophyPoints : TextView = view.findViewById(R.id.trophyPoints)

                    if (a.points < 10) {
                        trophyImg.setImageResource(R.drawable.no_prize_small)
                    }
                    else if (a.points in 10..20) {
                        trophyImg.setImageResource(R.drawable.bronze_small)
                    }
                    else if (a.points in 20..30) {
                        trophyImg.setImageResource(R.drawable.silver_small)
                    }
                    else if (a.points > 30) {
                        trophyImg.setImageResource(R.drawable.gold_small)
                    }

                    trophyName.text = a.achievement
                    trophyPoints.text = a.points.toString()
                    textBlock.setVisibility(View.GONE)
                }
            }
        }
        else Toast.makeText(context, "Hi ha hagut un error", Toast.LENGTH_LONG).show()

        val blockedListuser : ArrayList<Block>? = username?.let { viewModel.getBlocks(it) }

        if (blockedListuser != null) {
            var youBlocked = false
            for (block in blockedListuser) {
                if (block.userBlocking == userActual) {
                    youBlocked = true
                }
            }
            if (youBlocked) {
                blockButton.setVisibility(View.GONE)
            }
            else {
                youBlocktext.setVisibility(View.GONE)
            }
        }
        else Toast.makeText(context, "Hi ha hagut un error", Toast.LENGTH_LONG).show()

       blockButton.setOnClickListener {
           if (username != null) {
               viewModel.blockUser(userActual, username)
           }
       }

        val ratingPair = username?.let { viewModel.getRating(it) }
        if (ratingPair != null) {
            if (ratingPair.first != 200) {
                Toast.makeText(context, "Hi ha hagut un error, intenta-ho més tard", Toast.LENGTH_LONG).show()
            } else {
                val star1: ImageView = view.findViewById(R.id.estrella1)
                val star2: ImageView = view.findViewById(R.id.estrella2)
                val star3: ImageView = view.findViewById(R.id.estrella3)
                val star4: ImageView = view.findViewById(R.id.estrella4)
                val star5: ImageView = view.findViewById(R.id.estrella5)

                var rating = ratingPair?.second!!.ratingValue/2
                var decimalValue = rating - rating.toInt()
                var enterValue = rating.toInt()

                when (enterValue) {
                    0 -> {
                        if (decimalValue >= 0.25 && decimalValue < 0.75) {
                            star1.setImageResource(R.drawable.ic_starmigplena)
                        } else if (decimalValue >= 0.75) {
                            star1.setImageResource(R.drawable.ic_starplena)
                        } else {
                            star1.setImageResource(R.drawable.ic_starbuida)
                        }
                        star2.setImageResource(R.drawable.ic_starbuida)
                        star3.setImageResource(R.drawable.ic_starbuida)
                        star4.setImageResource(R.drawable.ic_starbuida)
                        star5.setImageResource(R.drawable.ic_starbuida)
                    }
                    1 -> {
                        star1.setImageResource(R.drawable.ic_starplena)
                        if (decimalValue >= 0.25 && decimalValue < 0.75) {
                            star2.setImageResource(R.drawable.ic_starmigplena)
                        } else if (decimalValue >= 0.75) {
                            star2.setImageResource(R.drawable.ic_starplena)
                        } else {
                            star2.setImageResource(R.drawable.ic_starbuida)
                        }
                        star3.setImageResource(R.drawable.ic_starbuida)
                        star4.setImageResource(R.drawable.ic_starbuida)
                        star5.setImageResource(R.drawable.ic_starbuida)
                    }
                    2 -> {
                        star1.setImageResource(R.drawable.ic_starplena)
                        star2.setImageResource(R.drawable.ic_starplena)
                        if (decimalValue >= 0.25 && decimalValue < 0.75) {
                            star3.setImageResource(R.drawable.ic_starmigplena)
                        } else if (decimalValue >= 0.75) {
                            star3.setImageResource(R.drawable.ic_starplena)
                        } else {
                            star3.setImageResource(R.drawable.ic_starbuida)
                        }
                        star4.setImageResource(R.drawable.ic_starbuida)
                        star5.setImageResource(R.drawable.ic_starbuida)
                    }
                    3 -> {
                        star1.setImageResource(R.drawable.ic_starplena)
                        star2.setImageResource(R.drawable.ic_starplena)
                        star3.setImageResource(R.drawable.ic_starplena)
                        if (decimalValue >= 0.25 && decimalValue < 0.75) {
                            star4.setImageResource(R.drawable.ic_starmigplena)
                        } else if (decimalValue >= 0.75) {
                            star4.setImageResource(R.drawable.ic_starplena)
                        } else {
                            star4.setImageResource(R.drawable.ic_starbuida)
                        }
                        star5.setImageResource(R.drawable.ic_starbuida)
                    }
                    4 -> {
                        star1.setImageResource(R.drawable.ic_starplena)
                        star2.setImageResource(R.drawable.ic_starplena)
                        star3.setImageResource(R.drawable.ic_starplena)
                        star4.setImageResource(R.drawable.ic_starplena)

                        if (decimalValue >= 0.25 && decimalValue < 0.75) {
                            star5.setImageResource(R.drawable.ic_starmigplena)
                        } else if (decimalValue >= 0.75) {
                            star5.setImageResource(R.drawable.ic_starplena)
                        } else {
                            star5.setImageResource(R.drawable.ic_starbuida)
                        }
                    }
                    5 -> {
                        star1.setImageResource(R.drawable.ic_starplena)
                        star2.setImageResource(R.drawable.ic_starplena)
                        star3.setImageResource(R.drawable.ic_starplena)
                        star4.setImageResource(R.drawable.ic_starplena)
                        star5.setImageResource(R.drawable.ic_starplena)
                    }
                }
            }
        }


        val btnValorar = view.findViewById<Button>(R.id.profile_guest_valorar)
        btnValorar.setOnClickListener {
            val valorarDialog = ValorarUsuariDialog()

            val bundle = Bundle()
            bundle.putString("guestUser", username) //passem l'usuari que es vol valorar al dialog

            valorarDialog.arguments = bundle
            valorarDialog.show(childFragmentManager, "confirmDialog")
        }

        return view
    }
}