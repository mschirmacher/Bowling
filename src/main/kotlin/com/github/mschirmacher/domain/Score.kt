package com.github.mschirmacher.domain

data class Score(
        val frameScores: List<FrameScore>,
        val total: Int,
)

data class FrameScore(
        val rolls: List<Int>,
        val subTotal: Int,
)
