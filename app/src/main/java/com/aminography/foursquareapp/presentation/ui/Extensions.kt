package com.aminography.foursquareapp.presentation.ui

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Point
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.os.Build
import android.view.Display
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.RatingBar
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.Size
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.ViewPropertyAnimatorCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.aminography.foursquareapp.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Extension functions
 *
 * @author aminography
 */

fun LatLng.concat() = "${latitude},${longitude}"

fun LatLng.distanceTo(other: LatLng): Double {
    val earthRadiusKm = 6371
    fun degreesToRadians(degrees: Double): Double {
        return degrees * Math.PI / 180.0
    }

    val dLat = degreesToRadians(other.latitude - latitude)
    val dLon = degreesToRadians(other.longitude - longitude)

    val lat1 = degreesToRadians(latitude)
    val lat2 = degreesToRadians(other.latitude)

    val a = sin(dLat / 2) * sin(dLat / 2) +
            sin(dLon / 2) * sin(dLon / 2) * cos(lat1) * cos(lat2)
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))
    return earthRadiusKm * c
}

fun Fragment.launch(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit
): Job {
    return lifecycleScope.launch(context, start, block)
}

fun Context.hasPermissions(
    @Size(min = 1) vararg permissionList: String
): Boolean {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
        return true
    }
    for (permission in permissionList) {
        if (ContextCompat.checkSelfPermission(this, permission) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            return false
        }
    }
    return true
}

fun RatingBar.setRatingBarColors(
    @ColorRes fillColor: Int = R.color.ratingBarFillColor,
    @ColorRes emptyColor: Int = R.color.ratingBarEmptyColor
) {
    val stars = progressDrawable as LayerDrawable
    stars.getDrawable(2).setColorFilter(
        ContextCompat.getColor(context, fillColor)
    ) // for filled stars
    stars.getDrawable(1).setColorFilter(
        ContextCompat.getColor(context, fillColor)
    ) // for half filled stars
    stars.getDrawable(0).setColorFilter(
        ContextCompat.getColor(context, emptyColor)
    )  // for empty stars
}

private fun Drawable.setColorFilter(color: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        colorFilter = BlendModeColorFilter(color, BlendMode.SRC_ATOP)
    } else {
        @Suppress("DEPRECATION")
        setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
    }
}

fun View.animateCompat(): ViewPropertyAnimatorCompat {
    return ViewCompat.animate(this)
}

fun Context.screenSize(): Point {
    val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val display: Display? = windowManager.defaultDisplay
    val point = Point()
    display?.getSize(point)
    return point
}

private val factory = DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()

fun ImageView.loadImage(
    imageUrl: String?,
    circleCrop: Boolean = false,
    crossFade: Boolean = false,
    @DrawableRes placeholderResId: Int? = R.drawable.default_placeholder,
    listener: RequestListener<Drawable>? = null
) {
    val requestBuilder = Glide.with(context)
        .load(imageUrl)

    placeholderResId?.let { requestBuilder.apply(RequestOptions.placeholderOf(it)) }
    listener?.let { requestBuilder.listener(it) }
    if (listener != null) requestBuilder.listener(listener)
    if (crossFade) requestBuilder.transition(
        DrawableTransitionOptions.withCrossFade(
            factory
        )
    )
    if (circleCrop) requestBuilder.apply(RequestOptions.circleCropTransform())
    requestBuilder.into(this)
}
