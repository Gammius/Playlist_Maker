package com.example.playlist_maker.domain.sharing.impl

import com.example.playlist_maker.data.sharing.ExternalNavigator
import com.example.playlist_maker.domain.sharing.SharingInteractor
import com.example.playlist_maker.domain.sharing.model.EmailData

class SharingInteractorImpl(private val externalNavigator: ExternalNavigator) : SharingInteractor {

    override fun shareLink(link: String) {
        externalNavigator.shareLink(link)
    }

    override fun openLink(link: String) {
        externalNavigator.openLink(link)
    }

    override fun openEmail(emailData: EmailData) {
        externalNavigator.openEmail(emailData)
    }
}