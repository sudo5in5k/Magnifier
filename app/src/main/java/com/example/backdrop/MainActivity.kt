package com.example.backdrop

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.Magnifier
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.max
import kotlin.math.min

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val behavior = BottomSheetBehavior.from(behavior)
        behavior.state = BottomSheetBehavior.STATE_HIDDEN
        behavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(p0: View, p1: Float) {
                return
            }

            override fun onStateChanged(p0: View, state: Int) {
                when (state) {
                    BottomSheetBehavior.STATE_DRAGGING -> Unit
                    BottomSheetBehavior.STATE_EXPANDED -> Unit
                    BottomSheetBehavior.STATE_SETTLING -> Unit
                    BottomSheetBehavior.STATE_HIDDEN -> Unit
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> Unit
                    BottomSheetBehavior.STATE_COLLAPSED -> Unit
                }
            }
        })

        val magnifier = Magnifier.Builder(magnifier_text).build()
        magnifier_text.setOnTouchListener { view, event ->
            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                    val viewPosition = IntArray(2)
                    view.getLocationOnScreen(viewPosition)
                    magnifier.show(event.rawX - viewPosition[0], event.rawY - viewPosition[1])
                }
                MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                    magnifier.dismiss()
                }
            }
            false
        }

        magnifier_text.customSelectionActionModeCallback =
            object : android.view.ActionMode.Callback2() {
                override fun onActionItemClicked(
                    mode: android.view.ActionMode?,
                    item: MenuItem?
                ): Boolean {
                    when (item?.itemId) {
                        R.id.magnifier_text -> {
                            behavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
                            behavior_text.text = fetchSelectSequence(magnifier_text)
                        }
                        else -> Unit
                    }
                    return false
                }

                override fun onCreateActionMode(
                    p0: android.view.ActionMode?,
                    menu: Menu?
                ): Boolean {
                    menu?.removeItem(android.R.id.cut)
                    menu?.removeItem(android.R.id.copy)
                    menu?.removeItem(android.R.id.paste)
                    menu?.removeItem(android.R.id.shareText)
                    menu?.add(Menu.NONE, R.id.magnifier_text, Menu.NONE, "Search")
                    return true
                }

                override fun onPrepareActionMode(p0: android.view.ActionMode?, p1: Menu?): Boolean {
                    return false
                }

                override fun onDestroyActionMode(p0: android.view.ActionMode?) {
                    return
                }
            }
    }

    private fun fetchSelectSequence(tv: TextView): CharSequence {
        val text = tv.text
        var min = 0
        var max = text.length
        if (tv.isFocused) {
            val start = tv.selectionStart
            val end = tv.selectionEnd
            min = max(0, min(start, end))
            max = max(0, max(start, end))
        }
        return text.subSequence(min, max)
    }
}
