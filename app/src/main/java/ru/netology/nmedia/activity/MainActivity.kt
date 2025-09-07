package ru.netology.nmedia.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.WallService
import ru.netology.nmedia.viewmodel.PostViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels()
        viewModel.data.observe(this) { post ->
            with(binding) {
                author.text = post.author
                published.text = post.published
                content.text = post.content
                if (post.likeByMe) {
                    like.setImageResource(R.drawable.ic_set_like_24)
                } else {
                    like.setImageResource(R.drawable.ic_like_24)
                }
                countLikes.text = WallService.textForCounts(post.countLikes)
                countReposts.text = WallService.textForCounts(post.countReposts)
            }
        }
        binding.like.setOnClickListener {
            viewModel.like()
        }
        binding.repost.setOnClickListener {
            viewModel.repost()
        }
    }
}