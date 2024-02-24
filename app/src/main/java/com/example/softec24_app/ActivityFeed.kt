package com.example.softec24_app

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

data class ActivityPost(val userId: String, val content: String)
data class Achievement(val userId: String, val achievement: String)
data class Photo(val userId: String, val imageUrl: String)
data class Video(val userId: String, val videoUrl: String)

val db = FirebaseFirestore.getInstance()

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ActivityFeedScreen() {
    var activityFeed by remember { mutableStateOf(emptyList<Any>()) }



    fun fetchActivityFeed() {
        // Fetch activity feed from Firestore
        // Example: Fetching activity posts
        db.collection("activity_posts")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                val posts = mutableListOf<ActivityPost>()
                for (document in result) {
                    val userId = document.getString("userId") ?: ""
                    val content = document.getString("content") ?: ""
                    posts.add(ActivityPost(userId, content))
                }
                activityFeed = posts
            }
            .addOnFailureListener { exception ->
                // Handle any errors
            }
    }

    LaunchedEffect(true) {
        fetchActivityFeed()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Activity Feed") }
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(activityFeed) { item ->
                when (item) {
                    is ActivityPost -> {
                        ActivityPostItem(post = item)
                    }
                    is Achievement -> {
                        AchievementItem(achievement = item)
                    }
                    is Photo -> {
                        PhotoItem(photo = item)
                    }
                    is Video -> {
                        VideoItem(video = item)
                    }
                }
            }
        }
    }
}

@Composable
fun ActivityPostItem(post: ActivityPost) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray)//backgroundColor = Color.LightGray
            //.elevation = 8.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            Text(text = "Post by: ${post.userId}")
            Text(text = "Content: ${post.content}")
        }
    }
}

@Composable
fun AchievementItem(achievement: Achievement) {
    // Similar implementation as ActivityPostItem
}

@Composable
fun PhotoItem(photo: Photo) {
    // Similar implementation as ActivityPostItem
}

@Composable
fun VideoItem(video: Video) {
    // Similar implementation as ActivityPostItem
}

