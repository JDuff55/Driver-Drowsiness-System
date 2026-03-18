package com.example.driverdrowsinessapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.driverdrowsinessapp.ui.theme.DriverDrowsinessAppTheme
import android.content.Context
import android.media.MediaPlayer
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import androidx.compose.material3.Button
import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            DriverDrowsinessAppTheme {
                DriverScreen()
            }
        }
    }
}

@Composable
fun DriverScreen() {

    var driverStatus by remember { mutableStateOf("Monitoring...") }
    var eventList by remember { mutableStateOf(listOf<String>()) }
    //var lastEventTime by remember {mutableStateOf("")}
    var alertsEnabled by remember {mutableStateOf(true)}
    var totalEvents by remember { mutableStateOf(0) }
    var earEvents by remember { mutableStateOf(0) }
    var headNodEvents by remember { mutableStateOf(0) }
    var lastEventTime by remember { mutableStateOf("-") }
    var riskLevel by remember { mutableStateOf("LOW") }


    val context = androidx.compose.ui.platform.LocalContext.current

    val database = FirebaseDatabase.getInstance()
    val ref = database.getReference("drowsiness_events")

    LaunchedEffect(Unit) {

        ref.limitToLast(10).addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                val tempList = mutableListOf<String>()

                var total = 0
                var earCount = 0
                var headCount = 0
                var latestTime = "-"

                for (child in snapshot.children) {

                    val event = child.child("event").value.toString()
                    val trigger = child.child("trigger").value.toString()
                    val time = child.child("timestamp").value.toString()

                    total += 1
                    latestTime = time

                    if (trigger == "EAR") {
                        earCount += 1
                    } else if (trigger == "HEAD_NOD") {
                        headCount += 1
                    }

                    tempList.add("$time - $trigger")

                    driverStatus = "⚠ $event\nTrigger: $trigger\nTime: $time"
                }

                totalEvents = total
                earEvents = earCount
                headNodEvents = headCount
                lastEventTime = latestTime
                eventList = tempList.reversed()

                riskLevel = when {
                    total >= 10 -> "HIGH"
                    total >= 5 -> "MEDIUM"
                    total >= 1 -> "LOW-MEDIUM"
                    else -> "LOW"
                }
            }


            override fun onCancelled(error: DatabaseError) {
                driverStatus = "Database Error"
            }
        })
    }

    DriverDashboard(driverStatus,
        eventList,
        totalEvents = totalEvents,
        earEvents = earEvents,
        headNodEvents = headNodEvents,
        lastEventTime = lastEventTime,
        riskLevel = riskLevel,
        alertsEnabled = alertsEnabled,
        onToggleAlerts = {alertsEnabled = !alertsEnabled
        if (!alertsEnabled) {
            mediaPlayer?.stop()
            mediaPlayer = null
        }
        },
        onDriverSafe = {
            sendDriverResponse()

            driverStatus = "Driver SAFE"
            alertsEnabled = false

            mediaPlayer?.stop()
            mediaPlayer = null
        }
        )
}


@Composable
fun DriverDashboard(
    status: String,
    history: List<String>,
    totalEvents: Int,
    earEvents: Int,
    headNodEvents: Int,
    lastEventTime: String,
    riskLevel: String,
    alertsEnabled: Boolean,
    onToggleAlerts: () -> Unit,
    onDriverSafe: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDrowsy = status.contains("DROWSINESS")
    val statusColor = if (isDrowsy) Color(0xFFD32F2F) else Color(0xFF2E7D32)
    val statusBg = if (isDrowsy) Color(0xFFFFEBEE) else Color(0xFFE8F5E9)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7FA))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Driver Monitoring Dashboard",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1F2937)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Real-time drowsiness monitoring",
                fontSize = 14.sp,
                color = Color(0xFF6B7280)
            )
        }

        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = statusBg),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Current Status",
                        fontSize = 14.sp,
                        color = Color(0xFF6B7280)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (isDrowsy) "DROWSY" else "SAFE",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = statusColor
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = status,
                        fontSize = 16.sp,
                        color = Color(0xFF111827)
                    )
                }
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Analytics",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF111827)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    AnalyticsRow("Total Events", totalEvents.toString())
                    AnalyticsRow("EAR Events", earEvents.toString())
                    AnalyticsRow("Head Nod Events", headNodEvents.toString())
                    AnalyticsRow("Last Event Time", lastEventTime)
                    AnalyticsRow("Risk Level", riskLevel)
                }
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Controls",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF111827)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = onToggleAlerts,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text(if (alertsEnabled) "Silence Alerts" else "Enable Alerts")
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedButton(
                        onClick = onDriverSafe,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text("Driver Confirmed Safe")
                    }
                }
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Recent Events",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF111827)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    if (history.isEmpty()) {
                        Text(
                            text = "No events recorded yet.",
                            fontSize = 15.sp,
                            color = Color(0xFF6B7280)
                        )
                    } else {
                        history.take(5).forEach { item ->
                            EventRow(item)
                        }
                    }
                }
            }
        }
    }
}


var mediaPlayer: MediaPlayer? = null

fun triggerAlert(context: Context) {

    if (mediaPlayer == null) {
        mediaPlayer = MediaPlayer.create(
            context,
            android.provider.Settings.System.DEFAULT_ALARM_ALERT_URI
        )
    }

    if (mediaPlayer?.isPlaying == false) {
        mediaPlayer?.start()
    }
}


fun showNotification(context: Context) {

    val channelId = "drowsiness_alert"

    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    // Create channel (required for Android 8+)
    val channel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        NotificationChannel(
            channelId,
            "Drowsiness Alerts",
            NotificationManager.IMPORTANCE_HIGH
        )
    } else {
        TODO("VERSION.SDK_INT < O")
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        notificationManager.createNotificationChannel(channel)
    }

    val notification = NotificationCompat.Builder(context, channelId)
        .setContentTitle("Driver Alert")
        .setContentText("Drowsiness Detected!")
        .setSmallIcon(android.R.drawable.ic_dialog_alert)
        .build()

    notificationManager.notify(1, notification)
}

fun sendDriverResponse() {

    val database = FirebaseDatabase.getInstance()
    val ref = database.getReference("driver_responses")

    val response = mapOf(
        "timestamp" to System.currentTimeMillis().toString(),
        "response" to "DRIVER CONFIRMED SAFE"
    )

    ref.push().setValue(response)

}

@Composable
fun AnalyticsRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 15.sp,
            color = Color(0xFF6B7280)
        )
        Text(
            text = value,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF111827)
        )
    }
}

@Composable
fun EventRow(event: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9FAFB))
    ) {
        Text(
            text = event,
            modifier = Modifier.padding(14.dp),
            fontSize = 14.sp,
            color = Color(0xFF111827)
        )
    }
}


