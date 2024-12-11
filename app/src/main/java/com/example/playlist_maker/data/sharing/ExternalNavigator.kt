package com.example.playlist_maker.data.sharing

import com.example.playlist_maker.domain.sharing.model.EmailData

interface ExternalNavigator {
    fun shareLink(link: String)
    fun openLink(link: String)
    fun openEmail(emailData: EmailData)
}