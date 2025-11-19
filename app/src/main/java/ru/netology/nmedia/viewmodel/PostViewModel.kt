package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryIFilesImpl

private val empty = Post(
    id = 0,
    author = "",
    published = "",
    content = "",
    countLikes = 0,
    likeByMe = false,
    countReposts = 0,
    countViews = 0
)

class PostViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PostRepository = PostRepositoryIFilesImpl (application)
    val data: LiveData<List<Post>> = repository.get()
    val edited = MutableLiveData(empty)
    fun like(id: Int) = repository.likeById(id)

    fun repost(id: Int) = repository.repostById(id)
    fun view(id: Int) = repository.viewById(id)
    fun remove(id: Int) = repository.removeById(id)
    fun save(text: String) {
        edited.value?.let { post ->
            val content = text.trim()
            if (content != post.content) {
                repository.save(post.copy(content = content))
            }
            edited.value = empty
        }
    }

    fun edit(post: Post) {
        edited.value = post
    }

    fun cancel() {
        edited.value = empty
    }
}