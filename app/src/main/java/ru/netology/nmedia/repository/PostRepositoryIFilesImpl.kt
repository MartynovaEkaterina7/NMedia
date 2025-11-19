package ru.netology.nmedia.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.nmedia.dto.Post
import androidx.core.content.edit

class PostRepositoryIFilesImpl(private val context: Context) : PostRepository {

    private var nextId = 1
    private var posts = listOf<Post>()
        set(value) {
            field = value
            sync()
        }

    private val data = MutableLiveData(posts)

    init {
        val file = context.filesDir.resolve(FILENAME)
        if (file.exists()) {
            context.openFileInput(FILENAME).bufferedReader().use {
                posts = gson.fromJson(it, type)
                nextId = posts.maxOfOrNull { it.id }?.inc() ?: 1
                data.value = posts
            }
        }
    }

    private fun sync() {
        context.openFileOutput(FILENAME, Context.MODE_PRIVATE).bufferedWriter().use {
            it.write(gson.toJson(posts))
        }
    }

    override fun get(): LiveData<List<Post>> = data

    override fun likeById(id: Int) {
        posts = posts.map { post ->
            if (post.id == id) {
                post.copy(
                    likeByMe = !post.likeByMe,
                    countLikes = if (post.likeByMe) {
                        post.countLikes - 1
                    } else {
                        post.countLikes + 1
                    }
                )
            } else {
                post
            }
        }
        data.value = posts
    }

    override fun repostById(id: Int) {
        posts = posts.map { post ->
            if (post.id == id) {
                post.copy(
                    countReposts = post.countReposts + 1
                )
            } else {
                post
            }
        }
        data.value = posts
    }

    override fun viewById(id: Int) {
        posts = posts.map { post ->
            if (post.id == id) {
                post.copy(
                    countViews = post.countViews + 1
                )
            } else {
                post
            }
        }
        data.value = posts
    }

    override fun removeById(id: Int) {
        posts = posts.filter { post ->
            post.id != id
        }
        data.value = posts
    }

    override fun save(post: Post) {
        posts = if (post.id == 0) {
            listOf(
                post.copy(
                    id = nextId++,
                    author = "me",
                    published = "now"
                )
            ) + posts
        } else {
            posts.map {
                if (it.id != post.id) it else it.copy(content = post.content)
            }
        }
        data.value = posts
    }

    companion object {
        private const val FILENAME = "posts.json"
        private val gson = Gson()
        private val type = TypeToken.getParameterized(List::class.java, Post::class.java).type
    }

}