package com.aminography.foursquareapp.presentation.ui.base

import android.os.Bundle
import android.view.MenuItem
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.aminography.foursquareapp.R

/**
 * A class contains base implementations for the [android.app.Activity] which are common in the project.
 *
 * @author aminography
 */
abstract class BaseActivity(
    @LayoutRes private val layoutResId: Int,
    private val backButtonEnabled: Boolean = false
) : AppCompatActivity() {

    protected var rootLayout: ViewGroup? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutResId)
        setSupportActionBar(findViewById(R.id.toolbar))
        checkBackButton()
    }

    override fun setContentView(@LayoutRes layoutResId: Int) {
        super.setContentView(layoutResId)
        rootLayout = findViewById(R.id.rootLayout)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> {
            onBackPressed()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun checkBackButton() {
        if (backButtonEnabled) {
            supportActionBar?.apply {
                setDisplayHomeAsUpEnabled(true)
                setDisplayShowHomeEnabled(true)
                setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp)
            }
        }
    }

}