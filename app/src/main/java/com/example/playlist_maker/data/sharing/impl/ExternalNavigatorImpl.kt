package com.example.playlist_maker.data.sharing.impl

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.playlist_maker.domain.sharing.ExternalNavigator
import com.example.playlist_maker.domain.sharing.model.EmailData

class ExternalNavigatorImpl (private val context: Context) : ExternalNavigator {
    override fun shareLink(link: String) {
        val shareAppIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, link)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(shareAppIntent)
    }

    override fun openLink(link: String) {
       val iconArrowIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link)).apply{
           flags = Intent.FLAG_ACTIVITY_NEW_TASK
       }

        context.startActivity(iconArrowIntent)
    }

    override fun openEmail(emailData: EmailData) {
        val iconCallIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(emailData.email))
            putExtra(Intent.EXTRA_SUBJECT, emailData.subject)
            putExtra(Intent.EXTRA_TEXT, emailData.body)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(iconCallIntent)
    }
}