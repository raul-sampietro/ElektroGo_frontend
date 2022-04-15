/**
 * @file ChatFragment.kt
 * @author Adria Abad
 * @date 15/04/2022
 * @brief Implementacio de la classe ChatFragment
 */
package elektrogo.front.ui.chatList


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import elektrogo.front.R

/**
 * @brief La clase Chat representa la GUI de la pantalla on l'usuari veura el sistema de xat entre usuaris.
 */

class ChatFragment() : Fragment() {


    /**
     * @brief Metode que s'executa al crear el fragment.
     * @post Es crea la vista per al fragment.
     * @return Retorna el layout creat.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }
}
