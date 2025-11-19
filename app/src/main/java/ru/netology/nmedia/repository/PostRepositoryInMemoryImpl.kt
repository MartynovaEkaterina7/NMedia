package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post

class PostRepositoryInMemoryImpl : PostRepository {

    private var nextId = 1
    private var posts = listOf(
        Post(
            nextId++,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Знаний хватит на всех: на следующей неделе разбираемся с разработкой мобильных приложений, учимся рассказывать истории и составлять PR-стратегию прямо на бесплатных занятиях \uD83D\uDC47",
            published = "18 сентября в 10:12",
            countLikes = 85,
            likeByMe = false,
            countReposts = 80,
            countViews = 91,
            video = "https://rutube.ru/video/6550a91e7e523f9503bed47e4c46d0cb"
        ),
        Post(
            nextId++,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Знаний хватит на всех: на следующей неделе разбираемся с разработкой мобильных приложений, учимся рассказывать истории и составлять PR-стратегию прямо на бесплатных занятиях \uD83D\uDC47",
            published = "18 сентября в 10:12",
            countLikes = 45,
            likeByMe = false,
            countReposts = 80,
            countViews = 82
        ),
        Post(
            nextId++,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Знаний хватит на всех: на следующей неделе разбираемся с разработкой мобильных приложений, учимся рассказывать истории и составлять PR-стратегию прямо на бесплатных занятиях \uD83D\uDC47",
            published = "18 сентября в 10:12",
            countLikes = 10,
            likeByMe = false,
            countReposts = 60,
            countViews = 74,
            video = "https://rutube.ru/video/6550a91e7e523f9503bed47e4c46d0cb"

        ),
        Post(
            nextId++,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Знаний хватит на всех: на следующей неделе разбираемся с разработкой мобильных приложений, учимся рассказывать истории и составлять PR-стратегию прямо на бесплатных занятиях \uD83D\uDC47",
            published = "18 сентября в 10:12",
            countLikes = 20,
            likeByMe = false,
            countReposts = 30,
            countViews = 45
        ),
        Post(
            id = 1,
            author = "Нетология. Университет интернет-профессий будущего",
            published = "21 мая в 18:36",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
            countLikes = 30,
            likeByMe = false,
            countReposts = 50,
            countViews = 63,
            video = "https://rutube.ru/video/6550a91e7e523f9503bed47e4c46d0cb"
        )
    )

    private val data = MutableLiveData(posts)

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

}