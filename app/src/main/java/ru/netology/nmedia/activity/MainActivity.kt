package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.util.AndroidUtils
import ru.netology.nmedia.viewmodel.PostViewModel


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels()
        val adapter = PostsAdapter(object : OnInteractionListener {
            override fun like(post: Post) {
                viewModel.like(post.id)
            }

            override fun repost(post: Post) {
                viewModel.repost(post.id)
            }

            override fun remove(post: Post) {
                viewModel.remove(post.id)
            }

            override fun edit(post: Post) {
                viewModel.edit(post)
            }
        })
        binding.list.adapter = adapter
        viewModel.data.observe(this) { posts ->
            val new = posts.size > adapter.currentList.size && adapter.currentList.isNotEmpty()
            adapter.submitList(posts) {
                if (new) {
                    binding.list.smoothScrollToPosition(0)
                }
            }
        }
        viewModel.edited.observe(this) {
            if (it.id != 0) {
                binding.content.setText(it.content)
                binding.editGroup.visibility = View.VISIBLE
                binding.editContent.setText(it.content)
                AndroidUtils.showKeyboard(binding.content)
            }
        }

        binding.save.setOnClickListener {
            val text = binding.content.text.toString()
            if (text.isBlank()) {
                Toast.makeText(this@MainActivity, R.string.error_empty_content, Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            viewModel.save(text)
            binding.content.setText("")
            binding.content.clearFocus()
            binding.editGroup.visibility = View.GONE
            AndroidUtils.hideKeyboard(binding.content)

        }
        binding.cancel.setOnClickListener {
            binding.content.setText("")
            binding.content.clearFocus()
            binding.editGroup.visibility = View.GONE
            AndroidUtils.hideKeyboard(binding.content)
        }
    }
}

