package com.example.playlist_maker.presentation.mediaLibrary.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.playlist_maker.R
import com.example.playlist_maker.Utils.dpToPx
import com.example.playlist_maker.Utils.formatMinutes
import com.example.playlist_maker.Utils.getTrackCountText
import com.example.playlist_maker.Utils.setDialogTextColors
import com.example.playlist_maker.databinding.FragmentPlaylistBinding
import com.example.playlist_maker.domain.playlist.model.Playlist
import com.example.playlist_maker.domain.search.model.Track
import com.example.playlist_maker.presentation.audioPlayer.activity.AudioPlayer
import com.example.playlist_maker.presentation.mediaLibrary.view_model.PlaylistViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment : Fragment() {

    private val playlistViewModel: PlaylistViewModel by viewModel()

    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!
    private lateinit var trackPlaylistAdapter: TrackPlaylistAdapter

    companion object {
        fun newInstanse(): PlaylistFragment {
            return PlaylistFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val playlistId = arguments?.getLong("playlistId") ?: return

        binding.arrowBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        playlistViewModel.loadPlaylistAndTracks(playlistId)

        binding.recyclerViewPlaylist.layoutManager = LinearLayoutManager(requireContext())

        trackPlaylistAdapter = TrackPlaylistAdapter(emptyList(), { track ->
            val intent = createTrackIntent(track)
            startActivity(intent)
        }, { track ->
            showDeleteDialog(playlistId, track)
        })

        binding.recyclerViewPlaylist.adapter = trackPlaylistAdapter

        lifecycleScope.launch {
            playlistViewModel.playlist.collect { state ->
                val playlist = state.playlist
                val trackPlaylist = state.trackPlaylist

                playlist?.let {

                    val coverPlaylistView: ImageView = binding.coverPlaylist
                    Glide.with(requireContext())
                        .load(it.uriImageCoverPlaylist)
                        .placeholder(R.drawable.place_holder_pl)
                        .apply(
                            RequestOptions()
                                .transform(
                                    MultiTransformation(
                                        CenterCrop(),
                                    )
                                )
                        )
                        .into(coverPlaylistView)

                    val namePlaylistView: TextView = binding.namePlaylist
                    namePlaylistView.text = it.namePlaylist

                    val descriptionPlaylistTextView: TextView = binding.descriptionPlaylist
                    descriptionPlaylistTextView.text = it.descriptionPlaylist

                    val trackCountTextView: TextView = binding.trackCount
                    trackCountTextView.text = getTrackCountText(playlist.trackCount)

                    val totalDurationTextView: TextView = binding.totalDuration
                    totalDurationTextView.text = formatMinutes(state.totalDuration)

                    val cornerRadiusPx = dpToPx(2f, requireContext())
                    val coverTextView = binding.cover
                    Glide.with(requireContext()).load(it.uriImageCoverPlaylist)
                        .placeholder(R.drawable.place_holder).apply(
                            RequestOptions()
                                .transform(
                                    MultiTransformation(
                                        CenterCrop(),
                                        RoundedCorners(cornerRadiusPx)
                                    )
                                )
                        ).into(coverTextView)

                    val nameTextView: TextView = binding.name
                    nameTextView.text = it.namePlaylist

                    val countTextView: TextView = binding.count
                    countTextView.text = getTrackCountText(playlist.trackCount)
                }

                binding.noTrackPlaylist.visibility =
                    if (state.noTrackPlaylist) View.VISIBLE else View.GONE
                trackPlaylistAdapter.updateTracks(trackPlaylist)
            }
        }

        val bottomSheetContainer = binding.menuBottomSheet
        val overlay = binding.overlay
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        overlay.visibility = View.VISIBLE
                    }

                    BottomSheetBehavior.STATE_COLLAPSED -> {

                    }

                    BottomSheetBehavior.STATE_HIDDEN -> {
                        overlay.visibility = View.GONE
                    }

                    else -> {
                        overlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        overlay.setOnClickListener {
        }

        binding.menuButton.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        binding.shareButton.setOnClickListener {
            handleShareButtonClick()
        }

        binding.sharePlaylists.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            handleShareButtonClick()
        }

        binding.deletePlaylist.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            val dialog = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog)
                .setTitle("Удалить плейлист")
                .setMessage("Хотите удалить плейлист?")
                .setPositiveButton("Да") { _, _ ->
                    playlistViewModel.deletePlaylist(playlistId)
                    playlistViewModel.updateSuccess.observe(viewLifecycleOwner, Observer { isUpdated ->
                        if (isUpdated) {
                            requireActivity().supportFragmentManager.popBackStack()
                        }
                    })
                }
                .setNegativeButton("Нет", null)
                .create()
            dialog.show()
            setDialogTextColors(dialog, requireContext())
        }

        binding.editPlaylist.setOnClickListener {
            val playlist = playlistViewModel.playlist.value.playlist
            playlist?.let {
                val bundle = Bundle().apply {
                    putLong("playlistId", playlistId)
                    putString("playlistName", it.namePlaylist)
                    putString("playlistDescription", it.descriptionPlaylist)
                    putString("playlistCoverUri", it.uriImageCoverPlaylist.toString())
                }
                findNavController().navigate(
                    R.id.action_playlistFragment_to_playlistEditFragment,
                    bundle
                )
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun createTrackIntent(track: Track): Intent {
        return Intent(requireContext(), AudioPlayer::class.java).apply {
            putExtra("track_id", track.trackId)
            putExtra("track_name", track.trackName)
            putExtra("artist_name", track.artistName)
            putExtra("track_time", track.trackTimeMillis)
            putExtra("collection_name", track.collectionName)
            putExtra("release_date", track.getFormattedReleaseYear(track.releaseDate))
            putExtra("primary_genre_name", track.primaryGenreName)
            putExtra("country", track.country)
            putExtra("preview_url", track.previewUrl)
            putExtra("cover_artwork", track.getCoverArtwork())
            putExtra("is_favorite", track.isFavorite)
        }
    }

    private fun showDeleteDialog(playlistId: Long, track: Track) {

        val dialog = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog)
            .setTitle("Удалить трек")
            .setMessage("Вы уверены, что хотите удалить трек из плейлиста?")
            .setPositiveButton("Удалить") { _, _ ->
                playlistViewModel.deleteTrackPlaylist(playlistId, track)
            }
            .setNegativeButton("Отмена", null)
            .create()
        dialog.show()
        setDialogTextColors(dialog, requireContext())
    }

    private fun handleShareButtonClick() {
        val playlist = playlistViewModel.playlist.value.playlist
        val trackPlaylist = playlistViewModel.playlist.value.trackPlaylist

        if (trackPlaylist.isEmpty()) {
            Toast.makeText(
                requireContext(),
                "В этом плейлисте нет списка треков, которым можно поделиться",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            val shareText = buildShareText(playlist, trackPlaylist)

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, shareText)
            }

            val chooser = Intent.createChooser(shareIntent, "Поделиться плейлистом")
            startActivity(chooser)
        }
    }

    private fun buildShareText(playlist: Playlist?, trackPlaylist: List<Track>): String {
        val builder = StringBuilder()

        builder.append(playlist?.namePlaylist ?: "Без названия")
        builder.append("\n")
        builder.append(playlist?.descriptionPlaylist ?: "Без описания")
        builder.append("\n")
        builder.append("${trackPlaylist.size} треков")
        builder.append("\n")

        trackPlaylist.forEachIndexed { index, track ->
            val trackDuration = formatTrackDuration(track.trackTimeMillis)
            builder.append("${index + 1}. ${track.artistName} - ${track.trackName} ($trackDuration)\n")
        }

        return builder.toString()
    }

    private fun formatTrackDuration(durationMillis: Long): String {
        val minutes = durationMillis / 60000
        val seconds = (durationMillis % 60000) / 1000
        return String.format("%02d:%02d", minutes, seconds)
    }
}