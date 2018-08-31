package com.stratagile.qlink.shadowsocks.utils

import android.content.BroadcastReceiver
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.res.Resources
import android.graphics.BitmapFactory
//import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.support.annotation.AttrRes
import android.support.annotation.NonNull
import android.support.annotation.Nullable
import android.util.TypedValue
import androidx.core.content.systemService
//import androidx.annotation.AttrRes
//import com.crashlytics.android.Crashlytics
import com.github.shadowsocks.JniHelper
import com.stratagile.qlink.shadowsocks.utils.asIterable
import java.net.InetAddress
import java.net.URLConnection

fun String.isNumericAddress() = JniHelper.parseNumericAddress(this) != null
fun String.parseNumericAddress(): InetAddress? {
    val addr = JniHelper.parseNumericAddress(this)
    return if (addr == null) null else InetAddress.getByAddress(this, addr)
}

fun parsePort(str: String?, default: Int, min: Int = 1025): Int {
    val value = str?.toIntOrNull() ?: default
    return if (value < min || value > 65535) default else value
}

fun broadcastReceiver(callback: (Context, Intent) -> Unit): BroadcastReceiver = object : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) = callback(context, intent)
}

/**
 * Wrapper for kotlin.concurrent.thread that tracks uncaught exceptions.
 */
fun thread(name: String? = null, start: Boolean = true, isDaemon: Boolean = false,
           contextClassLoader: ClassLoader? = null, priority: Int = -1, block: () -> Unit): Thread {
    val thread = kotlin.concurrent.thread(false, isDaemon, contextClassLoader, name, priority, block)
    thread.setUncaughtExceptionHandler { _, t -> printLog(t) }
    if (start) thread.start()
    return thread
}

val URLConnection.responseLength: Long
    get() = if (Build.VERSION.SDK_INT >= 24) contentLengthLong else contentLength.toLong()

fun ContentResolver.openBitmap(uri: Uri) =
    BitmapFactory.decodeStream(openInputStream(uri))

val PackageInfo.signaturesCompat get() =
    signatures

/**
 * Based on: https://stackoverflow.com/a/26348729/2245107
 */
fun Resources.Theme.resolveResourceId(@AttrRes resId: Int): Int {
    val typedValue = TypedValue()
    if (!resolveAttribute(resId, typedValue, true)) throw Resources.NotFoundException()
    return typedValue.resourceId
}

val Intent.datas get() = listOfNotNull(data) + (clipData?.asIterable()?.map { it.uri }?.filterNotNull() ?: emptyList())

fun printLog(t: Throwable) {
//    Crashlytics.logException(t)
    t.printStackTrace()
}

inline fun <reified T> Context.getSystemService(): T? =
        ContextCompat.getSystemService(this, T::class.java)

@Deprecated(
        "Use getSystemService",
        ReplaceWith("this.getSystemService<T>()"),
        DeprecationLevel.ERROR
)
inline fun <reified T> Context.systemService(): T? =
        ContextCompat.getSystemService(this, T::class.java)
