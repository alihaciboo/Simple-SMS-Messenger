package com.simplemobiletools.smsmessenger.receivers

import android.content.Context
import android.net.Uri
import com.bumptech.glide.Glide
import com.simplemobiletools.commons.helpers.ensureBackgroundThread
import com.simplemobiletools.smsmessenger.R
import com.simplemobiletools.smsmessenger.extensions.getLatestMMS
import com.simplemobiletools.smsmessenger.extensions.showReceivedMessageNotification

// more info at https://github.com/klinker41/android-smsmms
class MmsReceiver : com.klinker.android.send_message.MmsReceivedReceiver() {
    override fun onMessageReceived(context: Context, messageUri: Uri) {
        val mms = context.getLatestMMS() ?: return
        val address = mms.participants.firstOrNull()?.phoneNumber ?: ""
        val size = context.resources.getDimension(R.dimen.notification_large_icon_size).toInt()

        ensureBackgroundThread {
            val glideBitmap = try {
                Glide.with(context)
                    .asBitmap()
                    .load(mms.attachment!!.uri)
                    .centerCrop()
                    .into(size, size)
                    .get()
            } catch (e: Exception) {
                null
            }

            context.showReceivedMessageNotification(address, mms.body, mms.thread, glideBitmap)
        }
    }

    override fun onError(context: Context, error: String) {}
}
