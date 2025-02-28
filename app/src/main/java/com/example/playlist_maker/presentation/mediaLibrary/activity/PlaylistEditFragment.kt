package com.example.playlist_maker.presentation.mediaLibrary.activity

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.playlist_maker.R
import com.example.playlist_maker.Utils.dpToPx
import com.example.playlist_maker.presentation.mediaLibrary.view_model.PlaylistEditViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistEditFragment : NewPlaylistFragment() {

    private val playlistEditViewModel: PlaylistEditViewModel by viewModel()

    override val pickImage =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
            if (uri != null) {
                binding.placeForCover.setImageURI(uri)
                binding.placeForCover.scaleType = ImageView.ScaleType.CENTER_CROP
                playlistEditViewModel.onImageUriChanged(uri)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.placeForCover.setOnClickListener {
            pickImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().supportFragmentManager.popBackStack()
                }
            })

        binding.arrowBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        binding.textNewPlaylist.text = "Редактировать"

        val playlistId = arguments?.getLong("playlistId", 0)
        val playlistName = arguments?.getString("playlistName")
        val playlistDescription = arguments?.getString("playlistDescription")
        val playlistCoverUri = arguments?.getString("playlistCoverUri")


        playlistEditViewModel.initializePlaylistData(
            playlistName = playlistName,
            playlistDescription = playlistDescription,
            playlistCoverUri = playlistCoverUri
        )

        playlistEditViewModel.newPlaylistState.observe(viewLifecycleOwner, Observer { state ->
            if (binding.editTextNamePlaylistLayoutText.text.toString() != state.namePlaylist) {
                binding.editTextNamePlaylistLayoutText.setText(state.namePlaylist)
            }
            if (binding.editTextDescriptionPlaylistText.text.toString() != state.descriptionPlaylist) {
                binding.editTextDescriptionPlaylistText.setText(state.descriptionPlaylist)
            }
            val cornerRadiusPx = dpToPx(8f, requireContext())
            state.uriImageCoverPlaylist?.let { uri ->
                Glide.with(requireContext())
                    .load(uri)
                    .placeholder(R.drawable.place_holder_pl)
                    .apply(
                        RequestOptions()
                            .transform(
                                MultiTransformation(
                                    CenterCrop(),
                                    RoundedCorners(cornerRadiusPx)
                                )
                            )
                    )
                    .into(binding.placeForCover)
            }
        })

        binding.editTextNamePlaylistLayoutText.addTextChangedListener {
            val namePlaylist = it.toString()
            playlistEditViewModel.onPlaylistNameChanged(namePlaylist)
        }

        binding.editTextDescriptionPlaylistText.addTextChangedListener {
            val descriptionPlaylist = it.toString()
            playlistEditViewModel.onPlaylistDescriptionChanged(descriptionPlaylist)
        }

        binding.createButton.text = "Сохранить"
        binding.createButton.setOnClickListener {
            val namePlaylist = binding.editTextNamePlaylistLayoutText.text.toString()
            val descriptionPlaylist = binding.editTextDescriptionPlaylistText.text.toString()
            val uriImageCoverPlaylist =
                playlistEditViewModel.newPlaylistState.value?.uriImageCoverPlaylist

            playlistEditViewModel.updateEditPlaylist(
                playlistId ?: 0,
                namePlaylist,
                descriptionPlaylist,
                uriImageCoverPlaylist
            )

            playlistEditViewModel.updateSuccess.observe(viewLifecycleOwner, Observer { isUpdated ->
                if (isUpdated) {
                    requireActivity().supportFragmentManager.popBackStack()
                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}