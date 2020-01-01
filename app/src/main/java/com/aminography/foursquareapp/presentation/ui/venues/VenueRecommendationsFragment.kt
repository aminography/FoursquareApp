package com.aminography.foursquareapp.presentation.ui.venues

import android.Manifest
import android.os.Bundle
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aminography.foursquareapp.R
import com.aminography.foursquareapp.data.base.Status
import com.aminography.foursquareapp.core.logic.location.LocationProvider
import com.aminography.foursquareapp.core.logic.location.LocationResponse
import com.aminography.foursquareapp.presentation.viewmodel.VenueRecommendationsViewModel
import com.aminography.foursquareapp.presentation.ui.base.BaseAdapter
import com.aminography.foursquareapp.presentation.ui.base.BaseFragment
import com.aminography.foursquareapp.presentation.ui.base.OnListItemClickListener
import com.aminography.foursquareapp.presentation.ui.details.VenueDetailsBottomSheet
import com.aminography.foursquareapp.presentation.ui.animateCompat
import com.aminography.foursquareapp.presentation.ui.hasPermissions
import com.aminography.foursquareapp.presentation.ui.launch
import com.aminography.foursquareapp.presentation.ui.venues.adapter.VenueListAdapter
import com.aminography.foursquareapp.presentation.ui.venues.dataholder.VenueItemDataHolder
import kotlinx.android.synthetic.main.fragment_venue_recommendations.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import org.jetbrains.anko.support.v4.dip
import org.jetbrains.anko.support.v4.longToast
import org.jetbrains.anko.support.v4.toast
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.net.UnknownHostException
import kotlin.system.exitProcess

/**
 * A fragment that presents venue recommendations.
 *
 * @author aminography
 */
class VenueRecommendationsFragment : BaseFragment(R.layout.fragment_venue_recommendations),
    OnListItemClickListener {

    private val locationProvider: LocationProvider by inject()
    private val viewModel: VenueRecommendationsViewModel by viewModel()

    private val adapter: VenueListAdapter by lazy {
        VenueListAdapter().also {
            it.setOnListItemClickListener(this)
        }
    }

    override fun onInitViews(rootView: View, savedInstanceState: Bundle?) {
        recyclerView.layoutManager = LinearLayoutManager(activityContext)
        recyclerView.adapter = adapter
        recyclerView.addOnScrollListener(OnScrollListener())

        viewModel.venues.observe(this, Observer {
            val list = it.data ?: arrayListOf()
            handleUiStates(it.status, it.error, list.isEmpty())
            when (it.status) {
                Status.SUCCESS, Status.EMPTY -> {
                    adapter.submitList(list)
                }
                else -> {
                }
            }
        })

        emptyStateButton.setOnClickListener {
            emptyStateButton.isEnabled = false
            emptyStateLayout?.visibility = View.GONE
            viewModel.retry()
        }

        checkPermission()
    }

    private fun checkPermission() {
        if (activityContext.hasPermissions(*LOCATION_PERMISSIONS)) {
            initLocationProvider()
        } else {
            AlertDialog.Builder(activityContext, R.style.AppCompatAlertDialogStyle).apply {
                setTitle(R.string.location_permission)
                setMessage(R.string.location_permission_descriptions)
                setNegativeButton(R.string.close_app) { _, _ -> exitProcess(1) }
                setPositiveButton(R.string.ok) { _, _ ->
                    requestPermissions(
                        LOCATION_PERMISSIONS,
                        REQUEST_CODE_LOCATION_PERMISSION
                    )
                }
            }.show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE_LOCATION_PERMISSION -> {
                checkPermission()
            }
        }
    }

    private fun initLocationProvider() {
        locationProvider.liveData.observe(this, Observer {
            when (it) {
                is LocationResponse.ErrorResponse -> {
                    longToast(it.errorMessage)
                }
                is LocationResponse.SuccessResponse -> {
                    viewModel.explore(it.location)
                }
            }
        })
    }

    private fun handleUiStates(status: Status, error: Throwable?, isResultEmpty: Boolean) {
        when (status) {
            Status.SUCCESS, Status.EMPTY -> {
                hideProgress {
                    if (isResultEmpty) {
                        showEmptyState(status, error)
                    }
                }
                if (!isResultEmpty) {
                    loadMoreLayoutVisibility(false)
                }
            }
            Status.ERROR -> {
                if (isResultEmpty) {
                    hideProgress { }
                    showEmptyState(status, error)
                } else {
                    loadMoreLayoutVisibility(false)
                    if (error is UnknownHostException) {
                        toast(R.string.no_internet_access)
                    } else {
                        toast(error?.message ?: "Unknown Error!")
                    }
                }
            }
            Status.LOADING -> {
                if (isResultEmpty) {
                    hideEmptyState()
                    showProgress()
                } else {
                    loadMoreLayoutVisibility(true)
                }
            }
        }
    }

    private fun showProgress() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgress(endAction: () -> Unit) {
        progressBar.visibility = View.GONE
        endAction()
    }

    private fun showEmptyState(status: Status, error: Throwable?) {
        fun makeVisible() {
            emptyStateLayout.visibility = View.VISIBLE

            launch(Dispatchers.Main) {
                delay(500)
                emptyStateButton.isEnabled = true
            }
        }

        when (status) {
            Status.SUCCESS/*, Status.EMPTY*/ -> {
                emptyStateImageView.setImageResource(R.drawable.ic_magnifier_black_24dp)
                emptyStateTextView.text = getString(R.string.no_results_found)
                makeVisible()
            }
            Status.ERROR -> {
                emptyStateImageView.setImageResource(R.drawable.ic_sad_cloud_black_24dp)
                if (error is UnknownHostException) {
                    emptyStateTextView.text = getString(R.string.no_internet_access)
                } else {
                    emptyStateTextView.text = error?.message ?: "Unknown Error!"
                }
                makeVisible()
            }
            else -> {
            }
        }
    }

    private fun hideEmptyState() {
        emptyStateLayout.visibility = View.GONE
    }

    private fun loadMoreLayoutVisibility(visible: Boolean) {
        loadMoreLayout.animateCompat().apply {
            duration = 200
            startDelay = if (visible) 0 else 0
            interpolator = DecelerateInterpolator()
            translationY(if (visible) 0f else dip(32).toFloat())
            start()
        }
    }

    override fun onItemClicked(dataHolder: BaseAdapter.BaseDataHolder) {
        if (dataHolder is VenueItemDataHolder) {
            VenueDetailsBottomSheet.newInstance(dataHolder.venueId).show(parentFragmentManager)
        }
    }

    private inner class OnScrollListener : RecyclerView.OnScrollListener() {

        override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(view, dx, dy)
            if (dy > 0) { // scroll down
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastPosition = layoutManager.findLastVisibleItemPosition()
                if (lastPosition == adapter.itemCount - 4) {
                    viewModel.loadMore()
                }
            }
        }
    }

    companion object {

        const val REQUEST_CODE_LOCATION_PERMISSION = 17
        val LOCATION_PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

}
