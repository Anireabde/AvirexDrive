package com.avirex.drive.ui.screens

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SettingsScreen(
    isDarkTheme: Boolean,
    onToggleTheme: (Boolean) -> Unit,
    is24Hour: Boolean,
    onToggle24Hour: (Boolean) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.Rounded.ArrowBack, "Retour")
            }
            Spacer(Modifier.width(8.dp))
            Text("AVIREX DRIVE — Paramètres", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        }

        Card(shape = RoundedCornerShape(20.dp)) {
            Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                SettingSwitchRow("Mode Sombre (CarPlay Night)", isDarkTheme, onToggleTheme)
                SettingSwitchRow("Format Horloge 24 Heures", is24Hour, onToggle24Hour)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = {
                            context.startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Activer Accès Musique (Spotify/Deezer)")
                    }

                    OutlinedButton(
                        onClick = {
                            context.startActivity(Intent(Settings.ACTION_DISPLAY_SETTINGS))
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Luminosité Écran")
                    }

                    OutlinedButton(
                        onClick = {
                            context.startActivity(Intent(Settings.ACTION_HOME_SETTINGS))
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Définir comme Launcher Auto")
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingSwitchRow(title: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, fontSize = 16.sp, fontWeight = FontWeight.Medium)
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}