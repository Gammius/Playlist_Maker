package com.example.playlist_maker.Utils

import android.content.Context
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.playlist_maker.R

fun setDialogTextColors(dialog: AlertDialog, context: Context) {

    val titleTextView = dialog.findViewById<TextView>(android.R.id.title)
    titleTextView?.setTextColor(ContextCompat.getColor(context, R.color.custom_title_color))

    val messageTextView = dialog.findViewById<TextView>(android.R.id.message)
    messageTextView?.setTextColor(ContextCompat.getColor(context, R.color.custom_message_color))

    val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
    positiveButton.setTextColor(ContextCompat.getColor(context, R.color.custom_button_color))

    val negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
    negativeButton.setTextColor(ContextCompat.getColor(context, R.color.custom_button_color))
}
