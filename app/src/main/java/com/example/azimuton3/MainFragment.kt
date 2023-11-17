
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.azimuton3.R
import com.example.azimuton3.ui.MainActivity

class MainFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        val btnPlay: Button = view.findViewById(R.id.recyclerViewHistory)
        val btnPolicy: Button = view.findViewById(R.id.btnPolicy)
        val btnHistory: Button = view.findViewById(R.id.btnHistory)
        val btnExit: Button = view.findViewById(R.id.btnExit)


        btnPlay.setOnClickListener {
            (activity as MainActivity).showGameScreen()
        }

        btnPolicy.setOnClickListener {
            (activity as MainActivity).showWebView("https://www.google.com")
        }

        btnHistory.setOnClickListener {
            (activity as MainActivity).showHistoryScreen()
        }

        btnExit.setOnClickListener {
            activity?.finish()
        }

        return view
    }
}
