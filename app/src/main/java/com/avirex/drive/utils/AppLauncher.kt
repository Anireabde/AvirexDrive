package com.avirex.drive.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.ContactsContract
import android.widget.Toast

object AppLauncher {

    fun launchApp(context: Context, packageName: String, appName: String) {
        val intent = context.packageManager.getLaunchIntentForPackage(packageName)
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } else {
            Toast.makeText(context, "$appName non installé", Toast.LENGTH_SHORT).show()
        }
    }

    fun navigateWaze(context: Context, query: String? = null) {
        val uri = if (query.isNullOrBlank()) {
            Uri.parse("https://waze.com/ul")
        } else {
            Uri.parse("https://waze.com/ul?q=${Uri.encode(query)}&navigate=yes")
        }
        val intent = Intent(Intent.ACTION_VIEW, uri).apply {
            setPackage("com.waze")
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            launchApp(context, "com.waze", "Waze")
        }
    }

    fun navigateGoogleMaps(context: Context, query: String? = null) {
        val uri = if (query.isNullOrBlank()) {
            Uri.parse("geo:0,0?q=")
        } else {
            Uri.parse("google.navigation:q=${Uri.encode(query)}")
        }
        val intent = Intent(Intent.ACTION_VIEW, uri).apply {
            setPackage("com.google.android.apps.maps")
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            launchApp(context, "com.google.android.apps.maps", "Google Maps")
        }
    }

    fun openDialer(context: Context) {
        val intent = Intent(Intent.ACTION_DIAL).apply { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) }
        context.startActivity(intent)
    }

    fun openContacts(context: Context) {
        val intent = Intent(Intent.ACTION_VIEW, ContactsContract.Contacts.CONTENT_URI).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }

    fun openWhatsApp(context: Context) = launchApp(context, "com.whatsapp", "WhatsApp")

    fun openYouTubeSafely(context: Context, isParked: Boolean) {
        if (!isParked) {
            Toast.makeText(
                context,
                "⚠️ Sécurité AVIREX : Activez le mode PARKING pour lancer YouTube vidéo.",
                Toast.LENGTH_LONG
            ).show()
            return
        }
        launchApp(context, "com.google.android.youtube", "YouTube")
    }
}