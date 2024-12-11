import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlist_maker.domain.audioPlayer.AudioPlayerInteractor
import com.example.playlist_maker.presentation.audioPlayer.view_model.AudioPlayerViewModel

class AudioPlayerViewModelFactory(
    private val audioPlayerInteractor: AudioPlayerInteractor
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AudioPlayerViewModel(audioPlayerInteractor) as T
    }
}