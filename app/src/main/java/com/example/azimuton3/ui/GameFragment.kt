package com.example.azimuton3.ui

import android.animation.ObjectAnimator
import android.graphics.Color
import android.graphics.Path
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.SystemClock
import android.view.Choreographer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.azimuton3.R
import com.example.azimuton3.model.GameViewModel
import com.example.azimuton3.utils.BackgroundColorManager
import com.example.azimuton3.utils.GameResultDatabaseHelper
import com.example.azimuton3.utils.MusicManager
import kotlin.random.Random

class GameFragment : Fragment() {

    private lateinit var whiteBall: ImageView
    private lateinit var grayRing: ImageView
    private lateinit var scoreView: TextView
    private lateinit var fpsView: TextView
    private lateinit var darkBlocksContainer: LinearLayout
    private lateinit var backgroundColorManager: BackgroundColorManager
    private lateinit var gameResultDatabaseHelper: GameResultDatabaseHelper
    private val gameViewModel by viewModels<GameViewModel>()
    //private lateinit var musicManager: MusicManager

    private var score = 0
    private var frameCount = 0
    private var lastFrameTimeNanos: Long = 0
    private var gameStartTime: Long = 0
    private var isMovingForward = true


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_game, container, false)

        initializeViews(view)
        setupAnimation()
        startFPSCounter()

        return view
    }

    private fun initializeViews(view: View) {
        grayRing = view.findViewById(R.id.grayCircle)
        whiteBall = view.findViewById(R.id.whiteBall)
        scoreView = view.findViewById(R.id.scoreView)
        darkBlocksContainer = view.findViewById(R.id.darkBlocksContainer)
        //musicManager = MusicManager.getInstance(requireContext(), R.raw.background_music)
        fpsView = view.findViewById(R.id.fpsView)
        gameStartTime = SystemClock.elapsedRealtime()

        backgroundColorManager = BackgroundColorManager(requireContext())
        gameResultDatabaseHelper = GameResultDatabaseHelper(requireContext())

        // Восстановит цвет фона при входе в игру
        val defaultColor = Color.WHITE
        val backgroundColor = backgroundColorManager.restoreBackgroundColor(defaultColor)
        view.setBackgroundColor(backgroundColor)

        // Определение количества блоков
        repeat(5) {
            addBlock()
        }
    }

    private fun setupAnimation() {
        val path = createCircularPath()

        whiteBall.layoutParams.width = 50
        whiteBall.layoutParams.height = 50
        whiteBall.requestLayout()

        val animator = ObjectAnimator.ofFloat(whiteBall, "translationX", "translationY", path)
        animator.duration = 3050
        animator.repeatCount = ObjectAnimator.INFINITE
        animator.interpolator = LinearInterpolator()

        grayRing.setOnClickListener {
            if (isMovingForward) {
                animator.reverse()
                //mediaPlayer.pause()
            } else {
                animator.resume()
                //mediaPlayer.start()
            }
            !isMovingForward
        }
        animator.addUpdateListener {
            for (i in 0 until darkBlocksContainer.childCount) {
                val newBlock = darkBlocksContainer.getChildAt(i) as ImageView
                if (isCollision(whiteBall, newBlock)) {
                    handleCollision(newBlock)
                }
            }
        }

        animator.start()
        //musicManager.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        saveGameResult()
        saveBackgroundColor()
        MusicManager.stop()
        MusicManager.changeMusic(requireContext(), R.raw.main_menu_music)
    }

    private fun createCircularPath(): Path {
        val path = Path()

        val ringRadius = 55f
        val ringCenterX = grayRing.width / 2f
        val ringCenterY = grayRing.height / 2f

        path.addCircle(ringCenterX, ringCenterY, ringRadius * 2, Path.Direction.CW)

        return path
    }

    private fun isCollision(view1: View, view2: View): Boolean {
        return view1.x < view2.x + view2.width &&
                view1.x + view1.width > view2.x &&
                view1.y < view2.y + view2.height &&
                view1.y + view1.height > view2.y
    }

    private fun addBlock() {
        val newBlock = ImageView(requireContext())
        newBlock.x = -500f
        newBlock.y = 0f
        newBlock.layoutParams = ViewGroup.LayoutParams(
            Random.nextInt(20, 50), // Пример: размер от 20 до 50
            Random.nextInt(20, 50)
        )

        // Рандомно выбираем цвет блока
        newBlock.setBackgroundColor(getRandomColor())

        darkBlocksContainer.addView(newBlock)

        val blockAnimator = ObjectAnimator.ofFloat(newBlock, "x", -500f, 1000f)
        blockAnimator.duration = (3000 + Random.nextInt(3000)).toLong()
        blockAnimator.repeatCount = ObjectAnimator.INFINITE
        blockAnimator.interpolator = LinearInterpolator()

        blockAnimator.addUpdateListener { animator ->
            if (isCollision(whiteBall, newBlock)) {
                handleCollision(newBlock)
            }
        }

        blockAnimator.start()
    }

    private fun handleCollision(block: ImageView) {
        val background = block.background
        if (background is ColorDrawable && background.color == Color.WHITE) {
            score++
        } else {
            score--
        }
        scoreView.text = "Score: $score"
    }

    private fun getRandomColor(): Int {
        return if (Random.nextBoolean()) {
            Color.BLACK
        } else {
            Color.WHITE
        }
    }

    // Вызывает saveBackgroundColor() при необходимости сохранения цвета
    private fun saveBackgroundColor() {
        backgroundColorManager.saveBackgroundColor()
    }

    private fun startFPSCounter() {
        Choreographer.getInstance().postFrameCallback(object : Choreographer.FrameCallback {
            override fun doFrame(frameTimeNanos: Long) {
                gameViewModel.measureFrameTime(lastFrameTimeNanos) { fps ->
                    fpsView.text = "FPS: $fps"
                }
                lastFrameTimeNanos = frameTimeNanos
                Choreographer.getInstance().postFrameCallback(this)
            }
        })
    }

    private fun saveGameResult() {
        val duration = (SystemClock.elapsedRealtime() - gameStartTime) / 1000
        val backgroundColor = backgroundColorManager.restoreBackgroundColor(Color.WHITE)
        gameResultDatabaseHelper.insertGameResult(score, duration, backgroundColor)
    }

}
