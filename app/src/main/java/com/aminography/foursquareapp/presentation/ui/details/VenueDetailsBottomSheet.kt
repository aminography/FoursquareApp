package com.aminography.foursquareapp.presentation.ui.details

import android.app.Dialog
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.aminography.foursquareapp.R
import com.aminography.foursquareapp.data.base.Status
import com.aminography.foursquareapp.presentation.viewmodel.VenueDetailsViewModel
import com.aminography.foursquareapp.core.tools.splitVenueName
import com.aminography.foursquareapp.domain.model.VenueDetailsModel
import com.aminography.foursquareapp.presentation.ui.base.BaseBottomSheetDialogFragment
import com.aminography.foursquareapp.presentation.ui.launch
import com.aminography.foursquareapp.presentation.ui.loadImage
import com.aminography.foursquareapp.presentation.ui.screenSize
import com.aminography.foursquareapp.presentation.ui.setRatingBarColors
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.fragment_venue_details.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.net.UnknownHostException
import java.util.*

/**
 * A bottom sheet fragment that presents venue details.
 *
 * @author aminography
 */
class VenueDetailsBottomSheet : BaseBottomSheetDialogFragment(R.layout.fragment_venue_details) {

    private val viewModel: VenueDetailsViewModel by viewModel()
    private lateinit var googleMap: GoogleMap
    private var toShowLocation: Pair<LatLng, String>? = null

    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        val parentView = rootView.parent as View
        val params = parentView.layoutParams as CoordinatorLayout.LayoutParams
        val behavior = params.behavior
        if (behavior is BottomSheetBehavior<*>) {
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            activityContext.screenSize().apply {
                behavior.peekHeight = y
                params.height = y
            }
        }
    }

    override fun onInitViews(rootView: View) {
        viewModel.venueDetails.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS, Status.EMPTY -> {
                    it.data?.let { data ->
                        bindData(data)
                        handleUiStates(it.status, it.error)
                    }
                }
                Status.ERROR -> {
                    handleUiStates(it.status, it.error)
                }
                Status.LOADING -> {
                }
            }
        })
        arguments?.getString("venueId")?.let { venueId ->
            viewModel.explore(venueId)
        }

        with(rootView) {
            closeButton.setOnClickListener { dismiss() }

            mapView.onCreate(null)
            mapView.getMapAsync {
                googleMap = it
                toShowLocation?.apply {
                    googleMap.addMarker(MarkerOptions().position(first).title(second))
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(first))
                }
            }

            emptyStateButton.setOnClickListener {
                emptyStateButton.isEnabled = false
                emptyStateLayout?.visibility = View.GONE
                progressBar.visibility = View.VISIBLE
                viewModel.retry()
            }
        }
    }

    private fun bindData(model: VenueDetailsModel) {
        with(rootView) {
            val venueName = splitVenueName(model.name).first
            nameTextView.text = venueName
            categoryNameTextView.text = model.category.name
            verifiedImageView.visibility = if (model.verified) View.VISIBLE else View.GONE

            model.price?.apply {
                val source = String.format(
                    Locale.getDefault(),
                    "%s (%s)",
                    currency.repeat(4),
                    message
                )
                val spannableString = SpannableString(source)
                val span = ForegroundColorSpan(
                    ContextCompat.getColor(
                        activityContext,
                        R.color.colorAccent
                    )
                )
                spannableString.setSpan(span, 0, tier, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
                priceTextView.text = spannableString
            }
            priceGroup.visibility = if (model.price == null) View.GONE else View.VISIBLE

            model.rating?.apply {
                ratingBar.rating = rating.toFloat() / 2
                ratingBar.setRatingBarColors()
                rateCountTextView.text = String.format(
                    Locale.getDefault(),
                    "(%d)",
                    ratingSignals
                )
            }
            ratingGroup.visibility = if (model.rating == null) View.GONE else View.VISIBLE

            model.contact?.apply {
                if (formattedPhone != null) {
                    phoneTextView.text = formattedPhone
                } else {
                    phoneTextView.visibility = View.GONE
                    phoneIconImageView.visibility = View.GONE
                }
                if (instagram != null) {
                    instagramTextView.text = instagram
                } else {
                    instagramTextView.visibility = View.GONE
                    instagramIconImageView.visibility = View.GONE
                }
                if (model.url != null) {
                    webTextView.text = model.url
                } else {
                    webTextView.visibility = View.GONE
                    webIconImageView.visibility = View.GONE
                }
            }
            if (phoneTextView.visibility == View.GONE &&
                instagramTextView.visibility == View.GONE &&
                webTextView.visibility == View.GONE
            ) {
                contactLabelTextView.visibility = View.GONE
            }

            model.location.apply {
                addressTextView.text = address
                addressTextView.visibility = if (address == null) View.GONE else View.VISIBLE

                if (::googleMap.isInitialized) {
                    val pin = LatLng(latitude, longitude)
                    googleMap.addMarker(MarkerOptions().position(pin).title(venueName))
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(pin))
                } else {
                    toShowLocation = Pair(LatLng(latitude, longitude), venueName)
                }
            }

            model.bestPhoto?.apply {
                bestPhotoImageView.loadImage(url, placeholderResId = R.drawable.default_placeholder)
                bestPhotoUserTextView.text =
                    String.format(Locale.getDefault(), "Taken at %s", createdAt)
            }
            bestPhotoGroup.visibility = if (model.bestPhoto == null) View.GONE else View.VISIBLE

            model.lastTip?.apply {
                tipUserNameTextView.text =
                    if (userLastName != null) "$userFirstName $userLastName"
                    else userFirstName

                tipDateTextView.text = createdAt
                tipUserPhotoImageView.loadImage(
                    userPhoto,
                    circleCrop = true,
                    crossFade = true,
                    placeholderResId = R.drawable.default_placeholder_circle
                )
                lastTipTextView.text = text
            }
            lastTipGroup.visibility = if (model.lastTip == null) View.GONE else View.VISIBLE
        }
    }

    private fun handleUiStates(status: Status, error: Throwable?) {
        with(rootView) {
            when (status) {
                Status.SUCCESS -> {
                    progressBar.visibility = View.GONE
                    coverView.visibility = View.GONE
                }
                Status.ERROR -> {
                    progressBar.visibility = View.GONE
                    emptyStateLayout.visibility = View.VISIBLE

                    if (error is UnknownHostException) {
                        emptyStateTextView.text = getString(R.string.no_internet_access)
                    } else {
                        emptyStateTextView.text = error?.message ?: "Unknown Error!"
                    }

                    launch(Dispatchers.Main) {
                        delay(500)
                        emptyStateButton.isEnabled = true
                    }
                }
                Status.LOADING -> {
                    progressBar.visibility = View.VISIBLE
                }
                Status.EMPTY -> {
                    progressBar.visibility = View.GONE
                }
            }
        }
    }

    override fun onResume() {
        with(rootView) {
            mapView.onResume()
        }
        super.onResume()
    }

    override fun onPause() {
        with(rootView) {
            mapView.onPause()
        }
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        with(rootView) {
            mapView.onDestroy()
        }
    }

    override fun onLowMemory() {
        super.onLowMemory()
        with(rootView) {
            mapView.onLowMemory()
        }
    }

    companion object {

        fun newInstance(venueId: String): VenueDetailsBottomSheet {
            return VenueDetailsBottomSheet().also {
                it.arguments = Bundle().apply {
                    putString("venueId", venueId)
                }
            }
        }
    }

}