package com.example.draganddroptest

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_MOVE
import android.view.MotionEvent.ACTION_UP
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.draganddroptest.databinding.FragmentDragAndDropBinding
import com.google.android.material.snackbar.Snackbar
import kotlin.math.absoluteValue
import kotlin.math.sign


class DragAndDropFragment : Fragment() {

    private val dragAndDropFragmentViewModel by viewModels<DragAndDropFragmentViewModel>()
    private lateinit var sharedPrefs: SharedPreferences

    private var _binding: FragmentDragAndDropBinding? = null

    private val binding get() = _binding ?: throw Throwable("binding not available")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDragAndDropBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        when (view.context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO -> binding.imageView.setColorFilter(Color.BLACK)
            Configuration.UI_MODE_NIGHT_YES -> binding.imageView.setColorFilter(Color.WHITE)
        }
        sharedPrefs = view.context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        var initialX = sharedPrefs.getFloat(PREFS_X_KEY, 0f)
        var initialY = sharedPrefs.getFloat(PREFS_Y_KEY, 0f)
        dragAndDropFragmentViewModel.updateXY(initialX, initialY)
        dragAndDropFragmentViewModel.initialPositionLiveData.observe(viewLifecycleOwner) { value ->
            initialX = value.x
            initialY = value.y
            updateImageXY(view, initialX, initialY)
        }
        view.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                updateImageXY(view, initialX, initialY)
            }
        })
        binding.imageView.setOnTouchListener(object: View.OnTouchListener {
            var downEventX = 0f
            var downEventY = 0f
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                val parentCenterX = view.width / 2
                val parentCenterY = view.height / 2
                when (event.action) {
                    ACTION_DOWN -> {
                        with(event) {
                            downEventX = rawX
                            downEventY = rawY
                        }
                    }
                    ACTION_MOVE -> {
                        val deltaX = downEventX - event.rawX
                        val deltaY = downEventY - event.rawY
                        val newX = parentCenterX * initialX - deltaX
                        val newY = parentCenterY * initialY - deltaY
                        v.translationX = if (parentCenterX - newX.absoluteValue > 0) newX else (parentCenterX) * newX.sign
                        v.translationY = if (parentCenterY - newY.absoluteValue > 0) newY else (parentCenterY) * newY.sign
                    }
                    ACTION_UP -> {
                        val fractionX = v.translationX / (parentCenterX)
                        val fractionY = v.translationY / (parentCenterY)
                        saveLastXY(fractionX, fractionY)
                        Snackbar
                            .make(view, getString(R.string.drop_message), Snackbar.LENGTH_SHORT)
                            .show()
                    }
                }
                return true
            }
        })

        view.setOnLongClickListener {
            saveLastXY(0f, 0f)
            true
        }
    }

    private fun updateImageXY(view: View, initialX: Float, initialY: Float) {
        with(binding.imageView) {
            translationX = (view.width / 2) * initialX
            translationY = (view.height / 2) * initialY
        }
    }

    private fun saveLastXY(fractionX: Float, fractionY: Float) {
        sharedPrefs.edit()
            .putFloat(PREFS_X_KEY, fractionX)
            .putFloat(PREFS_Y_KEY, fractionY)
            .apply()
        dragAndDropFragmentViewModel.updateXY(fractionX, fractionY)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val PREFS_NAME = "xy_prefs"
        const val PREFS_X_KEY = "x"
        const val PREFS_Y_KEY = "y"
    }
}