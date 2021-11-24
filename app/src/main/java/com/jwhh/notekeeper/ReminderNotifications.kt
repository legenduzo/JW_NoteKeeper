package com.jwhh.notekeeper

import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.content.Intent.getIntent
import android.os.Build
import androidx.core.app.NotificationCompat

object ReminderNotifications {
    private const val NOTIFICATION_TAG = "Reminder"
    const val REMINDER_CHANNEL = "reminders"

    fun notify (context: Context, titleText: String, noteText:String, notePosition: Int) {

        val intent = Intent(context, NoteActivity::class.java)
        intent.putExtra(NOTE_POSITION, notePosition)

        val pendingIntent = TaskStackBuilder.create(context)
            .addNextIntentWithParentStack(intent)
            .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)

        val shareIntent = PendingIntent.getActivity(context, 0,
        Intent.createChooser(Intent(Intent.ACTION_SEND)
            .setType("text/plain")
            .putExtra(Intent.EXTRA_TEXT, noteText
            ),
        "Share Note Reminder"),
        PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(context, REMINDER_CHANNEL)
            .setDefaults(Notification.DEFAULT_ALL)
            .setSmallIcon(R.drawable.ic_white_notification)
            .setContentTitle(titleText)
            .setContentText(noteText)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .addAction(R.drawable.ic_menu_share, "Share", shareIntent)

        notify(context,builder.build())
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    private fun notify(context: Context, notification: Notification) {
        val nm = context
            .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            nm.notify(NOTIFICATION_TAG, 0, notification)
        } else {
            nm.notify(NOTIFICATION_TAG.hashCode(), notification)
        }
    }
}