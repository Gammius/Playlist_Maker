package com.example.playlist_maker.presentation.settings.activity

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.playlist_maker.R
import com.example.playlist_maker.databinding.FragmentSettingsBinding
import com.example.playlist_maker.domain.sharing.model.EmailData
import com.example.practicum.playlist.ui.settings.view_model.SettingsViewModel
import com.google.android.material.switchmaterial.SwitchMaterial
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment(R.layout.fragment_settings) {
    private val viewModel: SettingsViewModel by viewModel()
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val messageIconRound = getString(R.string.message_icon_round)
        val urlIconArrow = getString(R.string.url_icon_arrow)
        val mailUserIconCall = getString(R.string.mail_user_icon_call)
        val subjectIconCall = getString(R.string.subject_icon_call)
        val messageIconCall = getString(R.string.message_icon_call)

        binding.iconRound.setOnClickListener {
            viewModel.shareAppLink(messageIconRound)
        }

        binding.iconCall.setOnClickListener {
            val emailData = EmailData(
                email = mailUserIconCall,
                subject = subjectIconCall,
                body = messageIconCall
            )
            viewModel.openSupportEmail(emailData)
        }

        binding.arrow.setOnClickListener {
            viewModel.openSupportLink(urlIconArrow)
        }

        val themeSwitcher = binding.themeSwitcher
        viewModel.themeSettings.observe(viewLifecycleOwner) { themeSettings ->
            themeSwitcher.isChecked = themeSettings.isDarkTheme
            updateTheme(themeSettings.isDarkTheme)
            applySwitchColors(themeSwitcher)
        }

        themeSwitcher.setOnCheckedChangeListener { _, isChecked ->
            viewModel.switchTheme(isChecked)
            updateTheme(isChecked)
        }
    }
    private fun applySwitchColors(switcher: SwitchMaterial) {
        val thumbColor = if (switcher.isChecked) R.color.blue else R.color.icon_color2
        val trackColor = if (switcher.isChecked) R.color.trackTintNight else R.color.trackTint
        switcher.thumbTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), thumbColor))
        switcher.trackTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), trackColor))
    }

    private fun updateTheme(isDarkTheme: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkTheme) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}