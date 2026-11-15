package com.avirex.drive.utils

import android.content.Context
import android.widget.Toast

object VoiceCommandParser {
    fun executeCommand(context: Context, rawText: String, isParked: Boolean) {
        val cmd = rawText.lowercase().trim()
        Toast.makeText(context, "AVIREX Voice: \"$rawText\"", Toast.LENGTH_SHORT).show()

        when {
            cmd.contains("waze") -> {
                val dest = cmd.substringAfter("vers ", "").substringAfter("to ", "")
                AppLauncher.navigateWaze(context, dest.ifBlank { null })
            }
            cmd.contains("maps") || cmd.contains("navig") || cmd.contains("itinéraire") || cmd.contains("dir") -> {
                val dest = cmd.substringAfter("vers ", "")
                    .substringAfter("à ", "")
                    .substringAfter("to ", "")
                AppLauncher.navigateGoogleMaps(context, dest.ifBlank { null })
            }
            cmd.contains("spotify") -> AppLauncher.launchApp(context, "com.spotify.music", "Spotify")
            cmd.contains("deezer") -> AppLauncher.launchApp(context, "deezer.android.app", "Deezer")
            cmd.contains("youtube music") -> AppLauncher.launchApp(context, "com.google.android.apps.youtube.music", "YT Music")
            cmd.contains("youtube") -> AppLauncher.openYouTubeSafely(context, isParked)
            cmd.contains("whatsapp") -> AppLauncher.openWhatsApp(context)
            cmd.contains("appel") || cmd.contains("téléphone") || cmd.contains("call") -> AppLauncher.openDialer(context)
            else -> AppLauncher.navigateGoogleMaps(context, rawText)
        }
    }
}