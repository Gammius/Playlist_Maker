package com.example.playlist_maker.domain.sharing

import com.example.playlist_maker.domain.sharing.model.EmailData

interface SharingInteractor {
    fun shareLink(link: String)
    fun openLink(link: String)
    fun openEmail(emailData: EmailData)
}