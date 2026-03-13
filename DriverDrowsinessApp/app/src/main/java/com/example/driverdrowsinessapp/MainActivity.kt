package com.example.driverdrowsinessapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.driverdrowsinessapp.ui.theme.DriverDrowsinessAppTheme

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError

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

    val database = FirebaseDatabase.getInstance()
    val ref = database.getReference("drowsiness_events")

    LaunchedEffect(Unit) {

        ref.limitToLast(1).addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                for (child in snapshot.children) {

                    val event = child.child("event").value.toString()
                    val trigger = child.child("trigger").value.toString()
                    val time = child.child("timestamp").value.toString()

                    driverStatus =
                        "⚠ $event\nTrigger: $trigger\nTime: $time"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                driverStatus = "Database Error"
            }
        })
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->

        DriverDashboard(
            status = driverStatus,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
fun DriverDashboard(status: String, modifier: Modifier = Modifier) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(30.dp),
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Driver Monitoring System",
            fontSize = 30.sp
        )

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "Driver Status:",
            fontSize = 22.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = status,
            fontSize = 20.sp
        )
    }
}
