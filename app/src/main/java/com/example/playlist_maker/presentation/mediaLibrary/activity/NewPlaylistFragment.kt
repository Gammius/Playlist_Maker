package com.example.playlist_maker.presentation.mediaLibrary.activity

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.playlist_maker.R
import com.example.playlist_maker.Utils.setDialogTextColors
import com.example.playlist_maker.databinding.FragmentNewPlaylistBinding
import com.example.playlist_maker.presentation.mediaLibrary.view_model.NewPlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

open class NewPlaylistFragment : Fragment(R.layout.fragment_new_playlist) {
    private val newPlaylistViewModel: NewPlaylistViewModel by viewModel()
    protected var _binding: FragmentNewPlaylistBinding? = null
    protected val binding get() = _binding!!

    open val pickImage =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
            if (uri != null) {
                binding.placeForCover.setImageURI(uri)
                binding.placeForCover.scaleType = ImageView.ScaleType.CENTER_CROP
                newPlaylistViewModel.onImageUriChanged(uri)
            }
        }

    companion object {
        fun newInstance(): NewPlaylistFragment {
            return NewPlaylistFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = FragmentNewPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    checkForUnsavedChanges()
                }
            })

        binding.arrowBack.setOnClickListener {
            checkForUnsavedChanges()
        }

        newPlaylistViewModel.newPlaylistState.observe(viewLifecycleOwner, Observer { state ->
            binding.createButton.isEnabled = state.isSendButtonEnabled
        })

        binding.editTextNamePlaylistLayoutText.addTextChangedListener {
            val namePlaylist = it.toString()
            newPlaylistViewModel.onPlaylistNameChanged(namePlaylist)
        }

        binding.editTextDescriptionPlaylistText.addTextChangedListener {
            val descriptionPlaylist = it.toString()
            newPlaylistViewModel.onPlaylistDescriptionChanged(descriptionPlaylist)
        }

        binding.createButton.setOnClickListener {
            val namePlaylist = newPlaylistViewModel.newPlaylistState.value?.namePlaylist.orEmpty()
            newPlaylistViewModel.createNewPlaylist()
            requireActivity().supportFragmentManager.popBackStack()
            Toast.makeText(requireContext(), "Плейлист ${namePlaylist} создан", Toast.LENGTH_LONG)
                .show()
        }

        binding.placeForCover.setOnClickListener {
            pickImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    private fun checkForUnsavedChanges() {
        val state = newPlaylistViewModel.newPlaylistState.value
        if (state != null && (state.namePlaylist.isNotEmpty() || state.descriptionPlaylist.isNotEmpty() || state.uriImageCoverPlaylist != null)) {
            showDiscardChangesDialog()
        } else {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun showDiscardChangesDialog() {
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Завершить создание плейлиста?")
            .setMessage("Все несохраненные данные будут потеряны.")
            .setPositiveButton("Завершить") { _, _ ->
                requireActivity().supportFragmentManager.popBackStack()
            }.setNegativeButton("Отмена", null).create()
        dialog.show()
        setDialogTextColors(dialog, requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}