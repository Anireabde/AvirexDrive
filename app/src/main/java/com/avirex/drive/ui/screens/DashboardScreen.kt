package com.avirex.drive.ui.screens

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.avirex.drive.service.AvirexNotificationListener
import com.avirex.drive.utils.AppLauncher
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DashboardScreen(
    is24Hour: Boolean,
    onVoiceClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    val context = LocalContext.current
    val mediaState by AvirexNotificationListener.mediaState.collectAsState()
    var isParked by remember { mutableStateOf(false) }
    var currentTime by remember { mutableStateOf("") }
    var currentDate by remember { mutableStateOf("") }
    val batteryLevel = remember { getBatteryPercentage(context) }

    LaunchedEffect(is24Hour) {
        while (true) {
            val now = Date()
            val timePattern = if (is24Hour) "HH:mm" else "hh:mm a"
            currentTime = SimpleDateFormat(timePattern, Locale.getDefault()).format(now)
            currentDate = SimpleDateFormat("EEE, dd MMM", Locale.getDefault()).format(now)
            delay(1000L)
        }
    }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(14.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Column(
            modifier = Modifier
                .width(155.dp)
                .fillMaxHeight()
                .clip(RoundedCornerShape(26.dp))
                .background(MaterialTheme.colorScheme.surface)
                .padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "AVIREX",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 2.sp
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = currentTime,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = currentDate.uppercase(),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Rounded.BatteryChargingFull, null, tint = Color(0xFF00E676), modifier = Modifier.size(18.dp))
                    Text(" $batteryLevel%  •  22°C", fontSize = 12.sp, fontWeight = FontWeight.Medium)
                }

                Surface(
                    onClick = { isParked = !isParked },
                    shape = RoundedCornerShape(14.dp),
                    color = if (isParked) Color(0xFF00C853).copy(alpha = 0.2f) else Color(0xFFFF5252).copy(alpha = 0.2f)
                ) {
                    Text(
                        text = if (isParked) "PARKED" else "DRIVING",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isParked) Color(0xFF00E676) else Color(0xFFFF5252)
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                IconButton(
                    onClick = onVoiceClick,
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(listOf(Color(0xFF00E5FF), Color(0xFF2979FF)))
                        )
                ) {
                    Icon(Icons.Rounded.Mic, "Voice Assistant", tint = Color.Black, modifier = Modifier.size(28.dp))
                }

                IconButton(
                    onClick = onSettingsClick,
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    Icon(Icons.Rounded.Settings, "Settings", tint = MaterialTheme.colorScheme.onSurface)
                }
            }
        }

        Column(
            modifier = Modifier
                .weight(1.15f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Card(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(26.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Rounded.Navigation, null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(Modifier.width(8.dp))
                        Text("NAVIGATION GPS", fontWeight = FontWeight.Bold, fontSize = 14.sp, letterSpacing = 1.sp)
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        NavButton(
                            title = "Waze",
                            subtitle = "Trafic & Alertes",
                            color = Color(0xFF33CCFF),
                            icon = Icons.Rounded.Explore,
                            modifier = Modifier.weight(1f)
                        ) { AppLauncher.navigateWaze(context) }

                        NavButton(
                            title = "Google Maps",
                            subtitle = "Itinéraires",
                            color = Color(0xFF00E676),
                            icon = Icons.Rounded.Map,
                            modifier = Modifier.weight(1f)
                        ) { AppLauncher.navigateGoogleMaps(context) }
                    }
                }
            }

            Card(
                modifier = Modifier
                    .weight(0.95f)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(26.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AppTile("Appels", Icons.Rounded.Call, Color(0xFF00E676)) { AppLauncher.openDialer(context) }
                    AppTile("Contacts", Icons.Rounded.Contacts, Color(0xFF448AFF)) { AppLauncher.openContacts(context) }
                    AppTile("WhatsApp", Icons.Rounded.Chat, Color(0xFF25D366)) { AppLauncher.openWhatsApp(context) }
                    AppTile("YouTube", Icons.Rounded.PlayCircleFilled, Color(0xFFFF1744)) {
                        AppLauncher.openYouTubeSafely(context, isParked)
                    }
                }
            }
        }

        Card(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            shape = RoundedCornerShape(26.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(18.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(84.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .background(MaterialTheme.colorScheme.background),
                        contentAlignment = Alignment.Center
                    ) {
                        if (mediaState.artwork != null) {
                            Image(
                                bitmap = mediaState.artwork!!.asImageBitmap(),
                                contentDescription = "Album Art",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            Icon(Icons.Rounded.Album, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(44.dp))
                        }
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = mediaState.title,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = mediaState.artist,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { AvirexNotificationListener.previous() }, modifier = Modifier.size(52.dp)) {
                        Icon(Icons.Rounded.SkipPrevious, "Previous", modifier = Modifier.size(36.dp))
                    }

                    IconButton(
                        onClick = { AvirexNotificationListener.playPause() },
                        modifier = Modifier
                            .size(68.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary)
                    ) {
                        Icon(
                            imageVector = if (mediaState.isPlaying) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                            contentDescription = "Play/Pause",
                            tint = Color.Black,
                            modifier = Modifier.size(38.dp)
                        )
                    }

                    IconButton(onClick = { AvirexNotificationListener.next() }, modifier = Modifier.size(52.dp)) {
                        Icon(Icons.Rounded.SkipNext, "Next", modifier = Modifier.size(36.dp))
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    StreamingChip("Spotify", Color(0xFF1DB954), Modifier.weight(1f)) {
                        AppLauncher.launchApp(context, "com.spotify.music", "Spotify")
                    }
                    StreamingChip("Deezer", Color(0xFFA238FF), Modifier.weight(1f)) {
                        AppLauncher.launchApp(context, "deezer.android.app", "Deezer")
                    }
                    StreamingChip("YT Music", Color(0xFFFF0000), Modifier.weight(1f)) {
                        AppLauncher.launchApp(context, "com.google.android.apps.youtube.music", "YouTube Music")
                    }
                }
            }
        }
    }
}

@Composable
private fun NavButton(
    title: String,
    subtitle: String,
    color: Color,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = modifier.height(95.dp),
        shape = RoundedCornerShape(20.dp),
        color = color.copy(alpha = 0.16f)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(icon, null, tint = color, modifier = Modifier.size(34.dp))
            Column {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(subtitle, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
            }
        }
    }
}

@Composable
private fun AppTile(label: String, icon: ImageVector, tint: Color, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(62.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(tint.copy(alpha = 0.18f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, label, tint = tint, modifier = Modifier.size(32.dp))
        }
        Spacer(Modifier.height(6.dp))
        Text(label, fontSize = 12.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun StreamingChip(name: String, accent: Color, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        modifier = modifier.height(42.dp),
        shape = RoundedCornerShape(14.dp),
        color = accent.copy(alpha = 0.2f)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(name, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = accent)
        }
    }
}

private fun getBatteryPercentage(context: Context): Int {
    val intent = context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
    val level = intent?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
    val scale = intent?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
    return if (level >= 0 && scale > 0) (level * 100) / scale else 100
}