package com.example.draganddroptest.presentation

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
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
import com.example.draganddroptest.App
import com.example.draganddroptest.R
import com.example.draganddroptest.databinding.FragmentDragAndDropBinding
import com.google.android.material.snackbar.Snackbar
import kotlin.math.absoluteValue
import kotlin.math.sign

class DragAndDropFragment : Fragment() {

    private var _binding: FragmentDragAndDropBinding? = null
    private val binding get() = _binding ?: throw Throwable("binding not available")

    private val viewModel by viewModels<DragAndDropViewModel> {
        viewModelFactory { DragAndDropViewModel(App.appModule.xyRepository) }
    }

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
        var initialX = 0f
        var initialY = 0f
        when (view.context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO -> binding.imageView.setColorFilter(Color.BLACK)
            Configuration.UI_MODE_NIGHT_YES -> binding.imageView.setColorFilter(Color.WHITE)
        }

        viewModel.positionLiveData.observe(viewLifecycleOwner) { value ->
            initialX = value.x
            initialY = value.y
            updateXY(initialX, initialY)
        }

        view.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                updateXY(initialX, initialY)
            }
        })

        binding.imageView.setOnTouchListener(object: View.OnTouchListener {
            var downEventX = 0f
            var downEventY = 0f
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                val parentCenterX = binding.root.width / 2
                val parentCenterY = binding.root.height / 2
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
                        viewModel.updateXY(v.translationX / parentCenterX, v.translationY / parentCenterY)
                        Snackbar.make(v, getString(R.string.drop_message), Snackbar.LENGTH_SHORT).show()
                    }
                }
                return true
            }
        })

        view.setOnLongClickListener {
            viewModel.updateXY(0f, 0f)
            true
        }
    }

    private fun updateXY(x: Float, y: Float) {
        with(binding.imageView) {
            translationX = (binding.root.width / 2) * x
            translationY = (binding.root.height / 2) * y
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}