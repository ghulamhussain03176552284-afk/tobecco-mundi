package com.example.data.local

import android.content.Context
import com.example.data.model.TobaccoPost
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.io.File

class TobaccoStorage(private val context: Context) {
    private val moshi: Moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
    private val listType = Types.newParameterizedType(List::class.java, TobaccoPost::class.java)
    private val adapter = moshi.adapter<List<TobaccoPost>>(listType)
    private val file = File(context.filesDir, "tobacco_posts.json")
    private val mutex = Mutex()

    private val _postsFlow = MutableStateFlow<List<TobaccoPost>>(emptyList())
    val postsFlow: Flow<List<TobaccoPost>> = _postsFlow.asStateFlow()

    suspend fun initialize() = withContext(Dispatchers.IO) {
        mutex.withLock {
            val loadedPosts = loadFromFile()
            _postsFlow.value = loadedPosts
        }
    }

    suspend fun getCount(): Int = withContext(Dispatchers.IO) {
        mutex.withLock {
            _postsFlow.value.size
        }
    }

    suspend fun insertPost(post: TobaccoPost) = withContext(Dispatchers.IO) {
        mutex.withLock {
            val current = _postsFlow.value.toMutableList()
            // Add to top of feed
            current.removeAll { it.id == post.id }
            current.add(0, post)
            _postsFlow.value = current
            saveToFile(current)
        }
    }

    suspend fun insertAll(posts: List<TobaccoPost>) = withContext(Dispatchers.IO) {
        mutex.withLock {
            val current = _postsFlow.value.toMutableList()
            for (p in posts) {
                if (current.none { it.id == p.id }) {
                    current.add(p)
                }
            }
            _postsFlow.value = current
            saveToFile(current)
        }
    }

    suspend fun updatePost(post: TobaccoPost) = withContext(Dispatchers.IO) {
        mutex.withLock {
            val current = _postsFlow.value.map { if (it.id == post.id) post else it }
            _postsFlow.value = current
            saveToFile(current)
        }
    }

    suspend fun getPostById(postId: String): TobaccoPost? = withContext(Dispatchers.IO) {
        mutex.withLock {
            _postsFlow.value.find { it.id == postId }
        }
    }

    suspend fun deletePost(postId: String) = withContext(Dispatchers.IO) {
        mutex.withLock {
            val current = _postsFlow.value.filterNot { it.id == postId }
            _postsFlow.value = current
            saveToFile(current)
        }
    }

    private fun loadFromFile(): List<TobaccoPost> {
        return try {
            if (file.exists()) {
                val json = file.readText()
                adapter.fromJson(json) ?: emptyList()
            } else {
                emptyList()
            }
        } catch (_: Exception) {
            emptyList()
        }
    }

    private fun saveToFile(posts: List<TobaccoPost>) {
        try {
            val json = adapter.toJson(posts)
            file.writeText(json)
        } catch (_: Exception) {
            // Log/ignore fallback
        }
    }
}
