package ru.netology.nmedia.dto

import android.icu.text.DecimalFormat

data class Post(
    val id: Int,
    val author: String,
    val published: String,
    val content: String,
    val countLikes: Int = 999,
    val likeByMe: Boolean = false,
    val countReposts: Int = 302
)

object WallService {

    fun textForCounts(count: Int): String {
        val dfRound = DecimalFormat("#")
        val dfNoRound = DecimalFormat("#.#")

        return when {
            count > 999 && count < 10_000 -> "${dfNoRound.format(count / 1_000.0)}K"
            count >= 10_000 && count < 1_000_000 -> "${dfRound.format(count / 1_000.0)}K"
            count >= 1_000_000 -> "${dfNoRound.format(count / 1_000_000.0)}M"
            else -> count.toString()
        }
    }
}
