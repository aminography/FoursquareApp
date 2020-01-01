package com.aminography.foursquareapp.presentation.ui.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

/**
 * A class contains base implementations for the [Fragment] which are common in the project.
 * @author aminography
 */
abstract class BaseFragment(@LayoutRes private val layoutResId: Int) : Fragment() {

    protected val activityContext: Context by lazy { activity!! }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutResId, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onInitViews(view, savedInstanceState)
    }

    /**
     * Called when the view hierarchy is created.
     */
    abstract fun onInitViews(rootView: View, savedInstanceState: Bundle?)

}
