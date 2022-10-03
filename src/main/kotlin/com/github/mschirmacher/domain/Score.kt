package com.github.mschirmacher.domain

data class Score(
    val frameScores: List<FrameScore>,
    val total: Int,
)

data class FrameScore(
    val rolls: List<Roll>,
    val subTotal: Int,
)
