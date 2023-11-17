package com.example.azimuton3.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.azimuton3.R
import com.example.azimuton3.utils.GameResultDatabaseHelper

class HistoryFragment : Fragment() {
    private lateinit var gameResultDatabaseHelper: GameResultDatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_history, container, false)

        gameResultDatabaseHelper = GameResultDatabaseHelper(requireContext())

        // Получает результаты из базы данных
        val results = gameResultDatabaseHelper.getAllGameResults()

        // Отобразите результаты в вашем макете (например, в TextView)
        val historyTextView: TextView = view.findViewById(R.id.historyTextView)
        val stringBuilder = StringBuilder()

        for (result in results) {
            stringBuilder.append("Score: ${result.score}, Duration: ${result.duration}, BackgroundColor: ${result.backgroundColor}\n")
        }

        historyTextView.text = stringBuilder.toString()

        // Инициализация кнопки очистки истории
        setupClearHistoryButton(view)

        return view
    }

    private fun setupClearHistoryButton(view: View) {
        val clearHistoryButton: Button = view.findViewById(R.id.clearHistoryButton)
        clearHistoryButton.setOnClickListener {
            gameResultDatabaseHelper.clearGameResults()
            updateHistoryTextView()
        }
    }

    private fun updateHistoryTextView() {
        // Получает результаты из базы данных после очистки
        val updatedResults = gameResultDatabaseHelper.getAllGameResults()

        // Отображает обновленные результаты в макете
        val historyTextView: TextView = requireView().findViewById(R.id.historyTextView)
        val stringBuilder = StringBuilder()

        for (result in updatedResults) {
            //stringBuilder.append("Score: ${result.score}\n, Duration: ${result.duration}\n, BackgroundColor: ${result.backgroundColor}\n")
            stringBuilder.append(
                """
                Score: ${result.score}
                Duration: ${result.duration}
                BackgroundColor: ${result.backgroundColor}
                """
            )
        }

        historyTextView.text = stringBuilder.toString()
    }
}
