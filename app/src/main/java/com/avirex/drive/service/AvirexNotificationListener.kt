package com.avirex.drive.service

import android.content.ComponentName
import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadata
import android.media.session.MediaController
import android.media.session.MediaSessionManager
import android.media.session.PlaybackState
import android.service.notification.NotificationListenerService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class MediaUiState(
    val title: String = "Aucune lecture",
    val artist: String = "AVIREX Audio Engine",
    val isPlaying: Boolean = false,
    val artwork: Bitmap? = null,
    val sourcePackage: String = ""
)

class AvirexNotificationListener : NotificationListenerService() {

    companion object {
        private val _mediaState = MutableStateFlow(MediaUiState())
        val mediaState: StateFlow<MediaUiState> = _mediaState
        private var activeController: MediaController? = null

        fun playPause() {
            val state = activeController?.playbackState?.state
            if (state == PlaybackState.STATE_PLAYING) {
                activeController?.transportControls?.pause()
            } else {
                activeController?.transportControls?.play()
            }
        }

        fun next() = activeController?.transportControls?.skipToNext()
        fun previous() = activeController?.transportControls?.skipToPrevious()
    }

    private val callback = object : MediaController.Callback() {
        override fun onMetadataChanged(metadata: MediaMetadata?) = updateFromController(activeController)
        override fun onPlaybackStateChanged(state: PlaybackState?) = updateFromController(activeController)
    }

    override fun onListenerConnected() {
        super.onListenerConnected()
        val manager = getSystemService(Context.MEDIA_SESSION_SERVICE) as MediaSessionManager
        val component = ComponentName(this, AvirexNotificationListener::class.java)
        
        manager.addOnActiveSessionsChangedListener({ controllers ->
            bindController(controllers?.firstOrNull())
        }, component)

        bindController(manager.getActiveSessions(component).firstOrNull())
    }

    private fun bindController(controller: MediaController?) {
        activeController?.unregisterCallback(callback)
        activeController = controller
        activeController?.registerCallback(callback)
        updateFromController(controller)
    }

    private fun updateFromController(controller: MediaController?) {
        if (controller == null) {
            _mediaState.value = MediaUiState()
            return
        }
        val meta = controller.metadata
        val state = controller.playbackState
        _mediaState.value = MediaUiState(
            title = meta?.getString(MediaMetadata.METADATA_KEY_TITLE) ?: "Piste inconnue",
            artist = meta?.getString(MediaMetadata.METADATA_KEY_ARTIST) ?: "Artiste inconnu",
            isPlaying = state?.state == PlaybackState.STATE_PLAYING,
            artwork = meta?.getBitmap(MediaMetadata.METADATA_KEY_ALBUM_ART)
                ?: meta?.getBitmap(MediaMetadata.METADATA_KEY_ART),
            sourcePackage = controller.packageName
        )
    }
}