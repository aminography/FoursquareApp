package com.aminography.foursquareapp.presentation.ui.base

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 * A class contains base implementations for the [BottomSheetDialogFragment] which are common in the project.
 *
 * @author aminography
 */
abstract class BaseBottomSheetDialogFragment(@LayoutRes private val layoutResId: Int) :
    BottomSheetDialogFragment() {

    protected val activityContext: Context by lazy { activity!!.applicationContext }
    protected lateinit var rootView: View

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        rootView = View.inflate(context, layoutResId, null)
        dialog.setContentView(rootView)
        onInitViews(rootView)
        val parentView = rootView.parent as View
        parentView.setBackgroundColor(
            ContextCompat.getColor(activityContext, android.R.color.transparent)
        )
    }

    /**
     * Called when the view hierarchy is created.
     */
    abstract fun onInitViews(rootView: View)

    open fun show(manager: FragmentManager?) {
        if (!isShowing) manager?.let {
            show(manager, tag)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        isShowing = true
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        isShowing = false
    }

    companion object {
        // to avoid opening multiple bottom-sheets
        private var isShowing = false
    }

}