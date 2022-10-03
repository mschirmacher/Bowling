package com.github.mschirmacher.domain

data class Roll(
    val pinsHit: Int,
    val pinsLeftBefore: Int,
    val rollNumber: Int,
    val frameNumber: Int,
) {
    fun isSpare() = pinsLeftBefore < 10 && pinsHit == pinsLeftBefore
    fun isStrike() = pinsHit == 10
    fun pinsLeftAfter(): Int = when {
        isSpare()  -> 10
        isStrike() -> 10
        else       -> pinsLeftBefore - pinsHit
    }
}
