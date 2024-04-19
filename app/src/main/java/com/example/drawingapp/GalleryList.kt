package com.example.drawingapp

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

data class UserInfo (val email: String, val drawingUrls: List<String>)

@Composable
fun GalleryList(context: Context?) {
    val loading: MutableState<Boolean> = remember { mutableStateOf(true) }
    val bitmaps: SnapshotStateList<fileAndBitmap> = remember { mutableStateListOf() }

    if (loading.value) {
        // Asynchronously get all of the users from the database
        LaunchedEffect(true) {
            val users: List<UserInfo> = fetchUsersFromDatabase(context!!)
            val drawings: List<fileAndBitmap> = fetchDrawings(users, context)

            bitmaps.addAll(drawings)
            loading.value = false
            Toast.makeText(context, "Drawings downloaded", Toast.LENGTH_LONG).show()
        }
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        if (loading.value) {
            CircularProgressIndicator(
                modifier = Modifier
                    .width(64.dp)
                    .align(Alignment.CenterHorizontally)
                    .padding(top = Dp(100f))
            )
        } else {
            bitmaps.forEach { bitmap ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {

                    Image(
                        bitmap.drawing.asImageBitmap(),
                        bitmap.filename,
                        modifier = Modifier
                            .size(100.dp)
                            .border(BorderStroke(1.dp, Color.Black))
                    )

                    Column(
                        Modifier.weight(1f)
                    ) {
                        Text(
                            bitmap.filename,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            "Created by ${bitmap.creator}",
                            textAlign = TextAlign.Center,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

// I used ChatGPT for these. Not for the whole function, just to help me make them asynchronous instead of using callback like elsewhere.
suspend fun fetchUsersFromDatabase(toastContext: Context): List<UserInfo> = withContext(Dispatchers.IO) {
    val userInfos: MutableList<UserInfo> = mutableListOf()
    try {
        val db = Firebase.firestore
        val documents = db.collection("users").get().await()
        for (document in documents) {
            val email = document.getString("email") ?: ""
            val drawingUrls = document.get("drawings") as? List<String> ?: emptyList()
            userInfos.add(UserInfo(email = email, drawingUrls = drawingUrls))
        }
    } catch (e: Exception) {
        Log.e("fetchUsersFromDatabase", "Error fetching users", e)
        Toast.makeText(toastContext, "Error fetching users from database", Toast.LENGTH_LONG).show()
    }
    userInfos
}

suspend fun fetchDrawings(users: List<UserInfo>, toastContext: Context): List<fileAndBitmap> = withContext(Dispatchers.IO) {
    val drawings: MutableList<fileAndBitmap> = mutableListOf()
    try {
        users.forEach { user ->
            user.drawingUrls.forEach { url ->
                val storageRef = Firebase.storage.getReferenceFromUrl(url)
                val bytes = storageRef.getBytes(Long.MAX_VALUE).await()
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                val filename = url.split('/').last().split('.').first()
                drawings.add(fileAndBitmap(filename = filename, bitmap = bitmap, owner = user.email))
            }
        }
    } catch (e: Exception) {
        Log.e("fetchDrawings", "Error fetching drawings", e)
        Toast.makeText(toastContext, "Error fetching drawings from database", Toast.LENGTH_LONG).show()
    }
    drawings
}