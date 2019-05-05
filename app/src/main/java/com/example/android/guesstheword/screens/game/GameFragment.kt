package com.example.android.guesstheword.screens.game

import android.os.Bundle
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.example.android.guesstheword.R
import com.example.android.guesstheword.databinding.GameFragmentBinding

class GameFragment : Fragment() {

    private lateinit var binding: GameFragmentBinding
    private lateinit var gameViewModel: GameViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.game_fragment, container, false)
        gameViewModel = ViewModelProviders.of(this).get(GameViewModel::class.java)

        initGameButton()
        updateScoreText()
        updateWordText()
        updateCurrentTime()
        gameFinish()

        return binding.root
    }

    private fun initGameButton() {
        binding.correctButton.setOnClickListener {
            gameViewModel.onCorrect()
            updateWordText()
        }

        binding.skipButton.setOnClickListener {
            gameViewModel.onSkip()
            updateWordText()
        }
    }

    private fun updateCurrentTime() {
        gameViewModel.currentTime.observe(this, Observer { newTime ->
            binding.timerText.text = DateUtils.formatElapsedTime(newTime)
        })
    }

    private fun updateScoreText() {
        gameViewModel.score.observe(this, Observer { newScore ->
            binding.scoreText.text = newScore.toString()
        })
    }

    private fun updateWordText() {
        binding.wordText.text = gameViewModel.word.value
    }

    private fun gameFinish() {
        gameViewModel.eventGameFinish.observe(this, Observer { hasFinished ->
            if (hasFinished) {
                gameFinished()
                gameViewModel.onGameFinishComplete()
            }
        })
    }

    private fun gameFinished() {
        val currentScore = gameViewModel.score.value ?: 0
        val action = GameFragmentDirections.actionGameToScore(currentScore)
        findNavController(this).navigate(action)
        gameViewModel.onGameFinishComplete()
    }
}
